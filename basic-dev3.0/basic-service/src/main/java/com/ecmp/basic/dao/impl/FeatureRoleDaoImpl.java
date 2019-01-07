package com.ecmp.basic.dao.impl;

import com.ecmp.basic.dao.*;
import com.ecmp.basic.entity.*;
import com.ecmp.basic.entity.enums.RoleType;
import com.ecmp.core.dao.impl.BaseEntityDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：功能角色数据访问扩展实现
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-17 16:54      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class FeatureRoleDaoImpl extends BaseEntityDaoImpl<FeatureRole> implements FeatureRoleExtDao {
    //构造函数
    public FeatureRoleDaoImpl(EntityManager entityManager) {
        super(FeatureRole.class, entityManager);
    }

    @Autowired
    private FeatureRoleGroupDao featureRoleGroupDao;
    /**
     * 获取用户的全局公共角色
     *
     * @param user 用户
     * @return 全局公共角色
     */
    @Override
    @Transactional(readOnly = true)
    public List<FeatureRole> getPublicRoles(User user) {
        if (Objects.isNull(user)){
            return new ArrayList<>();
        }
        String sql = "select r from FeatureRole r where r.tenantCode=:tenantCode and r.roleType=:roleType and r.publicUserType=:publicUserType and r.publicOrg is null";
        Query query = entityManager.createQuery(sql);
        query.setParameter("tenantCode",user.getTenantCode());
        query.setParameter("roleType", RoleType.CanUse);
        query.setParameter("publicUserType",user.getUserType());
        List<FeatureRole> result = new ArrayList<>();
        for (Object r: query.getResultList()
             ) {
            result.add((FeatureRole)r);
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
    public List<FeatureRole> getPublicRoles(User user, List<Organization> orgs) {
        List<FeatureRole> result = new ArrayList<>();
        if (Objects.isNull(user)||Objects.isNull(orgs)||orgs.isEmpty()){
            return result;
        }
        //获取租户的功能角色组
        List<FeatureRoleGroup> groups = featureRoleGroupDao.findByTenantCode(user.getTenantCode());
        for (FeatureRoleGroup group:groups){
            //循环组织机构
            for (Organization org:orgs){
                //获取匹配的公共角色
                String sql = "select r from FeatureRole r where r.tenantCode=:tenantCode " +
                        "and r.roleType=:roleType " +
                        "and r.publicUserType=:publicUserType " +
                        "and r.featureRoleGroup.id=:featureRoleGroupId "+
                        "and r.publicOrg is not null and r.publicOrg.id=:publicOrgId";
                Query query = entityManager.createQuery(sql);
                query.setParameter("tenantCode",user.getTenantCode());
                query.setParameter("roleType", RoleType.CanUse);
                query.setParameter("publicUserType",user.getUserType());
                query.setParameter("featureRoleGroupId", group.getId());
                query.setParameter("publicOrgId", org.getId());
                List<FeatureRole> orgPubRoles = new ArrayList<>();
                for (Object r:query.getResultList()
                     ) {
                    orgPubRoles.add((FeatureRole)r);
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
