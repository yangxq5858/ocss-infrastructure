package com.ecmp.basic.service;

import com.ecmp.basic.api.IEmployeeService;
import com.ecmp.basic.dao.EmployeeDao;
import com.ecmp.basic.dao.OrganizationDao;
import com.ecmp.basic.entity.*;
import com.ecmp.basic.entity.vo.EmployeeQueryParam;
import com.ecmp.basic.entity.vo.Executor;
import com.ecmp.basic.entity.vo.UserQueryParam;
import com.ecmp.basic.service.util.EmailUtil;
import com.ecmp.context.ContextUtil;
import com.ecmp.core.dao.BaseEntityDao;
import com.ecmp.core.search.*;
import com.ecmp.core.service.BaseEntityService;
import com.ecmp.enums.UserAuthorityPolicy;
import com.ecmp.enums.UserType;
import com.ecmp.vo.OperateResultWithData;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：实现企业员工的业务逻辑服务
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/5/5 13:41      秦有宝                     新建
 * <p/>
 * *************************************************************************************************
 */
@Service
public class EmployeeService extends BaseEntityService<Employee> implements IEmployeeService {

    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private UserService userService;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private EmployeePositionService employeePositionService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private PositionCategoryService postCatService;
    @Autowired
    private PositionService postService;

    @Override
    protected BaseEntityDao<Employee> getDao() {
        return employeeDao;
    }

    /**
     * 保存
     *
     * @param entity 实体
     * @return 返回操作对象
     */
    @Override
    @Transactional
    public OperateResultWithData<Employee> save(Employee entity) {
        boolean isNew = entity.isNew();
        //检查该租户下员工编号不能重复
        if (employeeDao.isCodeExist(entity.getCode(), entity.getId())) {
            //00040 = 该员工编号【{0}】已存在，请重新输入！
            return OperateResultWithData.operationFailure("00040", entity.getCode());
        }
        if (isNew) {
            //级联创建账户，用户，用户配置
            UserAccount userAccount = new UserAccount();
            userAccount.setCreateAdmin(entity.isCreateAdmin());
            userAccount.setTenantCode(entity.getTenantCode());
            userAccount.setUserName(entity.getUserName());       //前台输入：用户名，员工编号
            userAccount.setUserType(UserType.Employee);                    //默认为：员工
            userAccount.setNickName(entity.getUserName());                  //默认为：用户名
            userAccount.setAccount(entity.getCode());                       //默认为：员工编号
            userAccount.setPassword(DigestUtils.md5Hex(ContextUtil.getGlobalProperty("ECMP_DEFAULT_USER_PASSWORD")));   //默认为：123456
            userAccount.setEmail(entity.getEmail());
            OperateResultWithData<UserAccount> userAccountResult = userAccountService.save(userAccount);
            entity.setId(userAccountResult.getData().getUser().getId());
            employeeDao.save(entity, true);
        } else {
            //修改用户
            User user = userService.findById(entity.getId());
            user.setUserName(entity.getUserName());
            user.setFrozen(entity.isFrozen());
            userService.save(user);
            //如果是修改管理员，修改用户配置邮箱
            if (entity.isCreateAdmin()) {
                UserProfile userProfile = userProfileService.findByUserId(entity.getId());
                userProfile.setEmail(entity.getEmail());
                userProfileService.save(userProfile);
            }
            employeeDao.save(entity, false);
        }

        OperateResultWithData<Employee> operateResultWithData;
        if (isNew) {
            operateResultWithData = OperateResultWithData.operationSuccess("core_00001");
        } else {
            operateResultWithData = OperateResultWithData.operationSuccess("core_00002");
        }
        operateResultWithData.setData(entity);
        if (isNew && entity.isCreateAdmin() && operateResultWithData.successful()) {
            emailUtil.sendEmailNotifyUser(emailUtil.constructEmailMessage(entity));
        }
        return operateResultWithData;
    }

    /**
     * 根据查询参数获取企业员工(分页)
     *
     * @param employeeQueryParam 查询参数
     * @return 企业员工
     */
    @Override
    public PageResult<Employee> findByEmployeeParam(EmployeeQueryParam employeeQueryParam) {
        PageResult<Employee> result = employeeDao.findByEmployeeParam(employeeQueryParam);
        Collection<Employee> employees = result.getRows();
        if (!CollectionUtils.isEmpty(employees)) {
            for (Employee employee : employees) {
                employee.setUserName(employee.getUser().getUserName());
            }
        }
        return result;
    }

