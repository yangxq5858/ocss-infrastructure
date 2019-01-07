package com.ecmp.basic.entity.vo;

import com.ecmp.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 专家用户VO
 * Author:jamson
 * date:2018/3/13
 */
public class ExpertUserVo implements Serializable {
    private String id;
    private String code;
    private String name;
    private List<String> professionalDomainIds;
    private String professionalDomainName;
    @JsonFormat(timezone = DateUtils.DEFAULT_TIMEZONE, pattern = DateUtils.DEFAULT_DATE_FORMAT)
    private Date expireDate;
    /**
     * 专家ID（同步过来的源表中的ID）
     */
    private String expertId;
    private Boolean frozen;
    private String email;
    private Boolean gender;
    private String mobile;
    private String idCard;

    public String getProfessionalDomainName() {
        return professionalDomainName;
    }

    public void setProfessionalDomainName(String professionalDomainName) {
        this.professionalDomainName = professionalDomainName;
    }

    public String getExpertId() {
        return expertId;
    }

    public void setExpertId(String expertId) {
        this.expertId = expertId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getProfessionalDomainIds() {
        return professionalDomainIds;
    }

    public void setProfessionalDomainIds(List<String> professionalDomainIds) {
        this.professionalDomainIds = professionalDomainIds;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Boolean getFrozen() {
        return frozen;
    }

    public void setFrozen(Boolean frozen) {
        this.frozen = frozen;
    }

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
}
