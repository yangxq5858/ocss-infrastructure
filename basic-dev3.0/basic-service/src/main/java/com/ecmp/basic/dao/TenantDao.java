package com.ecmp.basic.dao;

import com.ecmp.basic.entity.Tenant;
import com.ecmp.core.dao.BaseEntityDao;
import org.springframework.stereotype.Repository;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：租户数据访问接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间                  变更人                 变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/14 15:45            高银军                  新建
 * <p/>
 * *************************************************************************************************
 */
@Repository
public interface TenantDao extends BaseEntityDao<Tenant> {
    /**
     * 查询未被冻结的租户
     *
     * @param tenantCode 租户代码
     * @return 返回null，则被冻结；反之返回指定代码的租户
     */
    Tenant findByFrozenFalseAndCode(String tenantCode);
}