    /**
     * 根据组织机构的id获取员工
     *
     * @param organizationId 组织机构的id
     * @return 员工清单
     */
    @Override
    public List<Employee> findByOrganizationId(String organizationId) {
        return employeeDao.findByOrganizationId(organizationId);
    }

    /**
     * 根据组织机构的id获取员工(不包含冻结)
     *
     * @param organizationId 组织机构的id
     * @return 员工清单
     */
    @Override
    public List<Employee> findByOrganizationIdWithoutFrozen(String organizationId) {
        List<Employee> list = employeeDao.findByOrganizationIdAndUserFrozenFalse(organizationId);
        list.forEach(r -> {
            //设置组织机构
            Organization organization = r.getOrganization();
            if (Objects.nonNull(organization)) {
                r.setUserRemark(organization.getName());
            }
            //设置岗位
            List<Position> positions = employeePositionService.getChildrenFromParentId(r.getId());
            if (Objects.nonNull(positions) && !positions.isEmpty()) {
                Position position = positions.get(0);
                r.setUserRemark(position.getOrganization().getName() + "-" + position.getName());
            }
        });
        return list;
    }

    /**
     * 获取企业员工用户
     *
     * @param param 企业员工用户查询参数
     * @return 员工清单
     */
    @Override
    public PageResult<Employee> findByUserQueryParam(UserQueryParam param) {
        Search search = new Search(param);
        if (param.getIncludeSubNode()) {
            List<Organization> orgs = organizationDao.getChildrenNodes4Unfrozen(param.getOrganizationId());
            List<String> orgIds = new ArrayList<>();
            orgs.forEach((r) -> orgIds.add(r.getId()));
            search.addFilter(new SearchFilter("organization.id", orgIds, SearchFilter.Operator.IN));
        } else {
            search.addFilter(new SearchFilter("organization.id", param.getOrganizationId(), SearchFilter.Operator.EQ));
        }
        return findByPage(search);
    }

    /**
     * 基于主键集合查询集合数据对象
     *
     * @param strings 主键集合
     */
    @Override
    public List<Employee> findByIds(Collection<String> strings) {
        List<Employee> employees = super.findByIds(strings);
        if (!CollectionUtils.isEmpty(employees)) {
            for (Employee employee : employees) {
                employee.setUserName(employee.getUser().getUserName());
            }
        }
        return employees;
    }

    /**
     * 通过企业用户构造流程任务执行人
     *
     * @param employee 企业用户
     */
    private Executor construstExecutor(Employee employee) {
        Executor executor = new Executor();
        executor.setId(employee.getId());
        executor.setCode(employee.getCode());
        executor.setName(employee.getUserName());
        //设置组织机构
        Organization organization = employee.getOrganization();
        if (Objects.nonNull(organization)) {
            executor.setOrganizationId(organization.getId());
            executor.setOrganizationCode(organization.getCode());
            executor.setOrganizationName(organization.getName());
            executor.setRemark(organization.getName());
        }
        //设置岗位
        List<Position> positions = employeePositionService.getChildrenFromParentId(employee.getId());
        if (Objects.nonNull(positions) && !positions.isEmpty()) {
            Position position = positions.get(0);
            executor.setPositionId(position.getId());
            executor.setPositionName(position.getName());
            executor.setPositionCode(position.getCode());
            executor.setRemark(position.getOrganization().getName() + "-" + position.getName());
        }
        return executor;
    }

    /**
     * 根据企业员工的id列表获取执行人
     *
     * @param employeeIds 企业员工的id列表
     * @return 执行人清单
     */
    @Override
    public List<Executor> getExecutorsByEmployeeIds(List<String> employeeIds) {
        if (employeeIds == null || employeeIds.size() == 0) {
            return Collections.emptyList();
        }
        List<Employee> employees = findByIds(employeeIds);
        List<Executor> executors = new ArrayList<>();
        for (Employee r : employees) {
            //排除冻结的用户
            if (r.getUser().getFrozen()) {
                continue;
            }
            executors.add(construstExecutor(r));
        }
        return executors;
    }

    /**
     * 查询可分配的功能角色
     *
     * @param featureRoleGroupId 功能角色组id
     * @param userId             用户id
     * @return 功能角色清单
     */
    @Override
    public List<FeatureRole> getCanAssignedFeatureRoles(String featureRoleGroupId, String userId) {
        return userService.getCanAssignedFeatureRoles(featureRoleGroupId, userId);
    }

