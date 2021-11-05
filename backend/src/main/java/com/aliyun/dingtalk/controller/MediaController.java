package com.aliyun.dingtalk.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

import com.aliyun.dingtalk.config.AppConfig;
import com.aliyun.dingtalk.constant.UrlConstant;
import com.aliyun.dingtalk.exception.InvokeDingTalkException;
import com.aliyun.dingtalk.model.ServiceResult;
import com.aliyun.dingtalk.util.AccessTokenUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMediaUploadRequest;
import com.dingtalk.api.response.OapiMediaUploadResponse;
import com.taobao.api.ApiException;
import com.taobao.api.FileItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传媒体文件
 */
@RestController
@Slf4j
public class MediaController {

    @Autowired
    private AppConfig appConfig;

    /**
     * 发送工作通知
     *
     * @return
     */
    @PostMapping("/upload")
    public ServiceResult uploadMedia(@RequestParam(value = "file", required = true) MultipartFile file, HttpServletRequest httpServletRequest)
        throws IOException {

        String accessToken = AccessTokenUtil.getAccessToken(appConfig.getAppKey(), appConfig.getAppSecret());

        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.UPLOAD_MEDIA_URL);
        OapiMediaUploadRequest req = new OapiMediaUploadRequest();
        req.setType("image");
        // 要上传的媒体文件
        byte[] bytes = file.getBytes();
        String realPath = httpServletRequest.getServletContext().getRealPath("/");
        log.info("realPath : {}", realPath);
        String fileName = UUID.randomUUID() + ".jpg";
        File file1 = new File(realPath + fileName);
        getFile(bytes, realPath, fileName);
        FileItem item = new FileItem(file1);
        log.info("item : {}", JSONObject.toJSON(item));
        req.setMedia(item);
        try {
            log.info("upload media request params req: {}, accessToken: {}", JSONObject.toJSON(req), accessToken);
            OapiMediaUploadResponse rsp = client.execute(req, accessToken);
            log.info("upload media response : {}", JSONObject.toJSON(rsp));

            if (rsp.isSuccess()) {

                return ServiceResult.getSuccessResult(rsp.getMediaId());
            } else {
                throw new InvokeDingTalkException(rsp.getErrorCode(), rsp.getErrmsg());
            }
        } catch (ApiException e) {
            e.printStackTrace();
            throw new InvokeDingTalkException(e.getErrCode(), e.getErrMsg());
        }

    }

    public void getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}

