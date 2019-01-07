package com.ecmp.basic.entity.vo;

import java.io.Serializable;

/**
 * <p>
 * 实现功能：用户密码修改vo
 * <p/>
 *
 * @author 秦有宝
 * @version 1.0.00
 */
public class UserPasswordVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户账号Id
     */
    private String userAccountId;

    /**
     * 用户账号
     */
    private String account;

    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 确认新密码
     */
    private String confirmNewPassword;

    public String getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(String userAccountId) {
        this.userAccountId = userAccountId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
