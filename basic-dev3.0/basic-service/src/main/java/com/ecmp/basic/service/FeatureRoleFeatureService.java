package com.ecmp.basic.service;

import com.ecmp.basic.api.IFeatureRoleFeatureService;
import com.ecmp.basic.dao.FeatureRoleFeatureDao;
import com.ecmp.basic.entity.Feature;
import com.ecmp.basic.entity.FeatureRole;
import com.ecmp.basic.entity.FeatureRoleFeature;
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
 * 实现功能：功能角色分配的功能项的业务逻辑实现
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-05 9:32      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Service
public class FeatureRoleFeatureService extends BaseRelationService<FeatureRoleFeature,FeatureRole,Feature>
        implements IFeatureRoleFeatureService{

    @Autowired
    private FeatureRoleFeatureDao dao;
    @Override
    protected BaseRelationDao<FeatureRoleFeature,FeatureRole,Feature> getDao() {
        return dao;
    }

    @Autowired
    private UserService userService;
    @Autowired
    private UserFeatureRoleService userFeatureRoleService;
    /**
     * 获取可以分配的子实体清单
     * @return 子实体清单
     */
    @Override
    protected List<Feature> getCanAssignedChildren(String parentId){
        String userId = ContextUtil.getUserId();
        return userService.getUserCanAssignFeatures(userId);
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
        UserFeatureRole userFeatureRole = userFeatureRoleService.getRelation(ContextUtil.getUserId(),parentId);
        if(userFeatureRole!=null){
            //00037 = 不能给当前用户的功能角色分配功能项！
            return OperateResult.operationFailure("00037");
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
        UserFeatureRole userFeatureRole = userFeatureRoleService.getRelation(ContextUtil.getUserId(),parentId);
        if(userFeatureRole!=null){
            //00038 = 不能给当前用户的功能角色移除功能项！
            return OperateResult.operationFailure("00038");
        }
        return super.removeRelations(parentId, childIds);
    }
}
