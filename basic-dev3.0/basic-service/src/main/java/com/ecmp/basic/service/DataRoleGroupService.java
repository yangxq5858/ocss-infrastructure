package com.ecmp.basic.service;

import com.ecmp.basic.api.IDataRoleGroupService;
import com.ecmp.basic.dao.DataRoleGroupDao;
import com.ecmp.basic.entity.DataRoleGroup;
import com.ecmp.core.dao.BaseEntityDao;
import com.ecmp.core.service.BaseEntityService;
import com.ecmp.vo.OperateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：数据角色组业务逻辑实现
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-04 14:04      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Service
public class DataRoleGroupService extends BaseEntityService<DataRoleGroup>
        implements IDataRoleGroupService {
    @Autowired
    private DataRoleGroupDao dao;
    @Override
    protected BaseEntityDao<DataRoleGroup> getDao() {
        return dao;
    }
    @Autowired
    private DataRoleService dataRoleService;

    /**
     * 删除数据保存数据之前额外操作回调方法 子类根据需要覆写添加逻辑即可
     *
     * @param s 待删除数据对象主键
     */
    @Override
    protected OperateResult preDelete(String s) {
        if (dataRoleService.isExistsByProperty("dataRoleGroup.id",s)){
            //数据角色组存在数据角色，禁止删除！
            return OperateResult.operationFailure("00021");
        }
        return super.preDelete(s);
    }
}
