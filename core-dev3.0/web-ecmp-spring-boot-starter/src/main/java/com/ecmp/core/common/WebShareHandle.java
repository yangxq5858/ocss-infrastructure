package com.ecmp.core.common;

import com.ecmp.context.ContextUtil;
import com.ecmp.core.security.enums.JwtRedisEnum;
import com.ecmp.spring.boot.autoconfigure.SessionConfig;
import com.ecmp.util.JsonUtils;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2017/10/20 14:08
 */
public final class WebShareHandle {
    public static final String SESSION_USER = "_SessionUser";
    public static final String REQUEST_FEATURE_MAPS = "_FeatureMaps";

    private static StringRedisTemplate redisTemplate;

    private static StringRedisTemplate getRedisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = ContextUtil.getApplicationContext().getBean(StringRedisTemplate.class);
        }
        return redisTemplate;
    }

    /**
     * 设置用户前端权限检查的功能项键值到缓存
     *
     * @param key         会话id
     * @param accessToken AccessToken
     */
    public static void setAccessToken(String key, String accessToken) {
        getRedisTemplate().opsForValue().set(JwtRedisEnum.getTokenKey(key),
                accessToken, SessionConfig.sessionTimeout(), TimeUnit.SECONDS);
    }

    public static void setUserAuthorizedFeatureMap(String userId, String userAuthorizedFeatureJson) {
        getRedisTemplate().opsForValue().set(JwtRedisEnum.getUserAuthorizedFeatureKey(userId), userAuthorizedFeatureJson);
    }

    /**
     * 更新缓存过期时间
     */
    public static void touch(String key) {
        BoundValueOperations valueOperations;
        //权限缓存
        valueOperations = getRedisTemplate().boundValueOps(JwtRedisEnum.getTokenKey(key));
        valueOperations.expire(SessionConfig.sessionTimeout(), TimeUnit.SECONDS);
    }

    /**
     * 删除缓存
     */
    public static void removeToken(String key) {
        //权限缓存
        getRedisTemplate().delete(JwtRedisEnum.getTokenKey(key));
    }

//    /**
//     * 删除缓存
//     */
//    public static void remove(String key) {
//        //权限缓存
//        getRedisTemplate().delete(JwtRedisEnum.getTokenKey(key));
//    }

    public static String getAccessToken(String key) {
        String value = getRedisTemplate().opsForValue().get(JwtRedisEnum.getTokenKey(key));
        return value;
    }

    /**
     * 获取用户前端权限检查的功能项键值
     */
    public static Map<String, Map<String, String>> getUserAuthorizedFeatureMap(String userId) {
        Map<String, Map<String, String>> allUserAuthorizedFeatureMap = null;
        BoundValueOperations valueOperations = getRedisTemplate().boundValueOps(JwtRedisEnum.getUserAuthorizedFeatureKey(userId));
        if (valueOperations != null) {
            String jsonData = (String) valueOperations.get();
            allUserAuthorizedFeatureMap = JsonUtils.fromJson(jsonData, HashMap.class);
        }
        return allUserAuthorizedFeatureMap;
    }

    /**
     * 获取指定模块代码用户前端权限检查的功能项键值
     */
    public static Map<String, String> getUserAuthorizedFeatureMap(String userId, String appModuleCode) {
        Map<String, String> moduleUserAuthorizedFeatureMap = null;
        Map<String, Map<String, String>> allUserAuthorizedFeatureMap = getUserAuthorizedFeatureMap(userId);
        if (allUserAuthorizedFeatureMap != null && allUserAuthorizedFeatureMap.size() > 0) {
            moduleUserAuthorizedFeatureMap = allUserAuthorizedFeatureMap.get(appModuleCode);
        }
        return moduleUserAuthorizedFeatureMap;
    }
}
