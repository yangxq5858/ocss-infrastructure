package com.ecmp.basic.entity;

import com.ecmp.core.entity.BaseAuditableEntity;

import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：用户配置实体
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间                  变更人                 变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/14 15:45            高银军                  新建
 * <p/>
 * *************************************************************************************************
 */
@Access(AccessType.FIELD)
@Entity
@Table(name = "user_profile")
@DynamicInsert
@DynamicUpdate
public class UserProfile extends BaseAuditableEntity {
    /**
     * 邮箱
     */
    @Column(name = "email", length = 100)
    private String email;
    /**
     * 性别 ，true表示男，false表示女
     */
    @Column(name = "gender")
    private Boolean gender;
    /**
     * 语言代码
     */
    @Column(name = "language_code", length = 10)
    private String languageCode;
    /**
     * 身份证号码
     */
    @Column(name = "id_card", length = 20)
    private String idCard;
    /**
     * 移动电话
     */
    @Column(name = "mobile", length = 20)
    private String mobile;
    /**
     * 记账用户
     */
    @Column(name = "accountor", length = 20)
    private String accountor;

    @OneToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccountor() {
        return accountor;
    }

    public void setAccountor(String accountor) {
        this.accountor = accountor;
    }
}
