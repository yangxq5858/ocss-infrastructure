package com.ecmp.core.security.verification;

import com.ecmp.core.security.code.ValidateCode;
import com.ecmp.core.security.enums.ValidateCodeKeyPreffixEnum;
import com.ecmp.core.security.enums.ValidateCodeTypeEnum;
import com.ecmp.core.security.exception.ValidateCodeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.concurrent.TimeUnit;

/**
 * Redis存验证码，实现前后分离
 */
public class RedisValidateCodeRepository implements ValidateCodeRepository {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void save(ServletWebRequest request, ValidateCode validateCode, ValidateCodeTypeEnum validateCodeType) {
        redisTemplate.opsForValue().set(getRedisKey(request, validateCodeType), validateCode, 60, TimeUnit.SECONDS);
    }

    @Override
    public ValidateCode get(ServletWebRequest request, ValidateCodeTypeEnum validateCodeType) {
        return (ValidateCode) redisTemplate.opsForValue().get(getRedisKey(request, validateCodeType));
    }

    @Override
    public void remove(ServletWebRequest request, ValidateCodeTypeEnum validateCodeType) {
        redisTemplate.delete(getRedisKey(request, validateCodeType));
    }

    /**
     * 获取redisKey
     *
     * @return
     */
    private String getRedisKey(ServletWebRequest request, ValidateCodeTypeEnum validateCodeType) {
        String deviceId = request.getHeader("deviceId");
        if (StringUtils.isBlank(deviceId)) {
            throw new ValidateCodeException("请在请求头中携带deviceId参数");
        }
        return ValidateCodeKeyPreffixEnum.REDIS_KEY_PREFIX.preffixKey() + validateCodeType.toString().toUpperCase() + "_" + deviceId;
    }

}
