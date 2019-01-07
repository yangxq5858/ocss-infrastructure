package com.ecmp.basic.dao;

import com.ecmp.basic.entity.DataRoleAuthTypeValue;
import com.ecmp.basic.entity.vo.DataRoleRelation;

import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：数据角色分配权限类型的值数据访问扩展接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-06-01 8:57      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public interface DataRoleAuthTypeValueExtDao {
    /**
     * 通过数据角色和数据权限类型获取分配关系
     * @param roleId 数据角色Id
     * @param dataAuthTypeId 数据权限类型Id
     * @return 分配关系
     */
    DataRoleRelation getDataRoleRelation(String roleId,String dataAuthTypeId);

    /**
     * 通过分配关系参数获取分配关系的Id清单
     * @param relation 分配关系参数
     * @return 分配关系的Id清单
     */
    List<String> getRelationIds(DataRoleRelation relation);

    /**
     * 通过分配关系参数获取分配关系清单
     * @param relation 分配关系参数
     * @return 分配关系的Id清单
     */
    List<DataRoleAuthTypeValue> getRelationValues(DataRoleRelation relation);

    /**
     * 判断数据角色是否已经存在分配权限对象
     * @param roleId 数据角色Id
     * @param dataAuthTypeId 数据权限类型Id
     * @return 是否已经存在分配
     */
    boolean isAlreadyAssign(String roleId,String dataAuthTypeId);

    /**
     * 通过数据角色和权限类型获取已分配的业务实体Id的清单
     * @param roleId 数据角色Id
     * @param dataAuthTypeId 数据权限类型Id
     * @return 业务实体Id的清单
     */
    List<String> getAssignedEntityIds(String roleId,String dataAuthTypeId);
}
