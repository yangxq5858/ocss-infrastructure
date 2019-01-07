package com.ecmp.basic.service;

import com.ecmp.basic.api.IFeatureRoleGroupService;
import com.ecmp.basic.dao.FeatureRoleGroupDao;
import com.ecmp.basic.entity.FeatureRoleGroup;
import com.ecmp.core.dao.BaseEntityDao;
import com.ecmp.core.service.BaseEntityService;
import com.ecmp.vo.OperateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：功能角色组业务逻辑实现
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-04 14:04      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Service
public class FeatureRoleGroupService extends BaseEntityService<FeatureRoleGroup>
        implements IFeatureRoleGroupService{
    @Autowired
    private FeatureRoleGroupDao dao;
    @Override
    protected BaseEntityDao<FeatureRoleGroup> getDao() {
        return dao;
    }

    @Autowired
    private FeatureRoleService featureRoleService;
    /**
     * 删除数据保存数据之前额外操作回调方法 子类根据需要覆写添加逻辑即可
     *
     * @param s 待删除数据对象主键
     */
    @Override
    protected OperateResult preDelete(String s) {
        //检查是否存在角色
        if (featureRoleService.isExistsByProperty("featureRoleGroup.id",s)){
            //角色组存在功能角色，禁止删除！
            return OperateResult.operationFailure("00008");
        }
        return super.preDelete(s);
    }
}
