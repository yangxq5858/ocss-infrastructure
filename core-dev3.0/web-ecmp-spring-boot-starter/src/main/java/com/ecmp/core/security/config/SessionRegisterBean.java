package com.ecmp.core.security.config;

import com.ecmp.core.security.properties.SecurityProperties;
import com.ecmp.core.security.session.CoreExpiredSessionStrategy;
import com.ecmp.core.security.session.CoreInvalidSessionStrategy;
import com.ecmp.core.security.session.CoreSessionAuthenticationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

/**
 * Session 管理注册bean
 *
 * @author Vision.Mac 2018-06-06 13:46
 */
@Configuration
public class SessionRegisterBean {

    @Autowired
    private SecurityProperties securityProperties;

    @Bean
    public InvalidSessionStrategy invalidSessionStrategy() {
        return new CoreInvalidSessionStrategy(securityProperties.getSession().getSessionInvalidUrl());
    }

    @Bean
    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
        return new CoreExpiredSessionStrategy(securityProperties.getSession().getSessionInvalidUrl());
    }

    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new CoreSessionAuthenticationStrategy();
    }
}
