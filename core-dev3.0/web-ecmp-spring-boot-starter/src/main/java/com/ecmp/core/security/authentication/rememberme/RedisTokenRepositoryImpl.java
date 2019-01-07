package com.ecmp.core.security.authentication.rememberme;

import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.util.Date;

/**
 * redis存储session token
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/6/30 13:49
 */
public class RedisTokenRepositoryImpl implements PersistentTokenRepository {
    private static final String KEY_PREFIX = "RememberMe:";
    @Nullable
    private RedisTemplate redisTemplate;

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String getRememberMeKey(String seriesId) {
        return KEY_PREFIX + seriesId;
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        BoundValueOperations valueOps = redisTemplate.boundValueOps(token.getSeries());
        valueOps.set(token);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        BoundValueOperations valueOps = redisTemplate.boundValueOps(series);
        PersistentRememberMeToken token = (PersistentRememberMeToken) valueOps.get();

        PersistentRememberMeToken newToken = new PersistentRememberMeToken(
                token.getUsername(), series, tokenValue, new Date());

        // Store it, overwriting the existing one.
        valueOps.set(newToken);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        BoundValueOperations valueOps = redisTemplate.boundValueOps(seriesId);
        PersistentRememberMeToken current = (PersistentRememberMeToken) valueOps.get();
        return current;
    }

    @Override
    public void removeUserTokens(String username) {
        BoundListOperations listOps = redisTemplate.boundListOps(KEY_PREFIX + "*");
        Long size = listOps.size();
        if (size != null && size > 0) {
            for (int i = 0; i < size; i++) {
                PersistentRememberMeToken token = (PersistentRememberMeToken) listOps.index(i);
                //TODO 存在相同用户名不同租户删除问题
                if (token != null && username.equals(token.getUsername())) {
                    redisTemplate.delete(token.getSeries());
                }
            }
        }
    }
}
