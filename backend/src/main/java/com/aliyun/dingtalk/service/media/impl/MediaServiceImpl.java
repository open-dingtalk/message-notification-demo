package com.aliyun.dingtalk.service.media.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dingtalk.config.AppConfig;
import com.aliyun.dingtalk.constant.UrlConstant;
import com.aliyun.dingtalk.exception.InvokeDingTalkException;
import com.aliyun.dingtalk.service.media.MediaService;
import com.aliyun.dingtalk.util.AccessTokenUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMediaUploadRequest;
import com.dingtalk.api.response.OapiMediaUploadResponse;
import com.taobao.api.ApiException;
import com.taobao.api.FileItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@Slf4j
public class MediaServiceImpl implements MediaService {

    @Autowired
    private AppConfig appConfig;

    /**
     * 上传媒体文件到钉钉，可以在钉钉发送卡片消息时作为图片,demo是直接取项目路径下面的图片
     */
    @Override
    public String uploadMedia() {

        String accessToken = AccessTokenUtil.getAccessToken(appConfig.getAppKey(), appConfig.getAppSecret());

        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.UPLOAD_MEDIA_URL);
        OapiMediaUploadRequest req = new OapiMediaUploadRequest();
        req.setType("image");
        // 要上传的媒体文件
        InputStream is = getClass().getClassLoader().getResourceAsStream("static/card.jpg");
        FileItem item = new FileItem("card.jpg", is);
        req.setMedia(item);
        try {
            log.info("upload media request params req: {}, accessToken: {}", JSONObject.toJSON(req), accessToken);
            OapiMediaUploadResponse rsp = client.execute(req, accessToken);
            log.info("upload media response : {}", JSONObject.toJSON(rsp));

            if (rsp.isSuccess()) {
                return rsp.getMediaId();
            } else {
                throw new InvokeDingTalkException(rsp.getErrorCode(), rsp.getErrmsg());
            }
        } catch (ApiException e) {
            e.printStackTrace();
            throw new InvokeDingTalkException(e.getErrCode(), e.getErrMsg());
        }
    }
}
