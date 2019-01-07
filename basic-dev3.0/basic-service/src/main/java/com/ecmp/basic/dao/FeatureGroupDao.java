package com.ecmp.basic.dao;

import com.ecmp.basic.entity.FeatureGroup;
import com.ecmp.core.dao.BaseEntityDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：
 * 应用模块Entity定义
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00     2017/4/19  16:50    余思豆 (yusidou)                 新建
 * <p/>
 * *************************************************************************************************
 */
@Repository
public interface FeatureGroupDao extends BaseEntityDao<FeatureGroup> {
    /**
     * 根据名称模糊查询功能项
     *
     * @param name 名称
     * @return 功能项组清单e
     */
    List<FeatureGroup> findByNameLike(String name);
}
