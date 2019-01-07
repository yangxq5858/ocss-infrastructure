package com.ecmp.basic.dao;

import com.ecmp.basic.entity.Feature;
import com.ecmp.basic.entity.FeatureRole;
import com.ecmp.basic.entity.FeatureRoleFeature;
import com.ecmp.core.dao.BaseRelationDao;
import org.springframework.stereotype.Repository;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：功能角色分配的功能项数据访问接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-04 13:24      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Repository
public interface FeatureRoleFeatureDao extends BaseRelationDao<FeatureRoleFeature,FeatureRole,Feature>,FeatureRoleFeatureExtDao {
}
