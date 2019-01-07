package com.ecmp.core.security.enums;

/**
 * 验证码存储的前缀key，session/redis
 */
public enum ValidateCodeKeyPreffixEnum {

    /**
     * 验证码放入session时的前缀
     */
    SESSION_KEY_PREFIX("SESSION_KEY_FOR_CODE_"),

    /**
     * 验证码放入redis时的前缀
     */
    REDIS_KEY_PREFIX("REDIS_KEY_FOR_CODE_");

    private String preffixKey;

    ValidateCodeKeyPreffixEnum(String preffixKey) {
        this.preffixKey = preffixKey;
    }

    public String preffixKey() {
        return preffixKey;
    }
}
