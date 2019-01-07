package com.ecmp.basic.dao;

import com.ecmp.basic.entity.FeatureRoleGroup;
import com.ecmp.core.dao.BaseEntityDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：功能角色组数据访问接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-04 13:15      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Repository
public interface FeatureRoleGroupDao extends BaseEntityDao<FeatureRoleGroup> {

    /**
     * 获取租户的功能角色组
     * @param tenantCode 租户代码
     * @return 功能角色组清单
     */
    List<FeatureRoleGroup> findByTenantCode(String tenantCode);
}
