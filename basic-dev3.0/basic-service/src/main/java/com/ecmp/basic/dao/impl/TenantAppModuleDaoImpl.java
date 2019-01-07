package com.ecmp.basic.dao.impl;

import com.ecmp.basic.dao.TenantAppModuleExtDao;
import com.ecmp.basic.entity.AppModule;
import com.ecmp.basic.entity.TenantAppModule;
import com.ecmp.core.dao.impl.BaseEntityDaoImpl;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * <strong>实现功能:</strong>
 * <p>租户可用的应用模块数据访问扩展接口实现</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2017-09-05 16:26
 */
public class TenantAppModuleDaoImpl extends BaseEntityDaoImpl<TenantAppModule> implements TenantAppModuleExtDao {

    //构造函数
    public TenantAppModuleDaoImpl(EntityManager entityManager) {
        super(TenantAppModule.class, entityManager);
    }

    /**
     * 通过租户代码获取应用模块
     *
     * @param tenantCode 租户代码
     * @return 应用模块清单
     */
    @Override
    public List<AppModule> getAppModuleByTenantCode(String tenantCode) {
        String sql = "select r.child from TenantAppModule r where r.parent.code = :code";
        Query query = entityManager.createQuery(sql);
        query.setParameter("code", tenantCode);
        List<AppModule> appModules = new ArrayList<>();
        for (Object appModule : query.getResultList()
                ) {
            appModules.add((AppModule) appModule);
        }
        return appModules;
    }
}
