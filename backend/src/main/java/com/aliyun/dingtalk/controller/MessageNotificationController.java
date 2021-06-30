package com.aliyun.dingtalk.controller;

import com.aliyun.dingtalk.model.GroupMessageInputVO;
import com.aliyun.dingtalk.service.MessageNotificationService;
import com.aliyun.dingtalk.model.RpcServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 钉钉消息通知
 */
@RestController
public class MessageNotificationController {

    @Autowired
    private MessageNotificationService messageNotificationService;

    /**
     * 欢迎页面, 检查后端服务是否启动
     *
     * @return
     */
    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    /**
     * 发送工作通知
     *
     * @return
     */
    @PostMapping("/message/work")
    public RpcServiceResult sendWorkNotice(@RequestBody List<String> userIdList) {

        return RpcServiceResult.getSuccessResult(messageNotificationService.sendWorkNotice(userIdList));

    }

    /**
     * 发送群消息
     *
     * @return
     */
    @PostMapping("/message/group")
    public RpcServiceResult sendGroupMessage(@RequestBody GroupMessageInputVO groupMessageInputVO) {

        return RpcServiceResult.getSuccessResult(messageNotificationService.sendGroupMessage(groupMessageInputVO));

    }

    /**
     * 根据消息ID获取已读人员列表
     *
     * @return
     */
    @GetMapping("/message/group/{messageId}")
    public RpcServiceResult getReadList(@PathVariable String messageId) {

        return RpcServiceResult.getSuccessResult(messageNotificationService.getReadList(messageId));

    }



}
