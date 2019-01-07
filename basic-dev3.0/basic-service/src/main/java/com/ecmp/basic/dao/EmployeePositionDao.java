package com.ecmp.basic.dao;

import com.ecmp.basic.entity.Employee;
import com.ecmp.basic.entity.EmployeePosition;
import com.ecmp.basic.entity.Position;
import com.ecmp.core.dao.BaseRelationDao;
import org.springframework.stereotype.Repository;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：企业员工用户分配岗位数据访问接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/5/11 20:16      秦有宝                     新建
 * <p/>
 * *************************************************************************************************
 */
@Repository
public interface EmployeePositionDao extends BaseRelationDao<EmployeePosition,Employee,Position> {
}
