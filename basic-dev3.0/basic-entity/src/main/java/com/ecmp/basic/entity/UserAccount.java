package com.ecmp.basic.entity;

import com.ecmp.enums.UserType;
import com.ecmp.core.entity.BaseAuditableEntity;
import com.ecmp.core.entity.ITenant;

import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：用户帐号实体类
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间                  变更人                 变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/17 14:19            高银军                  新建
 * 1.0.00      2017/4/25 19:26            秦有宝                  增加字段(密码，加密盐值)
 * <p/>
 * *************************************************************************************************
 */
@Entity
@Table(name = "user_account")
@Access(AccessType.FIELD)
@DynamicInsert
@DynamicUpdate
public class UserAccount extends BaseAuditableEntity implements ITenant {

    /**
     * 用户昵称
     */
    @Column(name = "nickname", length = 50, nullable = false)
    private String nickName;
    /**
     * 账号
     */
    @Column(name = "account", length = 50, nullable = false)
    private String account;

    /**
     * 用户密码
     */
    @Column(length = 100, nullable = false)
    private String password;

    /**
     * 加密盐值
     */
    @Column(length = 36, nullable = false, updatable = false)
    private String salt;

    /**
     * 租户代码
     */
    @Column(name = "tenant_code", length = 10, nullable = false, unique = true)
    private String tenantCode;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    /**
     * 用户名
     */
    @Transient
    private String userName;

    /**
     * 用户类型
     */
    @Transient
    private UserType userType;

    /**
     * 用户类型中文描述
     */
    @Transient
    private String userTypeRemark;

    /**
     * 是否是创建租户管理员
     */
    @Transient
    private boolean createAdmin;

    /**
     * 邮箱,创建租户管理员时发送邮件
     */
    @Transient
    private String email;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String getTenantCode() {
        return tenantCode;
    }

    @Override
    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getUserTypeRemark() {
        return userTypeRemark;
    }

    public void setUserTypeRemark(String userTypeRemark) {
        this.userTypeRemark = userTypeRemark;
    }

    public boolean isCreateAdmin() {
        return createAdmin;
    }

    public void setCreateAdmin(boolean createAdmin) {
        this.createAdmin = createAdmin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
