package com.aliyun.dingtalk.service;


import java.util.List;

public interface MessageNotificationService {

    String sendWorkNotice();

    String sendGroupMessage();

    List<String> getReadList(String messageId);
}
