package com.ecmp.basic.entity.vo;

import com.ecmp.basic.entity.UserAccount;

import java.io.Serializable;
/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：用户账户view object
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/5/27 13:52      秦有宝                     新建
 * <p/>
 * *************************************************************************************************
 */
public class UserAccountVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户Id
     */
    private String id;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 账号
     */
    private String account;

    /**
     * 构造函数
     */
    public UserAccountVo(){
    }

    /**
     * 通过用户账户构造
     * @param userAccount 用户账户
     */
    public UserAccountVo(UserAccount userAccount){
        id = userAccount.getId();
        userId = userAccount.getUser().getId();
        nickName = userAccount.getNickName();
        account = userAccount.getAccount();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
