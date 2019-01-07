package com.ecmp.basic.service;

import com.ecmp.basic.api.IUserDataRoleService;
import com.ecmp.basic.dao.UserDataRoleDao;
import com.ecmp.basic.entity.DataRole;
import com.ecmp.basic.entity.User;
import com.ecmp.basic.entity.UserDataRole;
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
 * 实现功能：用户分配的数据角色的业务逻辑实现
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/5/16 10:20      秦有宝                     新建
 * <p/>
 * *************************************************************************************************
 */
@Service
public class UserDataRoleService extends BaseRelationService<UserDataRole, User, DataRole>
        implements IUserDataRoleService {
    @Autowired
    private UserDataRoleDao dao;

    @Override
    protected BaseRelationDao<UserDataRole, User, DataRole> getDao() {
        return dao;
    }

    @Autowired
    private DataRoleService dataRoleService;

    /**
     * 获取可以分配的子实体清单
     * @return 子实体清单
     */
    @Override
    protected List<DataRole> getCanAssignedChildren(String parentId){
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
        if(parentId.equals(ContextUtil.getUserId())){
            //00033 = 不能为当前用户分配数据角色！
            return OperateResult.operationFailure("00033");
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
        if(parentId.equals(ContextUtil.getUserId())){
            //00034= 不能移除当前用户的数据角色！
            return OperateResult.operationFailure("00034");
        }
        return super.removeRelations(parentId, childIds);
    }
}
