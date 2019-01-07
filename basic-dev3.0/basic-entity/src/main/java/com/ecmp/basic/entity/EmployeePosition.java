package com.ecmp.basic.entity;

import com.ecmp.core.entity.BaseAuditableEntity;
import com.ecmp.core.entity.RelationEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：企业员工用户分配岗位
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/5/11 20:10      秦有宝                     新建
 * <p/>
 * *************************************************************************************************
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "employee_position")
@DynamicUpdate
@DynamicInsert
public class EmployeePosition extends BaseAuditableEntity implements RelationEntity<Employee, Position> {
    /**
     * 员工
     */
    @ManyToOne
    @JoinColumn(name = "employee_id",nullable = false)
    private Employee parent;
    /**
     * 岗位
     */
    @ManyToOne
    @JoinColumn(name = "position_id",nullable = false)
    private Position child;

    public Employee getParent() {
        return parent;
    }

    public void setParent(Employee parent) {
        this.parent = parent;
    }

    public Position getChild() {
        return child;
    }

    public void setChild(Position child) {
        this.child = child;
    }
}
