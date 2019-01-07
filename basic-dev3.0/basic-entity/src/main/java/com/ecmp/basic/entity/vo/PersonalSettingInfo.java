package com.ecmp.basic.entity.vo;

import com.ecmp.basic.entity.UserProfile;
import com.ecmp.enums.UserType;
import com.ecmp.util.EnumUtils;

import java.io.Serializable;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：个人设置信息
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/6/28 16:36      秦有宝                     新建
 * <p/>
 * *************************************************************************************************
 */
public class PersonalSettingInfo extends UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 组织机构名称
     */
    private String organizationName;

    /**
     * 用户类型中文描述
     */
    private String userTypeRemark;

    /**
     * 用户编号,0代表员工，1代表合作伙伴
     */
    private String code;

    public PersonalSettingInfo() {
    }

    public PersonalSettingInfo(UserProfile userProfile) {
        this.setId(userProfile.getId());
        this.setEmail(userProfile.getEmail());
        this.setGender(userProfile.getGender());
        this.setLanguageCode(userProfile.getLanguageCode());
        this.setIdCard(userProfile.getIdCard());
        this.setMobile(userProfile.getMobile());
        this.setAccountor(userProfile.getAccountor());
        this.setUser(userProfile.getUser());
        this.setUserTypeRemark(EnumUtils.getEnumItemRemark(UserType.class,userProfile.getUser().getUserType().ordinal()));
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getUserTypeRemark() {
        return userTypeRemark;
    }

    public void setUserTypeRemark(String userTypeRemark) {
        this.userTypeRemark = userTypeRemark;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
