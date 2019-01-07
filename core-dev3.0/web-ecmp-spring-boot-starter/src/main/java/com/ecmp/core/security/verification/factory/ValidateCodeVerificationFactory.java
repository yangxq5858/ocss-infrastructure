package com.ecmp.core.security.verification.factory;

import com.ecmp.core.security.enums.ValidateCodeTypeEnum;
import com.ecmp.core.security.verification.strategy.ImageValidateCodeVerificationStrategy;
import com.ecmp.core.security.verification.strategy.SmsValidateCodeVerificationStrategy;
import com.ecmp.core.security.verification.strategy.ValidateCodeVerificationStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证码校验工厂
 */
public final class ValidateCodeVerificationFactory {

    private ValidateCodeVerificationFactory() {
    }

    private static class InnterValidateCodeVerification {
        private static final ValidateCodeVerificationFactory INSTANCE = new ValidateCodeVerificationFactory();
    }

    private static Map<String, ValidateCodeVerificationStrategy> maps = new HashMap();

    static {
        maps.put(ValidateCodeTypeEnum.IMAGE.name(), new ImageValidateCodeVerificationStrategy());
        maps.put(ValidateCodeTypeEnum.SMS.name(), new SmsValidateCodeVerificationStrategy());
    }

    public final ValidateCodeVerificationStrategy creator(String key) {
        return maps.get(key);
    }

    public static ValidateCodeVerificationFactory getInstance() {
        return InnterValidateCodeVerification.INSTANCE;
    }
}
