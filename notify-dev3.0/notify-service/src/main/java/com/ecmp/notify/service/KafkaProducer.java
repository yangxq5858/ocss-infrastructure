package com.ecmp.notify.service;

import com.ecmp.log.util.LogUtil;
import com.ecmp.notity.entity.EmailMessage;
import com.ecmp.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * <strong>实现功能:</strong>
 * <p>发送邮件的生产者</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2018-06-15 13:47
 */
@Component
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 发送消息
     * @param emailMessage 消息
     */
    void send(EmailMessage emailMessage) {
        String message = JsonUtils.toJson(emailMessage);
        LogUtil.info("发送邮件消息:{}", message);
        kafkaTemplate.send("ecmp_email", message);
        LogUtil.info("Kafka邮件消息已发送");
    }
}
