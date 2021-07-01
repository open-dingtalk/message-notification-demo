package com.aliyun.dingtalk.model;

import lombok.Data;

@Data
public class WorkNotice {

    private String msgType;

    private TextMessage textMessage;

    @Data
    public static class TextMessage {

        private String content;

    }

}
