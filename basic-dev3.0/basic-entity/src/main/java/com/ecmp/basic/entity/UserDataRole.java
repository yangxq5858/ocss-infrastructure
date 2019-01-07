package com.ecmp.basic.entity;

import com.ecmp.core.entity.BaseAuditableEntity;
import com.ecmp.core.entity.RelationEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：用户分配数据角色
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-04 11:14      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "user_data_role")
@DynamicInsert
@DynamicUpdate
public class UserDataRole extends BaseAuditableEntity implements RelationEntity<User, DataRole> {
    /**
     * 用户
     */
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User parent;
    /**
     * 数据角色
     */
    @ManyToOne
    @JoinColumn(name = "data_role_id",nullable = false)
    private DataRole child;

    /**
     * 父实体
     *
     * @return 父实体
     */
    @Override
    public User getParent() {
        return parent;
    }

    @Override
    public void setParent(User parent) {
        this.parent = parent;
    }

    /**
     * 子实体
     *
     * @return 子实体
     */
    @Override
    public DataRole getChild() {
        return child;
    }

    @Override
    public void setChild(DataRole child) {
        this.child = child;
    }
}
