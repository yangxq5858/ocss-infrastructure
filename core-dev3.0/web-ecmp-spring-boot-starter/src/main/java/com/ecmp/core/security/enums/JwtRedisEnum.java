package com.ecmp.core.security.enums;

/**
 * Redis存储jwt的key前缀
 */
public enum JwtRedisEnum {

    /**
     * token 的key前缀
     */
    TOKEN_KEY_PREFIX("jwt:"),

    /***
     * authentication的key
     */
    AUTHENTICATION_KEY_PREFIX("authentication:"),
    /**
     * 用户有权限的功能项
     */
    USER_AUTHORIZED_FEATURE_KEY_PREFIX("UAF:")
    ;

    private String value;


    JwtRedisEnum(String value) {
        this.value = value;
    }

    /**
     * 获取key
     */
    public static String getTokenKey(String randomKey) {
        return TOKEN_KEY_PREFIX.value + randomKey;
    }

    /**
     * 获取身份认证key
     *
     * @param username：用户名
     */
    public static String getAuthenticationKey(String username, String randomKey) {
        return AUTHENTICATION_KEY_PREFIX.value + username + ":" + randomKey;
    }

    /**
     * 获取用户有权限的功能项key
     *
     * @param userId：用户id
     */
    public static String getUserAuthorizedFeatureKey(String userId) {
        return USER_AUTHORIZED_FEATURE_KEY_PREFIX.value + userId;
    }
}
