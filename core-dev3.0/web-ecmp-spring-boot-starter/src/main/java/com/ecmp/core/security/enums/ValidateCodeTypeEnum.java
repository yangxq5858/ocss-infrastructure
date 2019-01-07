package com.ecmp.core.security.enums;

/**
 * 验证码类型
 */
public enum ValidateCodeTypeEnum {

    /**
     * 图形验证码
     */
    IMAGE {
        @Override
        public String getParamNameOnValidate() {
            return ValidateCodeParamNameEnum.DEFAULT_PARAMETER_NAME_CODE_IMAGE.value();
        }
    },

    /**
     * 短信验证码
     */
    SMS {
        @Override
        public String getParamNameOnValidate() {
            return ValidateCodeParamNameEnum.DEFAULT_PARAMETER_NAME_MOBILE.value();
        }
    };

    /**
     * 校验验证码的时候，从请求中获取的参数的名称
     *
     * @return
     */
    public abstract String getParamNameOnValidate();

}
