package com.ecmp.core.security.verification.strategy;

import com.ecmp.core.security.enums.ValidateCodeParamNameEnum;
import com.ecmp.core.security.enums.ValidateCodeTypeEnum;
import com.ecmp.core.security.verification.ValidateCodeRepository;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码校验接口
 */
public class SmsValidateCodeVerificationStrategy implements ValidateCodeVerificationStrategy {

    @Override
    public void verification(ValidateCodeRepository validateCodeRepository, ServletWebRequest request, ValidateCodeTypeEnum validateCodeType) {
        new CommonValidateCodeVerification().verifity(validateCodeRepository, request, validateCodeType, ValidateCodeParamNameEnum.DEFAULT_PARAMETER_NAME_CODE_SMS.value());
    }
}
