package com.ecmp.basic.service;

import com.ecmp.basic.api.IEmployeePositionService;
import com.ecmp.basic.dao.EmployeePositionDao;
import com.ecmp.basic.entity.Employee;
import com.ecmp.basic.entity.EmployeePosition;
import com.ecmp.basic.entity.Position;
import com.ecmp.context.ContextUtil;
import com.ecmp.core.dao.BaseRelationDao;
import com.ecmp.core.service.BaseRelationService;
import com.ecmp.vo.OperateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：企业员工分配岗位的业务逻辑实现
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/5/11 20:29      秦有宝                     新建
 * <p/>
 * *************************************************************************************************
 */
@Service
public class EmployeePositionService extends BaseRelationService<EmployeePosition, Employee, Position>
        implements IEmployeePositionService {

    @Autowired
    private EmployeePositionDao dao;

    @Override
    protected BaseRelationDao<EmployeePosition, Employee, Position> getDao() {
        return dao;
    }

    @Autowired
    private PositionService positionService;

    /**
     * 获取可以分配的子实体清单
     *
     * @return 子实体清单
     */
    @Override
    protected List<Position> getCanAssignedChildren(String parentId) {
        return positionService.findAll();
    }

    /**
     * 创建分配关系
     *
     * @param parentId 父实体Id
     * @param childIds 子实体Id清单
     * @return 操作结果
     */
    @Override
    public OperateResult insertRelations(String parentId, List<String> childIds) {
        if (parentId.equals(ContextUtil.getUserId())) {
            //00027 = 不能给当前用户分配岗位！
            return OperateResult.operationFailure("00027");
        }
        return super.insertRelations(parentId, childIds);
    }

    /**
     * 移除分配关系
     *
     * @param parentId 父实体Id
     * @param childIds 子实体Id清单
     * @return 操作结果
     */
    @Override
    public OperateResult removeRelations(String parentId, List<String> childIds) {
        if (parentId.equals(ContextUtil.getUserId())) {
            //00028 = 不能移除当前用户的岗位！
            return OperateResult.operationFailure("00028");
        }
        return super.removeRelations(parentId, childIds);
    }

    /**
     * 通过父实体清单创建分配关系
     *
     * @param childId   子实体Id
     * @param parentIds 父实体Id清单
     * @return 操作结果
     */
    @Override
    public OperateResult insertRelationsByParents(String childId, List<String> parentIds) {
        for (String employeeId : parentIds) {
            if (employeeId.equals(ContextUtil.getUserId())) {
                //00029 = 不能为岗位分配当前用户！
                return OperateResult.operationFailure("00029");
            }
        }
        return super.insertRelationsByParents(childId, parentIds);
    }

    /**
     * 通过父实体清单移除分配关系
     *
     * @param childId   子实体Id
     * @param parentIds 父实体Id清单
     * @return 操作结果
     */
    @Override
    public OperateResult removeRelationsByParents(String childId, List<String> parentIds) {
        for (String employeeId : parentIds) {
            if (employeeId.equals(ContextUtil.getUserId())) {
                //00030 = 不能给岗位移除当前用户！
                return OperateResult.operationFailure("00030");
            }
        }
        return super.removeRelationsByParents(childId, parentIds);
    }
}