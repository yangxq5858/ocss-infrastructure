package com.ecmp.core.security.config;

import com.ecmp.core.security.verification.RedisValidateCodeRepository;
import com.ecmp.core.security.verification.SessionValidateCodeRepository;
import com.ecmp.core.security.verification.ValidateCodeRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码的存取删接口配置类
 */
@Configuration
public class ValidateCodeRepositoryConfig {

    /**
     * 配置{@link SessionValidateCodeRepository}
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "com.ecmp.security.code", value = "repository", havingValue = "session", matchIfMissing = true)
    public ValidateCodeRepository sessionValidateCodeRepository() {
        return new SessionValidateCodeRepository();
    }

    /**
     * 配置{@link RedisValidateCodeRepository}
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "com.ecmp.security.code", value = "repository", havingValue = "redis")
    public ValidateCodeRepository redisValidateCodeRepository() {
        return new RedisValidateCodeRepository();
    }
}
