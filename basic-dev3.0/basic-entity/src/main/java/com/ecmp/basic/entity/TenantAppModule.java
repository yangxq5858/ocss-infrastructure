package com.ecmp.basic.entity;

import com.ecmp.core.entity.BaseAuditableEntity;
import com.ecmp.core.entity.RelationEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：功能角色分配的功能项
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-04 11:08      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "tenant_app_module")
@DynamicInsert
@DynamicUpdate
public class TenantAppModule extends BaseAuditableEntity implements RelationEntity<Tenant, AppModule> {
    /**
     * 功能角色
     */
    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant parent;
    /**
     * 功能项
     */
    @ManyToOne
    @JoinColumn(name = "app_module_id", nullable = false)
    private AppModule child;

    public Tenant getParent() {
        return parent;
    }

    public void setParent(Tenant parent) {
        this.parent = parent;
    }

    public AppModule getChild() {
        return child;
    }

    public void setChild(AppModule child) {
        this.child = child;
    }
}
