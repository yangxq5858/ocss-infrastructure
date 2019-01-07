package com.ecmp.basic.service;

import com.ecmp.basic.api.IFeatureRoleService;
import com.ecmp.basic.dao.FeatureRoleDao;
import com.ecmp.basic.entity.*;
import com.ecmp.enums.UserType;
import com.ecmp.core.dao.BaseEntityDao;
import com.ecmp.core.search.SearchFilter;
import com.ecmp.core.service.BaseEntityService;
import com.ecmp.vo.OperateResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：功能角色业务逻辑实现
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-04 16:35      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Service
public class FeatureRoleService extends BaseEntityService<FeatureRole>
        implements IFeatureRoleService{

    @Autowired
    private FeatureRoleDao dao;
    @Override
    protected BaseEntityDao<FeatureRole> getDao() {
        return dao;
    }
    @Autowired
    private UserFeatureRoleService userFeatureRoleService;
    @Autowired
    private PositionFeatureRoleService positionFeatureRoleService;
    @Autowired
    private FeatureRoleFeatureService featureRoleFeatureService;
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
    public List<FeatureRole> findByFeatureRoleGroup(String roleGroupId) {
        SearchFilter filter = new SearchFilter("featureRoleGroup.id",roleGroupId, SearchFilter.Operator.EQ);
        return findByFilter(filter);
    }

    /**
     * 删除数据保存数据之前额外操作回调方法 子类根据需要覆写添加逻辑即可
     *
     * @param s 待删除数据对象主键
     */
    @Override
    protected OperateResult preDelete(String s) {
        if (featureRoleFeatureService.isExistByParent(s)){
            //功能角色存在已经分配的功能项，禁止删除！
            return OperateResult.operationFailure("00009");
        }
        return super.preDelete(s);
    }

    /**
     * 根据功能角色的id获取已分配的用户
     *
     * @param featureRoleId 功能角色的id
     * @return 员工用户清单
     */
    @Override
    public List<User> getAssignedEmployeesByFeatureRole(String featureRoleId) {
        return userFeatureRoleService.getParentsFromChildId(featureRoleId);
    }

    /**
     * 根据功能角色的id获取已分配的岗位
     *
     * @param featureRoleId 功能角色的id
     * @return 岗位清单
     */
    @Override
    public List<Position> getAssignedPositionsByFeatureRole(String featureRoleId) {
        return positionFeatureRoleService.getParentsFromChildId(featureRoleId);
    }

    /**
     * 根据代码查询实体
     *
     * @param code 代码
     * @return 实体
     */
    @Override
    public FeatureRole findByCode(String code) {
        if (StringUtils.isNotBlank(code)) {
            return super.findByProperty(FeatureRole.CODE_FIELD, code);
        }
        return null;
    }

    /**
     * 获取用户的公共功能角色
     * @param user 用户
     * @return 公共功能角色清单
     */
    List<FeatureRole> getPublicFeatureRoles(User user){
        List<FeatureRole> result = new ArrayList<>();
        //获取用户类型匹配的全局公共角色
        List<FeatureRole> publicRoles = dao.getPublicRoles(user);
        result.addAll(publicRoles);
        //获取用户的组织机构
        if (user.getUserType()== UserType.Employee){
            Employee employee =employeeService.findOne(user.getId());
            if (employee!=null){
                //获取企业用户的组织机构
                List<Organization> orgs = organizationService.getParentNodes(employee.getOrganization().getId(),true);
                List<FeatureRole> orgPubRoles = dao.getPublicRoles(user,orgs);
                result.addAll(orgPubRoles);
            }
        }
        return result;
    }
}
