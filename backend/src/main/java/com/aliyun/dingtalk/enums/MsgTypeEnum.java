package com.aliyun.dingtalk.enums;

import com.aliyun.dingtalk.model.DingTalkMessage;

/**
 * 消息类型枚举
 */
public enum MsgTypeEnum {

    LINK("link", DingTalkMessage.LinkMessage.class);

    MsgTypeEnum(String msgType, Class aClass) {
        this.msgType = msgType;
        this.aClass = aClass;
    }

    private String msgType;

    private Class aClass;

    public String getMsgType() {
        return msgType;
    }

    public Class getaClass() {
        return aClass;
    }
}
