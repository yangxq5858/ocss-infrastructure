package com.ecmp.basic.dao;

import com.ecmp.basic.entity.Employee;
import com.ecmp.basic.entity.vo.EmployeeQueryParam;
import com.ecmp.core.search.PageResult;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：企业员工扩展接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/5/26 13:59      秦有宝                     新建
 * <p/>
 * *************************************************************************************************
 */
public interface EmployeeExtDao {

    /**
     * 保存企业员工用户
     *
     * @param entity 企业员工用户实体
     * @param isNew 是否是创建
     * @return 保存结果
     */
    Employee save(Employee entity, boolean isNew);

    /**
     * 根据查询参数获取企业员工(分页)
     *
     * @param employeeQueryParam 查询参数
     * @return 企业员工
     */
    PageResult<Employee> findByEmployeeParam(EmployeeQueryParam employeeQueryParam);

    /**
     * 检查员工编号是否存在
     * @param code 员工编号
     * @param id 实体id
     * @return 是否存在
     */
    Boolean isCodeExist(String code, String id);
}
