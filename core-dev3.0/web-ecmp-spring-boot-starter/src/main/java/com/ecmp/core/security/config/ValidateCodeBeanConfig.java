package com.ecmp.core.security.config;

import com.ecmp.core.security.code.ValidateCodeGenerator;
import com.ecmp.core.security.code.image.ImageCodeGenerator;
import com.ecmp.core.security.code.sms.DefaultSmsCodeSender;
import com.ecmp.core.security.code.sms.SmsCodeSender;
import com.ecmp.core.security.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码bean的配置
 */
@Configuration
public class ValidateCodeBeanConfig {

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 配置图片验证码bean
     * <p>
     * imageValidateCodeGenerator =》{@link com.ecmp.core.security.code.AbstractValidateCodeProcessor} generate() generatorName
     *
     * @return
     */
    @Bean
    /**
     * 此注解可以方便扩展，你自己可以重写一套验证码生成逻辑，而不是我内置的。
     */
    @ConditionalOnMissingBean(name = "imageValidateCodeGenerator")
    public ValidateCodeGenerator imageValidateCodeGenerator() {
        ImageCodeGenerator imageCodeGenerator = new ImageCodeGenerator();
        imageCodeGenerator.setSecurityProperties(securityProperties);
        return imageCodeGenerator;
    }

    /**
     * 配置短信验证码发送bean
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    public SmsCodeSender smsCodeSender() {
        DefaultSmsCodeSender defaultSmsCodeSender = new DefaultSmsCodeSender();
        return defaultSmsCodeSender;
    }

}
