package com.ecmp.notify.config;

import com.ecmp.context.ContextUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * <strong>实现功能:</strong>
 * <p>发送EMAIL的配置类</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2018-06-14 16:36
 */
@Configuration
@DependsOn("ecmpContext")
public class MailSenderConfig {
    private static final String EMAIL_CONFIG_KEY="ECMP_EMAIL";

    @Bean
    public Properties javaMailProperties(){
        //获取EMAIL配置
        Properties emailProperties = ContextUtil.getGlobalProperties(EMAIL_CONFIG_KEY);
        if (emailProperties.isEmpty()){
            throw new ExceptionInInitializerError("系统没有配置EMAIL服务的参数："+EMAIL_CONFIG_KEY);
        }
        return emailProperties;
    }

    @Bean
    public JavaMailSender javaMailSender(Properties javaMailProperties) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        String host = javaMailProperties.getProperty("host");
        mailSender.setHost(host);
        int port = Integer.parseInt(javaMailProperties.getProperty("port"));
        mailSender.setPort(port);

        mailSender.setUsername(javaMailProperties.getProperty("user"));
        mailSender.setPassword(javaMailProperties.getProperty("password"));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.trust", host);

        return mailSender;
    }
}
