package com.ecmp.basic.entity;

import com.ecmp.core.entity.BaseAuditableEntity;
import com.ecmp.core.entity.ICodeUnique;
import com.ecmp.core.entity.ITenant;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：数据角色组
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-04 10:51      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "data_role_group")
@DynamicUpdate
@DynamicInsert
public class DataRoleGroup extends BaseAuditableEntity implements ITenant,ICodeUnique {
    /**
     * 租户代码
     */
    @Column(name = "tenant_code",length = 10,nullable = false)
    private String tenantCode;
    /**
     * 代码
     */
    @Column(name = "code",unique = true,length = 20,nullable = false)
    private String code;
    /**
     * 名称
     */
    @Column(name = "name",length = 50,nullable = false)
    private String name;

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
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
}
