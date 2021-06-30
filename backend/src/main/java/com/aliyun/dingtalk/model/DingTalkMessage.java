package com.aliyun.dingtalk.model;

import lombok.Data;

@Data
public class DingTalkMessage {

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 链接消息
     */
    private LinkMessage link;

    /**
     * link 链接消息
     */
    @Data
    public static class LinkMessage {

        /**
         * 消息点击链接地址，当发送消息为小程序时支持小程序跳转链接
         */
        private String messageUrl;

        /**
         * 图片地址，可以通过上传媒体文件接口获取。
         */
        private String picUrl;

        /**
         * 消息标题，建议100字符以内。
         */
        private String title;

        /**
         * 消息描述，建议500字符以内。
         */
        private String text;
    }
}
