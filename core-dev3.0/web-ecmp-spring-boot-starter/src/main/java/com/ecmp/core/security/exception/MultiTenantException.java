package com.ecmp.core.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 需要继承AuthenticationException以表明它是security的认证失败,这样才会走后续的失败流程
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2017/10/11 16:57
 */
public class MultiTenantException extends AuthenticationException {

    public MultiTenantException(String msg) {
        super(msg);
    }

    public MultiTenantException(String msg, Throwable t) {
        super(msg, t);
    }

}
