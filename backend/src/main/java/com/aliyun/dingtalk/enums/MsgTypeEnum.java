package com.aliyun.dingtalk.enums;

/**
 * 消息类型枚举
 */
public enum MsgTypeEnum {

    /**
     * 链接消息
     */
    LINK("link"),

    /**
     * 文本消息
     */
    TEXT("text");

    MsgTypeEnum(String msgType) {
        this.msgType = msgType;
    }

    private String msgType;

    public String getMsgType() {
        return msgType;
    }

}
