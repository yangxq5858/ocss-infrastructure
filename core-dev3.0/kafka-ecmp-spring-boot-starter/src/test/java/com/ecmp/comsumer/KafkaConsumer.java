package com.ecmp.comsumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * <strong>实现功能:</strong>
 * <p>发送邮件的消费者</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2018-06-15 14:25
 */
@Component
public class KafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    /**
     * 处理收到的监听消息
     *
     * @param record 消息纪录
     */
    @KafkaListener(topics = "ecmp_log_exception")
    public void processMessage(ConsumerRecord<?, ?> record) {
        log.info("topict:ecmp_email 接收消息记录");
        if (Objects.isNull(record)) {
            return;
        }
        Object content = record.value();
        log.info("---消息内容：{}", content);
    }
}
