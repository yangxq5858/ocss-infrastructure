package com.ecmp.notify.service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：服务器邮箱登录验证
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-04-17 10:37      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class MailAuthenticator extends Authenticator {
    private String username;
    private String password;

    /**
     * 构造函数
     * @param username 用户名（登录邮箱）
     * @param password 密码
     */
    public MailAuthenticator(String username,String password){
        this.username = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }

    public String getUsername() {
        return username;
    }
}
