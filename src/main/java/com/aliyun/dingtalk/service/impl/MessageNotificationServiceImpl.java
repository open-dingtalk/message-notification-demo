package com.aliyun.dingtalk.service.impl;

import com.aliyun.dingtalk.config.AppConfig;
import com.aliyun.dingtalk.constant.UrlConstant;
import com.aliyun.dingtalk.service.MessageNotificationService;
import com.aliyun.dingtalk.util.AccessTokenUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiChatCreateRequest;
import com.dingtalk.api.request.OapiChatSendRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiChatCreateResponse;
import com.dingtalk.api.response.OapiChatSendResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

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
    public String sendWorkNotice() {

        String accessToken = AccessTokenUtil.getAccessToken(appConfig.getAppKey(), appConfig.getAppSecret());

        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.ASYNC_SEND_MESSAGE);

        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setAgentId(appConfig.getAppAgentId());
        // userId 需要调用接口获取
        request.setUseridList("***");
        // 是否发送给企业全部用户 当设置为false时必须指定userid_list或dept_id_list其中一个参数的值
        request.setToAllUser(false);

        // 创建消息
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype("text");
        msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
        msg.getText().setContent("test123");

        request.setMsg(msg);

        try {
            OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(request, accessToken);
            log.info("send work notice rsp: {}", rsp);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 发送群消息
     * @return
     */
    @Override
    public String sendGroupMessage() {
        String accessToken = AccessTokenUtil.getAccessToken(appConfig.getAppKey(), appConfig.getAppSecret());

        // 创建群会话
        String openConversationId = createChat(accessToken);

        // 发送群消息
        String messageId = sendChatMessage(openConversationId, accessToken);

        return messageId;
    }

    /**
     * 创建群会话
     * @param accessToken
     * @return
     */
    private String createChat(String accessToken) {
        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.CREATE_CHAT_URL);
        OapiChatCreateRequest req = new OapiChatCreateRequest();
        req.setName("全员群");
        req.setOwner("043217290519980938");
        req.setUseridlist(Arrays.asList("043217290519980938"));
        try {
            OapiChatCreateResponse rsp = client.execute(req, accessToken);
            if (!Objects.isNull(rsp)) {
                if (rsp.isSuccess()) {

                    String openConversationId = rsp.getOpenConversationId();
                    return openConversationId;
                } else {
                    log.error("create chat error, errCode: {}, errMsg: {}", rsp.getErrcode(), rsp.getErrmsg());
                }
            } else {
                log.error("create chat fail");
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String sendChatMessage(String openConversationId, String accessToken) {
        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.SEND_CHAT_MESSAGE);
        OapiChatSendRequest req = new OapiChatSendRequest();
        req.setChatid(openConversationId);
        OapiChatSendRequest.Msg msg = new OapiChatSendRequest.Msg();
        OapiChatSendRequest.Text text = new OapiChatSendRequest.Text();

        text.setContent("请于本月底提交月度工作报告。");
        msg.setText(text);
        msg.setMsgtype("text");
        req.setMsg(msg);
        try {
            OapiChatSendResponse rsp = client.execute(req, accessToken);
            if (!Objects.isNull(rsp)) {
                if (rsp.isSuccess()) {

                    String messageId = rsp.getMessageId();
                    return messageId;
                } else {
                    log.error("create chat error, errCode: {}, errMsg: {}", rsp.getErrcode(), rsp.getErrmsg());
                }
            } else {
                log.error("create chat fail");
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }


}
