package com.aliyun.dingtalk.service.message;


import com.aliyun.dingtalk.model.DingTalkWorkNoticeInputVO;
import com.aliyun.dingtalk.model.GroupMessageInputVO;

import java.util.List;

public interface MessageNotificationService {

    Long sendWorkNotice(DingTalkWorkNoticeInputVO dingTalkWorkNoticeInputVO);

    String sendGroupMessage(GroupMessageInputVO groupMessageInputVO);

    List<String> getReadList(String messageId);
}
