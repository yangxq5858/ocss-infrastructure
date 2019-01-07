package com.ecmp.core.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码自定义异常
 * 需要继承AuthenticationException以表明它是security的认证失败,这样才会走后续的失败流程
 */
public class ValidateCodeException extends AuthenticationException {

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
