package com.ecmp.basic.dao;

import com.ecmp.basic.entity.Employee;
import com.ecmp.core.dao.BaseEntityDao;
import com.ecmp.enums.UserAuthorityPolicy;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：企业员工数据访问接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/5/5 13:33      秦有宝                     新建
 * <p/>
 * *************************************************************************************************
 */
@Repository
public interface EmployeeDao extends BaseEntityDao<Employee>, EmployeeExtDao {

     /**
      * 根据组织机构的id获取员工
      *
      * @param organizationId 组织机构的id
      * @return 员工清单
      */
     List<Employee> findByOrganizationId(String organizationId);

     /**
      * 根据组织机构的id获取员工(不包含冻结)
      *
      * @param organizationId 组织机构的id
      * @return 员工清单
      */
     List<Employee> findByOrganizationIdAndUserFrozenFalse(String organizationId);

     /**
      * 根据租户代码与用户权限策略获取员工
      *
      * @param tenantCode 组织机构的id
      * @param userAuthorityPolicy 用户权限策略
      * @return 员工清单
      */
     List<Employee> findByTenantCodeAndUserUserAuthorityPolicy(String tenantCode, UserAuthorityPolicy userAuthorityPolicy);

     /**
      * 通过员工编号和租户代码获取员工
      *
      * @param code 员工编号
      * @param tenantCode 租户代码
      * @return 员工
      */
     Employee findByCodeAndTenantCode(String code, String tenantCode);
}
