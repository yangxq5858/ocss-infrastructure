package com.ecmp.core.security.code.image;

import com.ecmp.core.security.code.AbstractValidateCodeProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;

/**
 * 图形验证码处理器
 * <p>
 * imageValidateCodeProcessor ==> {@link com.ecmp.core.security.code.ValidateCodeProcessorHolder}
 */
@Component("imageValidateCodeProcessor")
public class ImageCodeProcessor extends AbstractValidateCodeProcessor<ImageCode> {

    /**
     * 将验证码发到客户端。
     *
     * @param request：请求
     * @param code：验证码
     * @throws IOException
     */
    @Override
    protected void send(ServletWebRequest request, ImageCode code) throws IOException {
        request.getResponse().getWriter().print(code.getImage());
    }
}
