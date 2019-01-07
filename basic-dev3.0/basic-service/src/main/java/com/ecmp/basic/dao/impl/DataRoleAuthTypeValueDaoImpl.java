package com.ecmp.basic.dao.impl;

import com.ecmp.basic.dao.DataRoleAuthTypeValueExtDao;
import com.ecmp.basic.entity.DataRoleAuthTypeValue;
import com.ecmp.basic.entity.vo.DataRoleRelation;
import com.ecmp.core.dao.impl.BaseEntityDaoImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：数据角色分配权限类型的值数据访问实现
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-06-01 9:04      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class DataRoleAuthTypeValueDaoImpl extends BaseEntityDaoImpl<DataRoleAuthTypeValue>
        implements DataRoleAuthTypeValueExtDao {
    //构造函数
    public DataRoleAuthTypeValueDaoImpl(EntityManager entityManager) {
        super(DataRoleAuthTypeValue.class, entityManager);
    }

    /**
     * 通过数据角色和数据权限类型获取分配关系
     *
     * @param roleId         数据角色Id
     * @param dataAuthTypeId 数据权限类型Id
     * @return 分配关系
     */
    @Override
    @Transactional(readOnly = true)
    public DataRoleRelation getDataRoleRelation(String roleId, String dataAuthTypeId) {
        DataRoleRelation relation = new DataRoleRelation();
        relation.setDataRoleId(roleId);
        relation.setDataAuthorizeTypeId(dataAuthTypeId);
        relation.setEntityIds(getAssignedEntityIds(roleId,dataAuthTypeId));
        return relation;
    }

    /**
     * 通过分配关系参数获取分配关系的Id清单
     *
     * @param relation 分配关系参数
     * @return 分配关系的Id清单
     */
    @Override
    public List<String> getRelationIds(DataRoleRelation relation) {
        String sql = "select r.id from DataRoleAuthTypeValue r " +
                "where r.dataRole.id=:roleId " +
                "and r.dataAuthorizeType.id=:dataAuthTypeId " +
                "and r.entityId in :entityIds";
        Query query = entityManager.createQuery(sql);
        query.setParameter("roleId",relation.getDataRoleId());
        query.setParameter("dataAuthTypeId",relation.getDataAuthorizeTypeId());
        query.setParameter("entityIds",relation.getEntityIds());
        List ids = query.getResultList();
        List<String> result = new ArrayList<>();
        for (Object id:ids){
            result.add((String)id);
        }
        return result;
    }

    /**
     * 通过分配关系参数获取分配关系清单
     *
     * @param relation 分配关系参数
     * @return 分配关系的Id清单
     */
    @Override
    public List<DataRoleAuthTypeValue> getRelationValues(DataRoleRelation relation) {
        String sql = "select r from DataRoleAuthTypeValue r " +
                "where r.dataRole.id=:roleId " +
                "and r.dataAuthorizeType.id=:dataAuthTypeId " +
                "and r.entityId in :entityIds";
        Query query = entityManager.createQuery(sql);
        query.setParameter("roleId",relation.getDataRoleId());
        query.setParameter("dataAuthTypeId",relation.getDataAuthorizeTypeId());
        query.setParameter("entityIds",relation.getEntityIds());
        return query.getResultList();
    }

    /**
     * 判断数据角色是否已经存在分配权限对象
     *
     * @param roleId         数据角色Id
     * @param dataAuthTypeId 数据权限类型Id
     * @return 是否已经存在分配
     */
    @Override
    public boolean isAlreadyAssign(String roleId, String dataAuthTypeId) {
        String sql = "select r.id from DataRoleAuthTypeValue r " +
                "where r.dataRole.id=:roleId " +
                "and r.dataAuthorizeType.id=:dataAuthTypeId ";
        Query query = entityManager.createQuery(sql);
        query.setParameter("roleId",roleId);
        query.setParameter("dataAuthTypeId",dataAuthTypeId);
        List results = query.getResultList();
        return !results.isEmpty();
    }

    /**
     * 通过数据角色和权限类型获取已分配的业务实体Id的清单
     *
     * @param roleId         数据角色Id
     * @param dataAuthTypeId 数据权限类型Id
     * @return 业务实体Id的清单
     */
    @Override
    public List<String> getAssignedEntityIds(String roleId, String dataAuthTypeId) {
        String sql = "select r.entityId from DataRoleAuthTypeValue r " +
                "where r.dataRole.id=:roleId " +
                "and r.dataAuthorizeType.id=:dataAuthTypeId ";
        Query query = entityManager.createQuery(sql);
        query.setParameter("roleId",roleId);
        query.setParameter("dataAuthTypeId",dataAuthTypeId);
        List ids = query.getResultList();
        List<String> result = new ArrayList<>();
        for (Object id:ids){
            result.add((String)id);
        }
        return result;
    }
}
