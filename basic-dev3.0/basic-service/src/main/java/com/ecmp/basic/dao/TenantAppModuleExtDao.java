package com.ecmp.basic.dao;

import com.ecmp.basic.entity.AppModule;

import java.util.List;

/**
 * <strong>实现功能:</strong>
 * <p>租户可用的应用模块数据访问扩展接口</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2017-09-05 16:22
 */
public interface TenantAppModuleExtDao {
    /**
     * 通过租户代码获取应用模块
     *
     * @param tenantCode 租户代码
     * @return 应用模块清单
     */
    List<AppModule> getAppModuleByTenantCode(String tenantCode);
}
