package com.ecmp.notify.service;

import com.ecmp.log.util.LogUtil;
import com.ecmp.notity.entity.EmailMessage;
import com.ecmp.util.JsonUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private EmailProcesser emailProcesser;

    /**
     * 处理收到的监听消息
     * @param record 消息纪录
     */
    @KafkaListener(topics = "ecmp_email")
    public void processMessage(ConsumerRecord<String, String> record){
        LogUtil.info("topict:ecmp_email 接收消息记录");
        if (Objects.isNull(record)){
            return;
        }
        String content = record.value();
        LogUtil.info("---消息内容：{}", content);
        EmailMessage message = JsonUtils.fromJson(content, EmailMessage.class);
        if (Objects.isNull(message)){
            return;
        }
        try {
            emailProcesser.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("邮件消息处理失败！",e);
        }
        LogUtil.info("topict:ecmp_email 邮件消息处理完成！");
    }
}
