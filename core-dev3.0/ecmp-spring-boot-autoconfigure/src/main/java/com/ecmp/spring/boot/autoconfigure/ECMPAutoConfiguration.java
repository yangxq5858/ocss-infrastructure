package com.ecmp.spring.boot.autoconfigure;

import com.ecmp.context.BaseApplicationContext;
import com.ecmp.util.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/5/28 23:48
 */
public class ECMPAutoConfiguration {

    @Bean
    public BaseApplicationContext ecmpContext() {
        return new BaseApplicationContext();
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/lang/ecmp-lang", "classpath:/lang/messages");
        messageSource.setCacheSeconds(120);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    public JwtTokenUtil jwtTokenUtil(Environment env) {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        //JWT签名密钥
        String secret = env.getProperty("com.ecmp.security.jwt.secret");
        if (StringUtils.isNotBlank(secret)) {
            jwtTokenUtil.setJwtSecret(secret);
        }

        /*
            由于ECMP2.0前段使用EUI无法实时接收token的刷新，暂使用sessionId作为超时控制。
            将sessionId与token在redis建立关联关系，以sessionId为key值，token为value值。
            根据sessionId的超时来控制token的超时，因此token的超时时间应远大于session的超时时间
         */
        //会话超时时间。
        int tokenTimeout = env.getProperty("com.ecmp.security.jwt.expiration", Integer.class, 3600);
        int sessionTimeout = env.getProperty("server.servlet.session.timeout", Integer.class, 3600);
        if (tokenTimeout < sessionTimeout * 10) {
            tokenTimeout = sessionTimeout * 10;
        }
        //JWT过期时间（秒）
        jwtTokenUtil.setJwtExpiration(tokenTimeout);
        return jwtTokenUtil;
    }

}
