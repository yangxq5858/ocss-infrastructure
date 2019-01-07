package com.ecmp.core.security.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码处理器接口
 */
public interface ValidateCodeProcessor {

    /**
     * 创建验证码
     *
     * @param request：请求
     * @throws Exception
     */
    void create(ServletWebRequest request) throws Exception;

    /**
     * 校验验证码
     *
     * @param request：请求
     */
    void validate(ServletWebRequest request);
}