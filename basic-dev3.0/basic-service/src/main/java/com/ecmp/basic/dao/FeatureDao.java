package com.ecmp.basic.dao;

import com.ecmp.basic.entity.Feature;
import com.ecmp.core.dao.BaseEntityDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：实现功能项数据访问接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间                  变更人                 变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/19 16:42             李汶强                  新建
 * <p/>
 * *************************************************************************************************
 */
@Repository
public interface FeatureDao extends BaseEntityDao<Feature> {
    /**
     * 通过代码获取功能项
     * @param code 功能项代码
     * @return 功能项
     */
    Feature findByCode(String code);

    /**
     * 获取租户可用的功能项
     * @param tenantId 租户Id
     * @return 租户可用的功能项清单
     */
    @Query("select f from Feature f inner join FeatureGroup g on f.featureGroup.id=g.id " +
            "inner join AppModule a on g.appModule.id=a.id " +
            "inner join TenantAppModule t on a.id=t.child.id " +
            "where t.parent.id=?1 and f.tenantCanUse=1 ")
    List<Feature> getTenantCanUseFeatures(String tenantId);
}
