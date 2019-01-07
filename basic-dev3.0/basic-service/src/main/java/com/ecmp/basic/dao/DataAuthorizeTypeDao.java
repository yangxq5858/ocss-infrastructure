package com.ecmp.basic.dao;

import com.ecmp.basic.entity.DataAuthorizeType;
import com.ecmp.core.dao.BaseEntityDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：数据权限类型数据访问定义
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00     2017/4/19  16:50    王锦光(wangj)                 新建
 * <p/>
 * *************************************************************************************************
 */
@Repository
public interface DataAuthorizeTypeDao extends BaseEntityDao<DataAuthorizeType> {
    /**
     * 根据应用模块Id获取数据权限类型
     *
     * @param appModuleId 应用模块Id
     *
     * @return 数据权限类型清单
     */
    List<DataAuthorizeType> findByAuthorizeEntityTypeAppModuleId(String appModuleId);

    /**
     * 根据应用模块Id获取数据权限类型
     *
     * @param appModuleIds 应用模块Id清单
     *
     * @return 数据权限类型清单
     */
    List<DataAuthorizeType> findByAuthorizeEntityTypeAppModuleIdIn(List<String> appModuleIds);
}
