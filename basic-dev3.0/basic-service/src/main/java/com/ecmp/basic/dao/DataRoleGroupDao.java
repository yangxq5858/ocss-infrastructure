package com.ecmp.basic.dao;

import com.ecmp.basic.entity.DataRoleGroup;
import com.ecmp.core.dao.BaseEntityDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：数据角色组数据访问接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-04 13:15      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Repository
public interface DataRoleGroupDao extends BaseEntityDao<DataRoleGroup> {

    /**
     * 获取租户的数据角色组
     * @param tenantCode 租户代码
     * @return 数据角色组清单
     */
    List<DataRoleGroup> findByTenantCode(String tenantCode);
}
