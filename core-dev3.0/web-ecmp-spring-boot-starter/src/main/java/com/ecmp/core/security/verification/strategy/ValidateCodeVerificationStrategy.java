package com.ecmp.core.security.verification.strategy;

import com.ecmp.core.security.enums.ValidateCodeTypeEnum;
import com.ecmp.core.security.verification.ValidateCodeRepository;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码校验接口
 */
public interface ValidateCodeVerificationStrategy {

    /**
     * 校验验证码
     *
     * @param validateCodeRepository：验证码存取删接口
     * @param request：请求
     * @param validateCodeType：验证码类型
     */
    void verification(ValidateCodeRepository validateCodeRepository, ServletWebRequest request, ValidateCodeTypeEnum validateCodeType);
}
