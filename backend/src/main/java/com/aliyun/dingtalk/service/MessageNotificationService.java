package com.aliyun.dingtalk.service;


import com.aliyun.dingtalk.model.GroupMessageInputVO;

import java.util.List;

public interface MessageNotificationService {

    Long sendWorkNotice(List<String> userIdList);

    String sendGroupMessage(GroupMessageInputVO groupMessageInputVO);

    List<String> getReadList(String messageId);
}
