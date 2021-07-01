package com.aliyun.dingtalk.service.message.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dingtalk.config.AppConfig;
import com.aliyun.dingtalk.constant.UrlConstant;
import com.aliyun.dingtalk.enums.MsgTypeEnum;
import com.aliyun.dingtalk.exception.InvokeDingTalkException;
import com.aliyun.dingtalk.model.DingTalkMessage;
import com.aliyun.dingtalk.model.DingTalkWorkNoticeInputVO;
import com.aliyun.dingtalk.model.GroupMessageInputVO;
import com.aliyun.dingtalk.model.WorkNotice;
import com.aliyun.dingtalk.service.message.MessageNotificationService;
import com.aliyun.dingtalk.util.AccessTokenUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiChatCreateRequest;
import com.dingtalk.api.request.OapiChatGetReadListRequest;
import com.dingtalk.api.request.OapiChatSendRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiChatCreateResponse;
import com.dingtalk.api.response.OapiChatGetReadListResponse;
import com.dingtalk.api.response.OapiChatSendResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MessageNotificationServiceImpl implements MessageNotificationService {

    @Autowired
    private AppConfig appConfig;

    /**
     * 发送异步工作通知 参考文档：https://developers.dingtalk.com/document/app/asynchronous-sending-of-enterprise-session-messages
     * 获取企业全员信息 参考文档：https://developers.dingtalk.com/document/app/obtains-information-about-all-employees-of-an-enterprise
     *
     * @return
     */
    @Override
    public Long sendWorkNotice(DingTalkWorkNoticeInputVO dingTalkWorkNoticeInputVO) {

        String accessToken = AccessTokenUtil.getAccessToken(appConfig.getAppKey(), appConfig.getAppSecret());

        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.ASYNC_SEND_MESSAGE);

        OapiMessageCorpconversationAsyncsendV2Request request = buildOapiMessageCorpconversationAsyncsendV2Request(dingTalkWorkNoticeInputVO);

        try {

            log.info("send work notice request params req: {}, accessToken: {}", JSONObject.toJSON(request), accessToken);
            OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(request, accessToken);
            log.info("send work notice rsp: {}", JSONObject.toJSON(rsp));

            if (rsp.isSuccess()) {
                Long taskId = rsp.getTaskId();
                return taskId;
            } else {
                throw new InvokeDingTalkException(rsp.getErrorCode(), rsp.getErrmsg());
            }


        } catch (ApiException e) {
            e.printStackTrace();
            throw new InvokeDingTalkException(e.getErrCode(), e.getErrMsg());
        }
    }

    /**
     * 构建发送工作通知请求对象
     *
     * @param dingTalkWorkNoticeInputVO
     * @return
     */
    private OapiMessageCorpconversationAsyncsendV2Request buildOapiMessageCorpconversationAsyncsendV2Request(DingTalkWorkNoticeInputVO dingTalkWorkNoticeInputVO) {
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();

        request.setAgentId(appConfig.getAppAgentId());
        // userId 需要调用接口获取
        request.setUseridList(dingTalkWorkNoticeInputVO.getUserIdList().stream().collect(Collectors.joining(",")));
        // 是否发送给企业全部用户 当设置为false时必须指定userid_list或dept_id_list其中一个参数的值
        request.setToAllUser(dingTalkWorkNoticeInputVO.getToAllUser());

        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = buildOapiMessageCorpconversationAsyncsendV2RequestMsg(dingTalkWorkNoticeInputVO.getWorkNotice());

        request.setMsg(msg);
        return request;
    }

    /**
     * 构建工作通知消息
     *
     * @param workNotice
     * @return
     */
    private OapiMessageCorpconversationAsyncsendV2Request.Msg buildOapiMessageCorpconversationAsyncsendV2RequestMsg(WorkNotice workNotice) {

        // 创建消息

        // 根据消息类型构建不同的消息
        if (MsgTypeEnum.TEXT.getMsgType().equals(workNotice.getMsgType())) {

            // 获取前端请求的文本消息
            WorkNotice.TextMessage textMessage = workNotice.getTextMessage();

            // 文本消息
            OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            msg.setMsgtype(workNotice.getMsgType());

            // 构建文本消息内容
            OapiMessageCorpconversationAsyncsendV2Request.Text text = buildTextMessage(textMessage);

            msg.setText(text);

            return msg;
        } else {
            // TODO: 2021/6/30 build other message
            throw new RuntimeException("msgType not match");
        }

    }

    /**
     * 构建文本消息内容
     * @param textMessage
     * @return
     */
    private OapiMessageCorpconversationAsyncsendV2Request.Text buildTextMessage(WorkNotice.TextMessage textMessage) {

        OapiMessageCorpconversationAsyncsendV2Request.Text text = new OapiMessageCorpconversationAsyncsendV2Request.Text();
        text.setContent(textMessage.getContent());

        return text;
    }

    /**
     * 发送群消息
     *
     * @return
     */
    @Override
    public String sendGroupMessage(GroupMessageInputVO groupMessageInputVO) {

        String accessToken = AccessTokenUtil.getAccessToken(appConfig.getAppKey(), appConfig.getAppSecret());

        // 创建群会话
        String chatId = createChat(accessToken, groupMessageInputVO);

        // 发送群消息
        String messageId = sendChatMessage(chatId, accessToken, groupMessageInputVO.getDingTalkMessage());

        return messageId;
    }

    /**
     * 获取群消息已读人员列表
     *
     * @param messageId
     * @return
     */
    @Override
    public List<String> getReadList(String messageId) {

        String accessToken = AccessTokenUtil.getAccessToken(appConfig.getAppKey(), appConfig.getAppSecret());

        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.GET_READ_LIST_URL);

        Long cursor = 0L;

        Long size = 100L;

        OapiChatGetReadListRequest req = new OapiChatGetReadListRequest();
        req.setMessageId(messageId);
        req.setCursor(cursor);
        req.setSize(size);
        req.setHttpMethod(HttpMethod.GET.name());

        List<String> userIdList = new ArrayList<>();

        try {
            while (true) {

                OapiChatGetReadListResponse rsp = client.execute(req, accessToken);

                if (rsp.isSuccess()) {

                    userIdList.addAll(rsp.getReadUserIdList());

                    if (!hasMore(size, rsp)) {
                        cursor++;
                        return userIdList;
                    }
                } else {
                    throw new InvokeDingTalkException(rsp.getErrorCode(), rsp.getErrmsg());
                }
            }

        } catch (ApiException e) {
            e.printStackTrace();
            throw new InvokeDingTalkException(e.getErrCode(), e.getErrMsg());
        }

    }

    /**
     * 是否有更多已读人员
     * @param size
     * @param rsp
     * @return
     */
    private boolean hasMore(Long size, OapiChatGetReadListResponse rsp) {
        return rsp.getReadUserIdList().size() >= size;
    }

    /**
     * 创建群会话
     *
     * @param accessToken
     * @return
     */
    private String createChat(String accessToken, GroupMessageInputVO groupMessageInputVO) {

        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.CREATE_CHAT_URL);
        OapiChatCreateRequest req = new OapiChatCreateRequest();
        req.setName(groupMessageInputVO.getName());
        req.setOwner(groupMessageInputVO.getOwner());
        req.setUseridlist(groupMessageInputVO.getUserIdList());
        try {
            log.info("createChat request params req: {}, accessToken: {}", JSONObject.toJSON(req), accessToken);
            OapiChatCreateResponse rsp = client.execute(req, accessToken);
            log.info("createChat rsp : {}", JSONObject.toJSON(rsp));

            if (rsp.isSuccess()) {

                return rsp.getChatid();
            } else {
                throw new InvokeDingTalkException(rsp.getErrorCode(), rsp.getErrmsg());
            }
        } catch (ApiException e) {
            e.printStackTrace();
            throw new InvokeDingTalkException(e.getErrCode(), e.getErrMsg());
        }
    }

    /**
     * 发送群消息
     *
     * @param chatId
     * @param accessToken
     * @param dingTalkMessage
     * @return
     */
    private String sendChatMessage(String chatId, String accessToken, DingTalkMessage dingTalkMessage) {

        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.SEND_CHAT_MESSAGE);
        OapiChatSendRequest req = new OapiChatSendRequest();
        req.setChatid(chatId);

        OapiChatSendRequest.Msg msg = buildOapiChatSendRequestMsg(dingTalkMessage);

        req.setMsg(msg);

        try {

            log.info("sendChatMessage request params req: {}, accessToken: {}", JSONObject.toJSON(req), accessToken);
            OapiChatSendResponse rsp = client.execute(req, accessToken);
            log.info("sendChatMessage rsp: {}", JSONObject.toJSON(rsp));
            if (rsp.isSuccess()) {

                String messageId = rsp.getMessageId();

                return messageId;

            } else {
                throw new InvokeDingTalkException(rsp.getErrorCode(), rsp.getErrmsg());
            }
        } catch (ApiException e) {
            e.printStackTrace();
            throw new InvokeDingTalkException(e.getErrCode(), e.getErrMsg());
        }
    }


    /**
     * 构建钉钉消息
     *
     * @param dingTalkMessage
     * @return
     */
    private OapiChatSendRequest.Msg buildOapiChatSendRequestMsg(DingTalkMessage dingTalkMessage) {

        // 根据消息类型构建不同的消息
        if (MsgTypeEnum.LINK.getMsgType().equals(dingTalkMessage.getMsgType())) {

            // 获取前端请求的链接消息
            DingTalkMessage.LinkMessage linkMessage = dingTalkMessage.getLink();

            // 群消息
            OapiChatSendRequest.Msg msg = new OapiChatSendRequest.Msg();
            msg.setMsgtype(dingTalkMessage.getMsgType());

            // 构建链接消息
            OapiChatSendRequest.Link link = buildLink(linkMessage);
            msg.setLink(link);
            return msg;
        } else {
            // TODO: 2021/6/30 build other message
            throw new RuntimeException("msgType not match");
        }
    }

    /**
     * 构建链接消息
     *
     * @param linkMessage
     * @return
     */
    private OapiChatSendRequest.Link buildLink(DingTalkMessage.LinkMessage linkMessage) {
        OapiChatSendRequest.Link link = new OapiChatSendRequest.Link();
        link.setMessageUrl(linkMessage.getMessageUrl());
        link.setPicUrl(linkMessage.getPicUrl());
        link.setText(linkMessage.getText());
        link.setTitle(linkMessage.getTitle());
        return link;
    }


}
