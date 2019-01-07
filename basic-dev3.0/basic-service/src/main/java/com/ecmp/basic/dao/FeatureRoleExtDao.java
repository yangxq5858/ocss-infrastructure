package com.ecmp.basic.dao;

import com.ecmp.basic.entity.FeatureRole;
import com.ecmp.basic.entity.Organization;
import com.ecmp.basic.entity.User;

import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：功能角色数据访问扩展接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-17 16:47      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public interface FeatureRoleExtDao {
    /**
     * 获取用户的全局公共角色
     * @param user 用户
     * @return 全局公共角色
     */
    List<FeatureRole> getPublicRoles(User user);

    /**
     * 获取用户组织机构的公共角色
     * @param user 用户
     * @param orgs 组织机构父节点清单
     * @return 组织机构公共角色
     */
    List<FeatureRole> getPublicRoles(User user, List<Organization> orgs);
}