    /**
     * 查询可分配的数据角色
     *
     * @param dataRoleGroupId 数据角色组id
     * @param userId          用户id
     * @return 数据角色清单
     */
    @Override
    public List<DataRole> getCanAssignedDataRoles(String dataRoleGroupId, String userId) {
        return userService.getCanAssignedDataRoles(dataRoleGroupId, userId);
    }

    /**
     * 通过租户代码获取租户管理员
     *
     * @param tenantCode 租户代码
     * @return 员工
     */
    @Override
    public Employee findAdminByTenantCode(String tenantCode) {
        List<Employee> users = employeeDao.findByTenantCodeAndUserUserAuthorityPolicy(tenantCode, UserAuthorityPolicy.TenantAdmin);
        if (CollectionUtils.isEmpty(users)) {
            users = employeeDao.findByTenantCodeAndUserUserAuthorityPolicy(tenantCode, UserAuthorityPolicy.GlobalAdmin);
        }
        if (users != null && users.size() == 1) {
            Employee employee = users.get(0);
            employee.setEmail(userProfileService.findByUserId(employee.getId()).getEmail());
            return employee;
        } else {
            return null;
        }
    }

    /**
     * 通过员工编号获取员工
     *
     * @param code 员工编号
     * @return 员工
     */
    @Override
    public Employee findByCode(String code) {
        return employeeDao.findByCodeAndTenantCode(code, ContextUtil.getTenantCode());
    }

    /**
     * 快速查询企业用户(分页)
     *
     * @param param 快速查询参数
     * @return 企业用户查询结果
     */
    @Override
    public PageResult<Employee> quickSearch(QuickSearchParam param) {
        // 构造查询参数
        Search search = new Search(param);
        Collection<String> quickSearchProperties = param.getQuickSearchProperties();
        if (Objects.isNull(quickSearchProperties) || quickSearchProperties.isEmpty()) {
            //以员工编号或姓名查询
            quickSearchProperties = new ArrayList<>();
            quickSearchProperties.add("code");
            quickSearchProperties.add("user.userName");
            search.setQuickSearchProperties(quickSearchProperties);
        }
        List<SearchOrder> sortOrders = param.getSortOrders();
        if (Objects.isNull(sortOrders) || sortOrders.isEmpty()) {
            //以员工编号排序
            sortOrders = new ArrayList<>();
            sortOrders.add(new SearchOrder("code"));
            search.setSortOrders(sortOrders);
        }
        //限制未冻结的用户
        search.addFilter(new SearchFilter("user.frozen", false, SearchFilter.Operator.EQ));
        return findByPage(search);
    }

    /**
     * 快速查询企业用户作为流程执行人
     *
     * @param param 快速查询参数
     * @return 企业用户查询结果
     */
    @Override
    public PageResult<Executor> quickSearchExecutors(QuickSearchParam param) {
        PageResult<Employee> employeeResult = quickSearch(param);
        //转换为执行人
        PageResult<Executor> result = new PageResult<>(employeeResult);
        List<Employee> employees = Objects.nonNull(employeeResult) ? employeeResult.getRows() : null;
        if (Objects.isNull(employees) || employees.isEmpty()) {
            result.setRows(Collections.emptyList());
            return result;
        }
        List<Executor> executors = new ArrayList<>();
        employees.forEach((e) -> {
            if (Objects.nonNull(e)) {
                executors.add(construstExecutor(e));
            }
        });
        result.setRows(executors);
        return result;
    }


    /**
     * 根据组织机构IDS与岗位分类IDS获取执行人
     *
     * @param orgIds     组织机构IDS
     * @param postCatIds 岗位分类IDS
     * @return 执行人清单
     */
    @Override
    public List<Executor> getExecutorsByPostCatAndOrg(List<String> orgIds, List<String> postCatIds) {
        if (Objects.isNull(orgIds) || orgIds.isEmpty() || Objects.isNull(postCatIds) || postCatIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<Executor> es = new ArrayList<>();
        List<Position> posts = postService.findByFilter(new SearchFilter(Position.POSITION_CATEGORY_ID, postCatIds, SearchFilter.Operator.IN));
        if (Objects.nonNull(posts)) {
            Set<String> postIds = posts.stream().map(Position::getId).collect(Collectors.toSet());
            List<Employee> employees = employeePositionService.getParentsFromChildIds(new ArrayList<>(postIds));
            if (Objects.nonNull(employees)) {
                List<Employee> result = employees.stream().filter(e -> orgIds.contains(e.getOrganization().getId())).collect(Collectors.toList());
                result.forEach(e -> es.add(construstExecutor(e)));
            }
        }
        return es;
    }
}
