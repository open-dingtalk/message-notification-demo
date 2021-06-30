package com.aliyun.dingtalk.model;

import lombok.Data;

import java.util.List;

@Data
public class GroupMessageInputVO {

    /**
     * 群名称
     */
    private String name;

    /**
     * 群所有者
     */
    private String owner;

    /**
     * 群成员列表，每次最多支持40人，群人数上限为1000
     */
    private List<String> userIdList;

    private DingTalkMessage dingTalkMessage;

}
