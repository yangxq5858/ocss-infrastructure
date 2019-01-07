package com.ecmp.notify.service;

import com.ecmp.config.util.ApiClient;
import com.ecmp.config.util.IgnoreCheckSession;
import com.ecmp.context.common.ConfigConstants;
import com.ecmp.notify.api.INotifyService;
import com.ecmp.notity.entity.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.GenericType;
import java.util.*;

//import com.ecmp.mq.producer.MqProducer;

//import static com.ecmp.notify.service.EmailMqConsumer.EMAIL_TOPIC;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：平台消息通知服务
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-06-15 16:35      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Service
public class NotifyService implements INotifyService {
    private static final String GET_USERNOTIFY_INFO_PATH = "userProfile/findNotifyInfoByUserIds";
    @Autowired
    private ContentBuilder contentBuilder;
    //邮件队列生产者
    @Autowired
    private KafkaProducer kafkaProducer;

    /**
     * 发送平台消息通知
     *
     * @param message 平台消息
     */
    @Override
    public void send(EcmpMessage message) {
        //检查消息通知方式
        if (message == null || message.getNotifyTypes() == null || message.getNotifyTypes().isEmpty()) {
            return;
        }
        List<NotifyType> notifyTypes = message.getNotifyTypes();
        //检查收件人是否存在
        if (message.getReceiverIds() == null || message.getReceiverIds().isEmpty()) {
            return;
        }
        List<String> receiverIds = message.getReceiverIds();
        //调用API服务获取用户通知信息
        Set<String> userIds = new HashSet<>(receiverIds);
        if (StringUtils.isNotBlank(message.getSenderId())) {
            userIds.add(message.getSenderId());
        }
        GenericType<List<UserNotifyInfo>> resultClass = new GenericType<List<UserNotifyInfo>>() {};
        List<UserNotifyInfo> userInfos = ApiClient.postViaProxyReturnResult(ConfigConstants.BASIC_API, GET_USERNOTIFY_INFO_PATH, resultClass, new ArrayList<>(userIds));
        if (userInfos == null || userInfos.isEmpty()) {
            return;
        }
        // 生成消息
        contentBuilder.build(message);
        //消息主题
        String subject = message.getSubject();
        //消息内容
        String content = message.getContent();
        UserNotifyInfo sender=null;
        List<UserNotifyInfo> receivers = new ArrayList<>();
        for (UserNotifyInfo info : userInfos) {
            if (StringUtils.isNotBlank(message.getSenderId()) && Objects.equals(info.getUserId(), message.getSenderId())) {
                sender = info;
            } else {
                receivers.add(info);
            }
        }
        //判断是否发送给发件人
        if (message.isCanToSender()&&Objects.nonNull(sender)){
            if (receiverIds.contains(sender.getUserId())){
                receivers.add(sender);
            }
        }
        if (receivers.isEmpty()) {
            return;
        }
        //发送
        for (NotifyType notifyType: notifyTypes){
            switch (notifyType){
                //发送邮件
                case Email:
                    //构造邮件消息
                    EmailMessage emailMsg = new EmailMessage();
                    if (Objects.nonNull(sender)&&StringUtils.isNotBlank(sender.getEmail())){
                        emailMsg.setSender(new EmailAccount(sender.getUserName(),sender.getEmail()));
                    }
                    emailMsg.setSubject(subject);
                    emailMsg.setContent(content);
                    List<EmailAccount> emailAccounts = new ArrayList<>();
                    receivers.forEach((r)->emailAccounts.add(new EmailAccount(r.getUserName(),r.getEmail())));
                    emailMsg.setReceivers(emailAccounts);
                    //发送邮件
                    kafkaProducer.send(emailMsg);
                    break;
                case Sms:
                    break;
            }
        }
  }

    /**
     * Hello World
     *
     * @param message 消息
     * @return 问候消息
     */
    @Override
    @IgnoreCheckSession
    public String hello(String message) {
        return "Hello！"+message;
    }
}
