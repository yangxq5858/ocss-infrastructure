package com.ecmp.basic.entity;

import com.ecmp.core.entity.BaseAuditableEntity;
import com.ecmp.core.entity.ITenant;
import com.ecmp.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/3/6 20:03
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "expert")
@DynamicUpdate
@DynamicInsert
public class ExpertUser extends BaseAuditableEntity implements ITenant {
    /**
     * 代码
     */
    @Column(name = "code", length = 30, nullable = false)
    private String code;
    /**
     * 名称
     */
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    /**
     * 有效期
     */
    @JsonFormat(timezone = DateUtils.DEFAULT_TIMEZONE, pattern = DateUtils.DEFAULT_DATE_FORMAT)
    @Temporal(TemporalType.DATE)
    @Column(name = "expire_date", nullable = false)
    private Date expireDate;

    /**
     * 租户代码
     */
    @Column(name = "tenant_code", length = 10, nullable = false, unique = true)
    private String tenantCode;
    /**
     * 专家ID（同步过来的源表中的ID）
     */
    @Column(name = "expert_id", length = 36, nullable = false)
    private String expertId;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    public String getExpertId() {
        return expertId;
    }

    public void setExpertId(String expertId) {
        this.expertId = expertId;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
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

    @Override
    public String getTenantCode() {
        return tenantCode;
    }

    @Override
    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
