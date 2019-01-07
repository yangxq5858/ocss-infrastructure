package com.ecmp.core.security.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码生成接口
 */
public interface ValidateCodeGenerator {

    /**
     * 生成验证码
     *
     * @param request：请求
     * @return
     */
    ValidateCode generate(ServletWebRequest request);

}
