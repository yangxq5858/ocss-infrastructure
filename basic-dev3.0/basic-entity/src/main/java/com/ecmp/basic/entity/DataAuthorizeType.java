package com.ecmp.basic.entity;

import com.ecmp.core.entity.BaseAuditableEntity;
import com.ecmp.core.entity.ICodeUnique;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：数据权限类型
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-31 9:34      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "data_authorize_type")
@DynamicUpdate
@DynamicInsert
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DataAuthorizeType extends BaseAuditableEntity implements ICodeUnique {
    /**
     * 代码
     */
    @Column(name = "code", length = 50, nullable = false, unique = true)
    private String code;

    /**
     * 名称
     */
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    /**
     * 权限对象类型
     */
    @ManyToOne
    @JoinColumn(name = "authorize_entity_type_id", nullable = false)
    private AuthorizeEntityType authorizeEntityType;

    /**
     * 功能项
     */
    @ManyToOne
    @JoinColumn(name = "feature_id")
    private Feature feature;

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

    public AuthorizeEntityType getAuthorizeEntityType() {
        return authorizeEntityType;
    }

    public void setAuthorizeEntityType(AuthorizeEntityType authorizeEntityType) {
        this.authorizeEntityType = authorizeEntityType;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }
}
