package com.ecmp.basic.entity;

import com.ecmp.core.entity.BaseAuditableEntity;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ecmp.core.entity.ICodeUnique;
import com.ecmp.core.entity.IFrozen;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：租户实体
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
@Table(name = "tenant")
@DynamicInsert
@DynamicUpdate
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tenant extends BaseAuditableEntity
        implements ICodeUnique,IFrozen{
    /**
     * 租户代码
     */
    @Column(name = "code", length = 10, unique = true, nullable = false)
    private String code;
    /**
     * 租户名称
     */
    @Column(name = "name", length = 200, nullable = false)
    private String name;

    /**
     * 是否冻结，0代表未冻结，1代表冻结
     */
    @Column(name = "frozen", nullable = false)
    private Boolean frozen = Boolean.FALSE;

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
    public Boolean getFrozen() {
        return frozen;
    }

    public void setFrozen(Boolean frozen) {
        this.frozen = frozen;
    }
}
