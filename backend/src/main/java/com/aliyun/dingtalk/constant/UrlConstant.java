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
     * 通过免登授权码获取用户信息 url
     */
    public static final String GET_USER_INFO_URL = "https://oapi.dingtalk.com/topapi/v2/user/getuserinfo";

    /**
     * 根据用户id获取用户详情 url
     */
    public static final String USER_GET_URL = "https://oapi.dingtalk.com/topapi/v2/user/get";


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

    /**
     * 获取已读消息人员列表url
     */
    public static final String GET_READ_LIST_URL = "https://oapi.dingtalk.com/chat/getReadList";

    /**
     * 上传媒体文件url
     */
    public static final String UPLOAD_MEDIA_URL = "https://oapi.dingtalk.com/media/upload";

    /**
     * 获取部门成员信息
     */
    public static final String GET_USER_LIST_BY_DEPT_URL = "https://oapi.dingtalk.com/topapi/v2/user/list";
}
