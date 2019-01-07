package com.ecmp.notify.service;

import com.ecmp.notify.api.IEmailNotifyService;
import com.ecmp.notity.entity.EmailMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：消息通知的服务实现（业务逻辑）
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-04-14 21:14      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Service
public class EmailNotifyService implements IEmailNotifyService {
    @Autowired
    private ContentBuilder contentBuilder;
    //邮件队列生产者
    @Autowired
    private KafkaProducer kafkaProducer;
    /**
     * 发送一封电子邮件
     *
     * @param emailMessage 电子邮件消息
     */
    @Override
    public void sendEmail(EmailMessage emailMessage) {
        // 生成消息内容
        contentBuilder.build(emailMessage);
        // 生成发送邮件的消息队列
        kafkaProducer.send(emailMessage);
    }
}
