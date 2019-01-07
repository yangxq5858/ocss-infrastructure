package com.ecmp.basic.service;

import com.ecmp.basic.api.IDataRoleService;
import com.ecmp.basic.dao.DataRoleDao;
import com.ecmp.basic.entity.*;
import com.ecmp.enums.UserType;
import com.ecmp.core.dao.BaseEntityDao;
import com.ecmp.core.search.SearchFilter;
import com.ecmp.core.service.BaseEntityService;
import com.ecmp.vo.OperateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：数据角色业务逻辑实现
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-04 16:35      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Service
public class DataRoleService extends BaseEntityService<DataRole>
        implements IDataRoleService {

    @Autowired
    private DataRoleDao dao;
    @Override
    protected BaseEntityDao<DataRole> getDao() {
        return dao;
    }
    @Autowired
    private DataRoleAuthTypeValueService dataRoleAuthTypeValueService;
    @Autowired
    private PositionDataRoleService positionDataRoleService;
    @Autowired
    private UserDataRoleService userDataRoleService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private OrganizationService organizationService;
    /**
     * 通过角色组Id获取角色清单
     *
     * @param roleGroupId 角色组Id
     * @return 角色清单
     */
    @Override
    @Transactional(readOnly = true)
    public List<DataRole> findByDataRoleGroup(String roleGroupId) {
        SearchFilter filter = new SearchFilter("dataRoleGroup.id",roleGroupId, SearchFilter.Operator.EQ);
        return findByFilter(filter);
    }

    /**
     * 删除数据保存数据之前额外操作回调方法 子类根据需要覆写添加逻辑即可
     *
     * @param s 待删除数据对象主键
     */
    @Override
    protected OperateResult preDelete(String s) {
        if (dataRoleAuthTypeValueService.isExistsByProperty("dataRole.id",s)){
            //数据角色存在数据角色分配权限类型的值，禁止删除！
            return OperateResult.operationFailure("00022");
        }
        if (positionDataRoleService.isExistsByProperty("child.id",s)){
            //数据角色存在已分配的岗位，禁止删除！
            return OperateResult.operationFailure("00023");
        }
        if (userDataRoleService.isExistsByProperty("child.id",s)){
            //数据角色存在已分配的用户，禁止删除！
            return OperateResult.operationFailure("00024");
        }
        return super.preDelete(s);
    }

    /**
     * 获取用户的公共功能角色
     * @param user 用户
     * @return 公共功能角色清单
     */
    @Transactional(readOnly = true)
    List<DataRole> getPublicDataRoles(User user){
        List<DataRole> result = new ArrayList<>();
        //获取用户类型匹配的全局公共角色
        List<DataRole> publicRoles = dao.getPublicRoles(user);
        result.addAll(publicRoles);
        //获取用户的组织机构
        if (user.getUserType()== UserType.Employee){
            Employee employee =employeeService.findOne(user.getId());
            if (employee!=null){
                //获取企业用户的组织机构
                List<Organization> orgs = organizationService.getParentNodes(employee.getOrganization().getId(),true);
                List<DataRole> orgPubRoles = dao.getPublicRoles(user,orgs);
                result.addAll(orgPubRoles);
            }
        }
        return result;
    }
}
