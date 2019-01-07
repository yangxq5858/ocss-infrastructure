package com.ecmp.basic.service;

import com.ecmp.basic.api.IOrganizationService;
import com.ecmp.basic.dao.OrganizationDao;
import com.ecmp.basic.entity.Employee;
import com.ecmp.basic.entity.Organization;
import com.ecmp.basic.entity.vo.OrganizationDimension;
import com.ecmp.context.ContextUtil;
import com.ecmp.context.util.NumberGenerator;
import com.ecmp.core.dao.BaseTreeDao;
import com.ecmp.core.entity.BaseEntity;
import com.ecmp.core.service.BaseTreeService;
import com.ecmp.enums.UserAuthorityPolicy;
import com.ecmp.vo.OperateResult;
import com.ecmp.vo.OperateResultWithData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：实现组织机构的业务逻辑服务
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/19 15:49        秦有宝                      新建
 * <p/>
 * *************************************************************************************************
 */
@Service
public class OrganizationService extends BaseTreeService<Organization>
        implements IOrganizationService {
    private static final String CLEAR_OTHER_CACHES_KEY="CLEAR_ORG_OTHER_CACHES";
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private PositionService positionService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private FeatureRoleService featureRoleService;
    @Autowired
    private DataRoleService dataRoleService;

    @Override
    protected BaseTreeDao<Organization> getDao() {
        return organizationDao;
    }

    @Override
    @Transactional
    public OperateResultWithData<Organization> save(Organization entity) {
        if (StringUtils.isBlank(entity.getCode())) {
            entity.setCode(NumberGenerator.getNumber(Organization.class));
        }
        if (!entity.isNew()) {
            //冻结，级联冻结所有子节点
            if (entity.getFrozen()) {
                List<Organization> childrenNodes = organizationDao.getChildrenNodesNoneOwn(entity.getId());
                for (Organization organization : childrenNodes) {
                    organization.setFrozen(true);
                    organizationDao.save(organization);
                }
            } else {
                if (StringUtils.isNotBlank(entity.getParentId())) {
                    //解冻，判断父节点是否冻结
                    Organization parentNode = organizationDao.findOne(entity.getParentId());
                    if (parentNode.getFrozen()) {
                        //00047 = 禁止解冻，请先解冻【{0}】的所有父节点！
                        return OperateResultWithData.operationFailure("00047", entity.getName());
                    }
                }
            }
        }
        OperateResultWithData<Organization> result = super.save(entity);
        if (result.successful()){
            clearOrgOtherCaches();
        }
        return result;
    }

    @Override
    public Organization findOne(String s) {
        if(ContextUtil.getSessionUser().getAuthorityPolicy()== UserAuthorityPolicy.GlobalAdmin){
            Organization organization = findRootById(s);
            if(organization != null){
                return organization;
            }
        }
        return super.findOne(s);
    }

    /**
     * 通过获取组织机构根节点
     *
     * @param id 组织机构id
     * @return 组织机构
     */
    private Organization findRootById(String id) {
        return organizationDao.findByParentIdIsNullAndId(id);
    }

    @Override
    public OperateResult delete(String id) {
        OperateResult result = super.delete(id);
        if (result.successful()){
            clearOrgOtherCaches();
        }
        return result;
    }

    /**
     * 通过代码获取组织机构
     *
     * @param code 代码
     * @return 组织机构
     */
    @Override
    public Organization findByCode(String code) {
        return organizationDao.findByCodeAndTenantCode(code, ContextUtil.getTenantCode());
    }

    /**
     * 通过获取组织机构根节点
     *
     * @param tenantCode 租户代码
     * @return 组织机构
     */
    @Override
    public Organization findRootByTenantCode(String tenantCode) {
        return organizationDao.findByParentIdIsNullAndTenantCode(tenantCode);
    }

    /**
     * 获取组织机构树(不包含冻结)
     *
     * @return 组织机构树清单
     */
    @Override
    public List<Organization> findOrgTreeWithoutFrozen() {
        List<Organization> orgList = organizationDao.findAllUnfrozen();
        return buildTree(orgList);
    }

    /**
     * 通过获取组织机构树
     *
     * @return 组织机构清单
     */
    @Override
    public Organization findOrgTree() {
        String tenantCode = ContextUtil.getTenantCode();

        List<Organization> orgRoots = getAllRootNode();
        Optional<Organization> optional = orgRoots.stream().filter(tempRoot -> StringUtils.equals(tenantCode, tempRoot.getTenantCode())).findFirst();
        if (optional.isPresent()) {
            //获取树根节点
            Organization treeRoot = optional.get();
            return getTree(treeRoot.getId());
        }
        return null;
    }

    /**
     * 获取所有组织机构树
     *
     * @return 所有组织机构树
     */
    @Override
    public List<Organization> findAllOrgs() {
        List<Organization> orgTree = new ArrayList<>();
        List<Organization> orgRoots = getAllRootNode();
        for (Organization orgRoot : orgRoots) {
            orgTree.add(getTree(orgRoot.getId()));
        }
        return orgTree;
    }

    /**
     * 根据指定的节点id获取树
     *
     * @param nodeId 节点ID
     * @return 返回已指定节点ID为根的树
     */
    @Override
    public Organization getTree4Unfrozen(String nodeId) {
        return StringUtils.isNotBlank(nodeId) ? organizationDao.getTree4Unfrozen(nodeId) : null;
    }

    /**
     * 通过组织机构id获取组织机构清单
     *
     * @param nodeId 组织机构id
     * @return 组织机构清单（非树形）
     */
    @Override
    public List<Organization> getChildrenNodes4Unfrozen(String nodeId) {
        return organizationDao.getChildrenNodes4Unfrozen(nodeId);
    }

    /**
     * 删除数据保存数据之前额外操作回调方法 子类根据需要覆写添加逻辑即可
     *
     * @param s 待删除数据对象主键
     */
    @Override
    protected OperateResult preDelete(String s) {
        if (positionService.isExistsByProperty("organization.id", s)) {
            //00042 = 该组织机构下存在岗位，禁止删除！
            return OperateResult.operationFailure("00042");
        }
        if (employeeService.isExistsByProperty("organization.id", s)) {
            //00043 = 该组织机构下存在企业员工，禁止删除！
            return OperateResult.operationFailure("00043");
        }
        if (featureRoleService.isExistsByProperty("publicOrg.id", s)) {
            //00044 = 该组织机构下存在功能角色，禁止删除！
            return OperateResult.operationFailure("00044");
        }
        if (dataRoleService.isExistsByProperty("publicOrg.id", s)) {
            //00045 = 该组织机构下存在数据角色，禁止删除！
            return OperateResult.operationFailure("00045");
        }
        return super.preDelete(s);
    }

    /**
     * 获取一般用户有权限的业务实体Id清单
     *
     * @param featureCode 功能项代码
     * @param userId      用户Id
     * @return 业务实体Id清单
     */
    @Override
    protected List<String> getNormalUserAuthorizedEntityIds(String featureCode, String userId) {
        Set<String> authorizedEntityIds = new HashSet<>();
        List<String> entityIds = super.getNormalUserAuthorizedEntityIds(featureCode, userId);
        if (Objects.nonNull(entityIds) && !entityIds.isEmpty()){
            authorizedEntityIds.addAll(entityIds);
        }
        // 获取企业用户的组织机构
        Employee employee = employeeService.findOne(userId);
        if (Objects.nonNull(employee) && Objects.nonNull(employee.getOrganization())){
            // 获取所有子节点清单
            List<Organization> children = organizationDao.getChildrenNodes4Unfrozen(employee.getOrganization().getId());
            List<String> childIds = children.stream().map(BaseEntity::getId).collect(Collectors.toList());
            authorizedEntityIds.addAll(childIds);
        }
        return new ArrayList<>(authorizedEntityIds);
    }

    /**
     * 清除其他应用的组织机构缓存
     */
    public void clearOrgOtherCaches(){
        Properties clearConfig = ContextUtil.getGlobalProperties(CLEAR_OTHER_CACHES_KEY);
        if (Objects.isNull(clearConfig) || clearConfig.isEmpty()){
            return;
        }
        clearConfig.forEach((k,v) -> {
            String pattern = v.toString();
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys!=null&&!keys.isEmpty()){
                redisTemplate.delete(keys);
            }
        });
    }

    /**
     * 获取组织机构维度清单
     *
     * @return 组织机构维度清单
     */
    @Override
    public List<OrganizationDimension> findOrganizationDimension() {
        List<OrganizationDimension> organizationDimensions = new ArrayList<>();
        OrganizationDimension organizationDimension = new OrganizationDimension();
        organizationDimension.setId("0");
        organizationDimension.setName("业务部门");
        organizationDimensions.add(organizationDimension);
        return organizationDimensions;
    }
}
