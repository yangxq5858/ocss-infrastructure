package com.ecmp.basic.service;

import com.ecmp.basic.api.IPositionFeatureRoleService;
import com.ecmp.basic.dao.PositionFeatureRoleDao;
import com.ecmp.basic.entity.*;
import com.ecmp.context.ContextUtil;
import com.ecmp.core.dao.BaseRelationDao;
import com.ecmp.core.service.BaseRelationService;
import com.ecmp.vo.OperateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：岗位分配的功能角色的业务逻辑实现
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/5/16 10:04      秦有宝                     新建
 * <p/>
 * *************************************************************************************************
 */
@Service
public class PositionFeatureRoleService extends BaseRelationService<PositionFeatureRole, Position, FeatureRole>
        implements IPositionFeatureRoleService {

    @Autowired
    private PositionFeatureRoleDao dao;
    @Autowired
    private EmployeePositionService employeePositionService;

    @Override
    protected BaseRelationDao<PositionFeatureRole, Position, FeatureRole> getDao() {
        return dao;
    }

    @Autowired
    private FeatureRoleService featureRoleService;

    /**
     * 获取可以分配的子实体清单
     * @return 子实体清单
     */
    @Override
    protected List<FeatureRole> getCanAssignedChildren(String parentId){
        return featureRoleService.findAll();
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
        EmployeePosition employeePosition = employeePositionService.getRelation(ContextUtil.getUserId(),parentId);
        if(Objects.nonNull(employeePosition)){
            //00025 = 不能给当前用户的岗位分配功能角色！
            return OperateResult.operationFailure("00025");
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
        EmployeePosition employeePosition = employeePositionService.getRelation(ContextUtil.getUserId(),parentId);
        if(Objects.nonNull(employeePosition)){
            //00026 = 不能给当前用户的岗位移除功能角色！
            return OperateResult.operationFailure("00026");
        }
        return super.removeRelations(parentId, childIds);
    }
}
