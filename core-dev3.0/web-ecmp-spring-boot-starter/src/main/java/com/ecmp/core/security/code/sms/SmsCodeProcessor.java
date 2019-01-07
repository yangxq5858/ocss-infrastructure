package com.ecmp.core.security.code.sms;

import com.ecmp.core.security.code.AbstractValidateCodeProcessor;
import com.ecmp.core.security.code.ValidateCode;
import com.ecmp.core.security.enums.ValidateCodeParamNameEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 短信验证码处理器
 */
@Component("smsValidateCodeProcessor")
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

    @Autowired
    private SmsCodeSender smsCodeSender;

    @Override
    protected void send(ServletWebRequest request, ValidateCode validateCode) throws Exception {
        String paramName = ValidateCodeParamNameEnum.DEFAULT_PARAMETER_NAME_MOBILE.value();
        String mobile = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), paramName);
        smsCodeSender.send(mobile, validateCode.getCode());
    }
}
