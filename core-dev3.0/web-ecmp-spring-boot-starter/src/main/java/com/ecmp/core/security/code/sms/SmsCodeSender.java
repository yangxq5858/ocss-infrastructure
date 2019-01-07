package com.ecmp.core.security.code.sms;

/**
 * 短信发送接口
 */
public interface SmsCodeSender {

    /**
     * 短信发送
     *
     * @param mobile：手机号
     * @param code：验证码
     */
    void send(String mobile, String code);

}