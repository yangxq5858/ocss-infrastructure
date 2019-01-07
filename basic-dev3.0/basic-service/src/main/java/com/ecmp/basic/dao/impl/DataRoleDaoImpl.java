package com.ecmp.basic.dao.impl;

import com.ecmp.basic.dao.DataRoleExtDao;
import com.ecmp.basic.dao.DataRoleGroupDao;
import com.ecmp.basic.entity.*;
import com.ecmp.core.dao.impl.BaseEntityDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-06-05 10:15      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class DataRoleDaoImpl  extends BaseEntityDaoImpl<DataRole> implements DataRoleExtDao {
    @Autowired
    private DataRoleGroupDao dataRoleGroupDao;

    //构造函数
    public DataRoleDaoImpl(EntityManager entityManager) {
        super(DataRole.class, entityManager);
    }

    /**
     * 获取用户的全局公共角色
     *
     * @param user 用户
     * @return 全局公共角色
     */
    @Override
    @Transactional(readOnly = true)
    public List<DataRole> getPublicRoles(User user) {
        if (Objects.isNull(user)){
            return new ArrayList<>();
        }
        String sql = "select r from DataRole r where r.tenantCode=:tenantCode and r.publicUserType=:publicUserType and r.publicOrg is null";
        Query query = entityManager.createQuery(sql);
        query.setParameter("tenantCode",user.getTenantCode());
        query.setParameter("publicUserType",user.getUserType());
        List queryResult = query.getResultList();
        List<DataRole> result = new ArrayList<>();
        for (Object r:queryResult
             ) {
                result.add((DataRole)r);
        }
        return result;
    }

    /**
     * 获取用户组织机构的公共角色
     *
     * @param user 用户
     * @param orgs 组织机构父节点清单
     * @return 组织机构公共角色
     */
    @Override
    @Transactional(readOnly = true)
    public List<DataRole> getPublicRoles(User user, List<Organization> orgs) {
        List<DataRole> result = new ArrayList<>();
        if (Objects.isNull(user)||Objects.isNull(orgs)||orgs.isEmpty()){
            return result;
        }
        //获取租户的角色组
        List<DataRoleGroup> groups = dataRoleGroupDao.findByTenantCode(user.getTenantCode());
        for (DataRoleGroup group:groups){
            //循环组织机构
            for (Organization org:orgs){
                //获取匹配的公共角色
                String sql = "select r from DataRole r where r.tenantCode=:tenantCode " +
                        "and r.publicUserType=:publicUserType " +
                        "and r.dataRoleGroup.id=:dataRoleGroupId "+
                        "and r.publicOrg is not null and r.publicOrg.id=:publicOrgId";
                Query query = entityManager.createQuery(sql);
                query.setParameter("tenantCode",user.getTenantCode());
                query.setParameter("publicUserType",user.getUserType());
                query.setParameter("dataRoleGroupId", group.getId());
                query.setParameter("publicOrgId", org.getId());
                List<DataRole> orgPubRoles = new ArrayList<>();
                for (Object r: query.getResultList()
                     ) {
                    orgPubRoles.add((DataRole) r);
                }
                if (!orgPubRoles.isEmpty()){
                    result.addAll(orgPubRoles);
                    break;
                }
            }
        }
        return result;
    }
}
