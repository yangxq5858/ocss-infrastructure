package com.ecmp.basic.entity;

import com.ecmp.core.entity.BaseAuditableEntity;
import com.ecmp.core.entity.RelationEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：数据角色分配权限类型的值
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
@Table(name = "data_role_auth_type_value")
@DynamicUpdate
@DynamicInsert
public class DataRoleAuthTypeValue extends BaseAuditableEntity {
    /**
     * 默认构造函数
     */
    public DataRoleAuthTypeValue(){}

    /**
     * 构造函数
     * @param roleId 数据角色Id
     * @param authTypeId 数据权限类型Id
     * @param entityId 权限对象实体Id
     */
    public DataRoleAuthTypeValue(String roleId,String authTypeId,String entityId){
        dataRole = new DataRole();
        dataRole.setId(roleId);
        dataAuthorizeType = new DataAuthorizeType();
        dataAuthorizeType.setId(authTypeId);
        this.entityId = entityId;
    }
    /**
     * 数据角色
     */
    @ManyToOne
    @JoinColumn(name = "data_role_id",nullable = false)
    private DataRole dataRole;
    /**
     * 数据权限类型
     */
    @ManyToOne
    @JoinColumn(name = "data_authorize_type_id",nullable = false)
    private DataAuthorizeType dataAuthorizeType;
    /**
     * 权限对象实体Id
     */
    @Column(name = "entity_id",length = 36,nullable = false)
    private String entityId;

    public DataRole getDataRole() {
        return dataRole;
    }

    public void setDataRole(DataRole dataRole) {
        this.dataRole = dataRole;
    }

    public DataAuthorizeType getDataAuthorizeType() {
        return dataAuthorizeType;
    }

    public void setDataAuthorizeType(DataAuthorizeType dataAuthorizeType) {
        this.dataAuthorizeType = dataAuthorizeType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
}
