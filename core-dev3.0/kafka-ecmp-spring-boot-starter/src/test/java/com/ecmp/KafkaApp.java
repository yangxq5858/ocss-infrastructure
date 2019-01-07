package com.ecmp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <strong>实现功能:</strong>
 * <p>发送邮件的生产者</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2018-06-15 13:47
 */
@SpringBootApplication
public class KafkaApp {

    public static void main(String[] args) {
        SpringApplication.run(KafkaApp.class);
    }

}
