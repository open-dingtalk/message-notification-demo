package com.aliyun.dingtalk.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dingtalk.model.DingTalkWorkNoticeInputVO;
import com.aliyun.dingtalk.model.GroupMessageInputVO;
import com.aliyun.dingtalk.service.message.MessageNotificationService;
import com.aliyun.dingtalk.model.ServiceResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



/**
 * 钉钉消息通知
 */
@Slf4j
@RestController
public class MessageNotificationController {

    @Autowired
    private MessageNotificationService messageNotificationService;


    /**
     * 发送工作通知
     *
     * @return
     */
    @PostMapping("/message/work")
    public ServiceResult sendWorkNotice(@RequestBody DingTalkWorkNoticeInputVO dingTalkWorkNoticeInputVO) {
        log.info("MessageNotificationController#sendWorkNotice param : {}", JSONObject.toJSON(dingTalkWorkNoticeInputVO));
        return ServiceResult.getSuccessResult(messageNotificationService.sendWorkNotice(dingTalkWorkNoticeInputVO));

    }

    /**
     * 发送群消息
     *
     * @return
     */
    @PostMapping("/message/group")
    public ServiceResult sendGroupMessage(@RequestBody GroupMessageInputVO groupMessageInputVO) {
        log.info("MessageNotificationController#sendGroupMessage param : {}", JSONObject.toJSON(groupMessageInputVO));
        return ServiceResult.getSuccessResult(messageNotificationService.sendGroupMessage(groupMessageInputVO));

    }

    /**
     * 根据消息ID获取已读人员列表
     *
     * @return
     */
    @GetMapping("/message/group/{messageId}")
    public ServiceResult getReadList(@PathVariable String messageId) {
        log.info("MessageNotificationController#getReadList param : {}", messageId);
        return ServiceResult.getSuccessResult(messageNotificationService.getReadList(messageId));

    }



}
