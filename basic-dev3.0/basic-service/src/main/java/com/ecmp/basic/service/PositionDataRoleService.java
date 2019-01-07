package com.ecmp.basic.service;

import com.ecmp.basic.api.IPositionDataRoleService;
import com.ecmp.basic.dao.PositionDataRoleDao;
import com.ecmp.basic.entity.DataRole;
import com.ecmp.basic.entity.EmployeePosition;
import com.ecmp.basic.entity.Position;
import com.ecmp.basic.entity.PositionDataRole;
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
 * 实现功能：岗位分配的数据角色的业务逻辑实现
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/5/16 10:04      秦有宝                     新建
 * <p/>
 * *************************************************************************************************
 */
@Service
public class PositionDataRoleService extends BaseRelationService<PositionDataRole, Position, DataRole>
        implements IPositionDataRoleService {

    @Autowired
    private PositionDataRoleDao dao;
    @Autowired
    private EmployeePositionService employeePositionService;

    @Override
    protected BaseRelationDao<PositionDataRole, Position, DataRole> getDao() {
        return dao;
    }

    @Autowired
    private DataRoleService dataRoleService;

    /**
     * 获取可以分配的子实体清单
     *
     * @return 子实体清单
     */
    @Override
    protected List<DataRole> getCanAssignedChildren(String parentId) {
        return dataRoleService.findAll();
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
        EmployeePosition employeePosition = employeePositionService.getRelation(ContextUtil.getUserId(), parentId);
        if (Objects.nonNull(employeePosition)) {
            //00035 = 不能给当前用户的岗位分配数据角色！
            return OperateResult.operationFailure("00035");
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
        EmployeePosition employeePosition = employeePositionService.getRelation(ContextUtil.getUserId(), parentId);
        if (Objects.nonNull(employeePosition)) {
            //00036 = 不能给当前用户的岗位移除数据角色！
            return OperateResult.operationFailure("00036");
        }
        return super.removeRelations(parentId, childIds);
    }
}
