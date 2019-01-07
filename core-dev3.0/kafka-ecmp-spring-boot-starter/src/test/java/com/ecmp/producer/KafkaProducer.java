package com.ecmp.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 发送消息
     *
     * @param obj 消息
     */
    public void send(String obj) {
        log.info("发送消息:{}", obj);
        kafkaTemplate.send("ecmp_log_exception", obj);
        log.info("Kafka消息已发送");
    }
}
