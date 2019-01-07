package com.ecmp.basic.service;

import com.ecmp.basic.api.IUserFeatureRoleService;
import com.ecmp.basic.dao.UserFeatureRoleDao;
import com.ecmp.basic.entity.FeatureRole;
import com.ecmp.basic.entity.User;
import com.ecmp.basic.entity.UserFeatureRole;
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
 * 实现功能：用户分配的功能角色的业务逻辑实现
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/5/16 10:20      秦有宝                     新建
 * <p/>
 * *************************************************************************************************
 */
@Service
public class UserFeatureRoleService extends BaseRelationService<UserFeatureRole, User, FeatureRole>
        implements IUserFeatureRoleService {
    @Autowired
    private UserFeatureRoleDao dao;

    @Override
    protected BaseRelationDao<UserFeatureRole, User, FeatureRole> getDao() {
        return dao;
    }

    @Autowired
    private FeatureRoleService featureRoleService;

    /**
     * 获取可以分配的子实体清单
     *
     * @return 子实体清单
     */
    @Override
    protected List<FeatureRole> getCanAssignedChildren(String parentId) {
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
        if (parentId.equals(ContextUtil.getUserId())) {
            //00031 = 不能为当前用户分配功能角色！
            return OperateResult.operationFailure("00031");
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
            //00032 = 不能移除当前用户的功能角色！
            return OperateResult.operationFailure("00032");
        }
        return super.removeRelations(parentId, childIds);
    }
}
