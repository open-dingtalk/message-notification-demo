package com.aliyun.dingtalk.constant;

/**
 * 钉钉开放接口网关常量
 */
public class UrlConstant {

    /**
     * 获取access_token url
     */
    public static final String GET_ACCESS_TOKEN_URL = "https://oapi.dingtalk.com/gettoken";

    /**
     * 异步发送工作通知url
     */
    public static final String ASYNC_SEND_MESSAGE = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";

    /**
     * 创建群会话url
     */
    public static final String CREATE_CHAT_URL = "https://oapi.dingtalk.com/chat/create";

    /**
     * 发送群消息url
     */
    public static final String SEND_CHAT_MESSAGE = "https://oapi.dingtalk.com/chat/send";
}
