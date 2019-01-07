package com.ecmp.core.security.config;

import com.ecmp.core.security.authentication.CoreAuthenticationFailureHandler;
import com.ecmp.core.security.authentication.CoreAuthenticationSuccessHandler;
import com.ecmp.core.security.authentication.CoreLogoutSuccessHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * 处理成功/失败的配置类
 */
@Configuration
public class CoreAuthenticationHandlerConfig {

    /**
     * 成功处理器
     */
    @Bean
    @ConditionalOnMissingBean(name = "logoutSuccessHandler")
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CoreLogoutSuccessHandler();
    }

    /**
     * 成功处理器
     */
    @Bean
    @ConditionalOnMissingBean(name = "authenticationSuccessHandler")
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CoreAuthenticationSuccessHandler();
    }

    /**
     * 失败处理器
     */
    @Bean
    @ConditionalOnMissingBean(name = "authenticationFailureHandler")
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CoreAuthenticationFailureHandler();
    }
}