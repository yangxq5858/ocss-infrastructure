package com.ecmp.basic.dao;

import com.ecmp.basic.entity.Organization;
import com.ecmp.core.dao.BaseTreeDao;
import org.springframework.stereotype.Repository;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：组织机构数据访问接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/19 15:39        秦有宝                      新建
 * <p/>
 * *************************************************************************************************
 */
@Repository
public interface OrganizationDao extends BaseTreeDao<Organization> {

    Organization findByCodeAndTenantCode(String code, String tenantCode);

    Organization findByParentIdIsNullAndTenantCode(String tenantCode);

    Organization findByParentIdIsNullAndId(String id);
}
