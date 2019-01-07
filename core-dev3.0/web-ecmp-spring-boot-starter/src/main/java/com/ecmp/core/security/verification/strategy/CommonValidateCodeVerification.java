package com.ecmp.core.security.verification.strategy;

import com.ecmp.core.security.code.ValidateCode;
import com.ecmp.core.security.enums.ValidateCodeTypeEnum;
import com.ecmp.core.security.exception.ValidateCodeException;
import com.ecmp.core.security.verification.ValidateCodeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 通用的校验逻辑，图形验证码/sms验证码公用
 */
public class CommonValidateCodeVerification {

    /**
     * 通用的校验逻辑，图形验证码/sms验证码公用
     *
     * @param validateCodeRepository：验证码存取删接口
     * @param request：请求
     * @param validateCodeType：验证码类型
     * @param codeParam：验证码参数
     */
    public void verifity(ValidateCodeRepository validateCodeRepository, ServletWebRequest request, ValidateCodeTypeEnum validateCodeType, String codeParam) {
        ValidateCode code = validateCodeRepository.get(request, validateCodeType);
        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), codeParam);
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败！");
        }
        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }

        if (code == null) {
            throw new ValidateCodeException("验证码不存在，请刷新页面重试");
        }

        if (code.isExpired()) {
            validateCodeRepository.remove(request, validateCodeType);
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equals(code.getCode(), codeInRequest)) {
            validateCodeRepository.remove(request, validateCodeType);
            throw new ValidateCodeException("验证码不匹配");
        }
    }

}
