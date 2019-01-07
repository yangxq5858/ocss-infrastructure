package com.ecmp.notity.entity;

import java.io.Serializable;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：用户的消息通知信息
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-06-15 16:50      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class UserNotifyInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 邮箱地址
     */
    private String email;
    /**
     * 移动电话
     */
    private String mobile;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
