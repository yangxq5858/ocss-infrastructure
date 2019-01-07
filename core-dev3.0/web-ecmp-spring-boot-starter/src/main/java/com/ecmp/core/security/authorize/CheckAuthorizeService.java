package com.ecmp.core.security.authorize;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/7/2 23:18
 */
public interface CheckAuthorizeService {

    /**
     * 权限检查
     *
     * @return 返回true时，允许访问
     */
    public boolean hasPermission(HttpServletRequestWrapper request, Authentication authentication);
}
