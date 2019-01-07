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
@Table(name = "feature_role_feature")
@DynamicInsert
@DynamicUpdate
public class FeatureRoleFeature extends BaseAuditableEntity implements RelationEntity<FeatureRole,Feature> {
    /**
     * 功能角色
     */
    @ManyToOne
    @JoinColumn(name = "feature_role_id",nullable = false)
    private FeatureRole parent;
    /**
     * 功能项
     */
    @ManyToOne
    @JoinColumn(name = "feature_id",nullable = false)
    private Feature child;

    public FeatureRole getParent() {
        return parent;
    }

    public void setParent(FeatureRole parent) {
        this.parent = parent;
    }

    public Feature getChild() {
        return child;
    }

    public void setChild(Feature child) {
        this.child = child;
    }
}
