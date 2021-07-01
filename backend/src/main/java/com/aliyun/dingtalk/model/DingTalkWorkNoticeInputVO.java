package com.aliyun.dingtalk.model;

import lombok.Data;

import java.util.List;

@Data
public class DingTalkWorkNoticeInputVO {

    private List<String> userIdList;

    private Boolean toAllUser;

    private WorkNotice workNotice;

}
