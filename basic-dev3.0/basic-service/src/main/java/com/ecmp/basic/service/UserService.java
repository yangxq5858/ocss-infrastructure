package com.ecmp.basic.service;

import com.ecmp.basic.api.IUserService;
import com.ecmp.basic.dao.EmployeeDao;
import com.ecmp.basic.dao.TenantDao;
import com.ecmp.basic.dao.UserAccountDao;
import com.ecmp.basic.dao.UserDao;
import com.ecmp.basic.entity.*;
import com.ecmp.basic.entity.enums.FeatureType;
import com.ecmp.basic.entity.enums.RoleType;
import com.ecmp.basic.entity.vo.Executor;
import com.ecmp.enums.UserType;
import com.ecmp.basic.entity.vo.MenuVo;
import com.ecmp.config.util.ApiClient;
import com.ecmp.config.util.IgnoreCheckSession;
import com.ecmp.context.ContextUtil;
import com.ecmp.core.dao.BaseEntityDao;
import com.ecmp.core.entity.auth.AuthEntityData;
import com.ecmp.core.entity.auth.AuthTreeEntityData;
import com.ecmp.core.service.BaseEntityService;
import com.ecmp.enums.UserAuthorityPolicy;
import com.ecmp.exception.ServiceException;
import com.ecmp.vo.OperateResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericType;
import java.util.*;

import static com.ecmp.basic.service.DataRoleAuthTypeValueService.FIND_ALL_AUTH_ENTITY_DATA_METHOD;
import static com.ecmp.basic.service.DataRoleAuthTypeValueService.FIND_ALL_AUTH_TREE_ENTITY_DATA_METHOD;
import static com.ecmp.basic.service.DataRoleAuthTypeValueService.GET_AUTH_ENTITY_DATA_METHOD;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：实现用户的业务逻辑服务
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/14 9:46        秦有宝                      新建
 * <p/>
 * *************************************************************************************************
 */
@Service
public class UserService extends BaseEntityService<User>
        implements IUserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private FeatureRoleService featureRoleService;
    @Autowired
    private UserFeatureRoleService userFeatureRoleService;
    @Autowired
    private EmployeePositionService employeePositionService;
    @Autowired
    private PositionFeatureRoleService positionFeatureRoleService;
    @Autowired
    private FeatureRoleFeatureService featureRoleFeatureService;
    @Autowired
    private FeatureService featureService;
    @Autowired
    private TenantAppModuleService tenantAppModuleService;
    @Autowired
    private TenantDao tenantDao;
    @Autowired
    private MenuService menuService;
    @Autowired
    private DataAuthorizeTypeService dataAuthorizeTypeService;
    @Autowired
    private UserDataRoleService userDataRoleService;
    @Autowired
    private PositionDataRoleService positionDataRoleService;
    @Autowired
    private DataRoleService dataRoleService;
    @Autowired
    private DataRoleAuthTypeValueService dataRoleAuthTypeValueService;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private UserAccountDao userAccountDao;
    @Override
    protected BaseEntityDao<User> getDao() {
        return userDao;
    }

    /**
     * 根据用户id查询用户
     *
     * @param id 用户id
     * @return 用户
     */
    @Override
    public User findById(String id){
        return userDao.getById(id);
    }

    /**
     * 获取租户管理员可以使用的功能项清单
     * @param tenantCode 租户代码
     * @return 功能项清单
     */
    private List<Feature> getTenantAdminCanUseFeatures(String tenantCode){
        //获取租户
        Tenant tenant = tenantDao.findByFrozenFalseAndCode(tenantCode);
        if (tenant==null){
            return Collections.emptyList();
        }
        //获取租户已经分配的应用模块对应的功能项清单
        return featureService.getTenantCanUseFeatures(tenant);
    }

    /**
     * 获取用户有权限的功能项清单
     * @param userId 平台用户Id
     * @return 功能项清单
     */
    @Cacheable(value = "UserAuthorizedFeaturesCache",key = "'UserAuthorizedFeatures_'+#userId")
    public List<Feature> getUserAuthorizedFeatures(String userId){
        List<Feature> result = new ArrayList<>();
        //获取用户
        User user = findOne(userId);
        if (user==null){
            return result;
        }
        UserAuthorityPolicy authorityPolicy = user.getUserAuthorityPolicy();
        //判断全局管理员
        if (authorityPolicy== UserAuthorityPolicy.GlobalAdmin){
            return result;
        }
        //判断租户管理员
        if (authorityPolicy==UserAuthorityPolicy.TenantAdmin){
            //获取租户管理员可以使用的功能项清单
            return getTenantAdminCanUseFeatures(user.getTenantCode());
        }
        //一般用户的功能角色
        Set<FeatureRole> userRoles = new HashSet<>();
        //获取用户的公共角色
        List<FeatureRole> publicRoles = featureRoleService.getPublicFeatureRoles(user);
        userRoles.addAll(publicRoles);
        //获取用户授权的角色
        List<FeatureRole> authRoles = userFeatureRoleService.getChildrenFromParentId(user.getId());
        //添加可以使用的角色
        authRoles.forEach((r)->{
            if (r.getRoleType()== RoleType.CanUse){
                userRoles.add(r);
            }
        });
        //获取用户的岗位
        if (user.getUserType()== UserType.Employee){
            List<Position> positions = employeePositionService.getChildrenFromParentId(user.getId());
            List<String> positionIds = new ArrayList<>();
            positions.forEach((p)->positionIds.add(p.getId()));
            //获取岗位对应的角色
            List<FeatureRole> positionRoles = positionFeatureRoleService.getChildrenFromParentIds(positionIds);
            //添加可以使用的角色
            positionRoles.forEach((r)->{
                if (r.getRoleType()== RoleType.CanUse){
                    userRoles.add(r);
                }
            });
        }
        //获取角色分配的功能项清单
        if (userRoles.isEmpty()){
            return result;
        }
        List<String> userRoleIds = new ArrayList<>();
        userRoles.forEach((r)->userRoleIds.add(r.getId()));
        List<Feature> features = featureRoleFeatureService.getChildrenFromParentIds(userRoleIds);
        result.addAll(features);
        return result;
    }

    /**
     * 获取用户有权限的操作菜单树(VO)
     * @param userId 用户Id
     * @return 操作菜单树
     */
    @Override
    @Cacheable(value = "UserAuthorizedMenusCache",key = "'UserAuthorizedMenus_'+#userId")
    public List<MenuVo> getUserAuthorizedMenus(String userId){
        Set<Menu> userMenus = new HashSet<>();
        List<Menu> memuNodes = new ArrayList<>();
        List<MenuVo> authMemus = new ArrayList<>();
        //获取用户
        User user = findOne(userId);
        if (user==null){
            throw new ServiceException("租户【"+ContextUtil.getTenantCode()+"】,用户【"+userId+"】不存在！");
        }
        UserAuthorityPolicy authorityPolicy = user.getUserAuthorityPolicy();
        //判断全局管理员
        if (authorityPolicy== UserAuthorityPolicy.GlobalAdmin){
            userMenus.addAll(menuService.findAll());
        }
        else {
            //判断租户管理员和一般用户
            //--获取用户有权限的功能项清单
            List<Feature> userFeatures = getUserAuthorizedFeatures(userId);
            //通过功能项清单获取菜单节点
            memuNodes.addAll(menuService.findByFeatures(userFeatures));
            //拼接菜单关联的父节点
            memuNodes.forEach((m)->{
                List<Menu> parents = menuService.getParentNodes(m,true);
                userMenus.addAll(parents);
            });
        }
        //通过菜单生成展示对象
        userMenus.forEach((m)->{
            MenuVo vo = new MenuVo(m);
            //环境格式化
            //vo.setFeatureUrl(GlobalParam.environmentFormat(vo.getFeatureUrl()));
            Feature feature = m.getFeature();
            if (Objects.nonNull(feature)) {
                String baseAddress = feature.getFeatureGroup().getAppModule().getWebBaseAddress();
                StringBuilder url = new StringBuilder(64);
                url.append(ContextUtil.getGlobalProperty(baseAddress)).append(feature.getUrl());
                vo.setFeatureUrl(url.toString());
            }
            authMemus.add(vo);
        });
        //构造菜单树
        return MenuService.buildTree(authMemus);
    }

    /**
     * 获取用户有权限分配的功能项清单
     * @param userId 用户Id
     * @return 可分配的功能项清单
     */
    @Cacheable(value = "UserCanAssignFeaturesCache",key = "'UserCanAssignFeatures_'+#userId")
    public List<Feature> getUserCanAssignFeatures(String userId){
        List<Feature> result = new ArrayList<>();
        //获取用户
        User user = findOne(userId);
        if (user==null){
            return result;
        }
        UserAuthorityPolicy authorityPolicy = user.getUserAuthorityPolicy();
        //判断全局管理员
        if (authorityPolicy== UserAuthorityPolicy.GlobalAdmin){
            return featureService.findAll();
        }
        //判断租户管理员
        if (authorityPolicy==UserAuthorityPolicy.TenantAdmin){
            //获取租户管理员可以使用的功能项清单
            return getTenantAdminCanUseFeatures(user.getTenantCode());
        }
        //一般用户的可分配功能角色
        Set<FeatureRole> userRoles = new HashSet<>();
        List<FeatureRole> authRoles = userFeatureRoleService.getChildrenFromParentId(user.getId());
        //添加可以分配的角色
        authRoles.forEach((r)->{
            if (r.getRoleType()== RoleType.CanAssign){
                userRoles.add(r);
            }
        });
        //获取用户的岗位
        if (user.getUserType()== UserType.Employee){
            List<Position> positions = employeePositionService.getChildrenFromParentId(user.getId());
            List<String> positionIds = new ArrayList<>();
            positions.forEach((p)->positionIds.add(p.getId()));
            //获取岗位对应的角色
            List<FeatureRole> positionRoles = positionFeatureRoleService.getChildrenFromParentIds(positionIds);
            //添加可以分配的角色
            positionRoles.forEach((r)->{
                if (r.getRoleType()== RoleType.CanAssign){
                    userRoles.add(r);
                }
            });
        }
        //获取角色分配的功能项清单
        if (userRoles.isEmpty()){
            return result;
        }
        List<String> userRoleIds = new ArrayList<>();
        userRoles.forEach((r)->userRoleIds.add(r.getId()));
        List<Feature> features = featureRoleFeatureService.getChildrenFromParentIds(userRoleIds);
        result.addAll(features);
        return result;
    }

    /**
     * 获取用户前端权限检查的功能项键值
     * @param userId 用户Id
     * @return 功能项键值
     */
    @Override
    @Cacheable(value = "UserAuthorizedFeatureMapsCache",key = "'UserAuthorizedFeatureMaps_'+#userId")
    public Map<String,Map<String,String>> getUserAuthorizedFeatureMaps(String userId){
        //获取用户有权限的功能项清单
        List<Feature> authFeatures = getUserAuthorizedFeatures(userId);
        //是全局管理员
        if (authFeatures==null){
            return new HashMap<>();
        }
        Map<String,Map<String,String>> result = new HashMap<>();
        //循环构造键值对
        for (Feature feature : authFeatures){
            FeatureGroup featureGroup = feature.getFeatureGroup();
            if (featureGroup==null){
                continue;
            }
            AppModule appModule = featureGroup.getAppModule();
            if (appModule==null){
                continue;
            }
            //只添加操作功能项
            if (feature.getFeatureType()!= FeatureType.Operate){
                continue;
            }
            //判断应用模块键值是否存在
            if (!result.containsKey(appModule.getWebBaseAddress())){
                result.put(appModule.getWebBaseAddress(),new HashMap<>());
            }
            //添加功能项
            result.get(appModule.getWebBaseAddress()).put(feature.getCode(),feature.getUrl());
        }
        return result;
    }

    /***
     * 判断用户是否有指定功能项的权限
     * @param userId 用户Id
     * @param featureCode 功能项代码
     * @return 有无权限
     */
    public boolean hasFeatureAuthority(String userId,String featureCode){
        //获取用户
        User user = findOne(userId);
        if (user==null){
            return false;
        }
        UserAuthorityPolicy authorityPolicy = user.getUserAuthorityPolicy();
        //判断全局管理员
        if (authorityPolicy== UserAuthorityPolicy.GlobalAdmin){
            return true;
        }
        //判断租户管理员
        if (authorityPolicy==UserAuthorityPolicy.TenantAdmin){
            //获取功能项
            Feature feature = featureService.findByCode(featureCode);
            if (feature==null||feature.getFeatureGroup()==null||feature.getFeatureGroup().getAppModule()==null){
                return false;
            }
            if (!feature.getTenantCanUse()){
                return false;
            }
            AppModule appModule = feature.getFeatureGroup().getAppModule();
            //获取租户
            Tenant tenant = tenantDao.findByFrozenFalseAndCode(user.getTenantCode());
            if (tenant==null){
                return false;
            }
            TenantAppModule tenantAppModule = tenantAppModuleService.getRelation(tenant.getId(),appModule.getId());
            return tenantAppModule!=null;
        }
        //先从缓存获取
        Object cacheFeatures = redisTemplate.opsForValue().get("UserAuthorizedFeatures_"+userId);
        List<Feature> features;
        if (cacheFeatures!=null&& cacheFeatures instanceof List){
            List cacheFeaturesList = (List)cacheFeatures;
            features = new ArrayList<>();
            for (Object cache:cacheFeaturesList){
                if (cache instanceof Feature){
                    features.add((Feature)cache);
                }
            }
        }
        else {
            features = getUserAuthorizedFeatures(userId);
        }
        //判断是否已经授权
        Feature authorized = features.stream().filter((r)-> Objects.equals(r.getCode(), featureCode)).findAny().orElse(null);
        return authorized!=null;
    }

    /**
     * 清除用户权限相关的所有缓存
     */
    public void clearUserAuthorizedCaches(String userId){
        if (StringUtils.isBlank(userId)){
            return;
        }
        String pattern = "*"+userId;
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys!=null&&!keys.isEmpty()){
            redisTemplate.delete(keys);
        }
    }

    /**
     * 获取用户可以分配的数据权限业务实体清单
     * @param dataAuthTypeId 数据权限类型Id
     * @param userId 用户Id
     * @return 数据权限业务实体清单
     */
    @Override
    @Cacheable(value = "UserCanAssignAuthDataList",key = "'UserCanAssignAuthDataList_'+#dataAuthTypeId+'_'+#userId")
    public List<AuthEntityData> getUserCanAssignAuthDataList(String dataAuthTypeId,String userId){
        //获取数据权限类型
        DataAuthorizeType authorizeType =dataAuthorizeTypeService.findOne(dataAuthTypeId);
        if (authorizeType==null){
            return Collections.emptyList();
        }
        //获取用户
        User user = findOne(userId);
        if (user==null){
            return Collections.emptyList();
        }
        UserAuthorityPolicy authorityPolicy = user.getUserAuthorityPolicy();
        //判断是全局管理员，不能分配数据权限
        if (authorityPolicy==UserAuthorityPolicy.GlobalAdmin){
            return Collections.emptyList();
        }
        //判断是租户管理员，可以分配租户的所有数据
        if (authorityPolicy == UserAuthorityPolicy.TenantAdmin){
            //调用API服务，获取业务实体
            String appModuleCode = authorizeType.getAuthorizeEntityType().getAppModule().getApiBaseAddress();
            String path = String.format("%s/%s",authorizeType.getAuthorizeEntityType().getApiPath(),FIND_ALL_AUTH_ENTITY_DATA_METHOD);
            GenericType<List<AuthEntityData>> resultClass = new GenericType<List<AuthEntityData>>(){};
            return ApiClient.getEntityViaProxy(appModuleCode,path,resultClass,null);
        }
        //一般用户，通过数据角色获取业务实体清单
        Set<AuthEntityData> entities = new HashSet<>();
        //一般用户的数据角色
        Set<DataRole> userRoles = getNormalUserDataRoles(user);
        //获取角色分配的数据权限业务实体清单
        if (userRoles.isEmpty()){
            return Collections.emptyList();
        }
        userRoles.forEach((r)->{
            List<AuthEntityData> dataList = dataRoleAuthTypeValueService.getAssignedAuthDataList(r.getId(),dataAuthTypeId);
            entities.addAll(dataList);
        });
        return new ArrayList<>(entities);
    }

    /**
     * 获取一般用户的数据角色清单
     * @param user 用户
     * @return 数据角色清单
     */
    private Set<DataRole> getNormalUserDataRoles(User user){
        //一般用户的数据角色
        Set<DataRole> userRoles = new HashSet<>();
        //获取用户的公共角色
        List<DataRole> publicRoles = dataRoleService.getPublicDataRoles(user);
        userRoles.addAll(publicRoles);
        //一般用户的角色
        List<DataRole> authRoles = userDataRoleService.getChildrenFromParentId(user.getId());
        userRoles.addAll(authRoles);
        //获取用户的岗位
        if (user.getUserType()== UserType.Employee){
            List<Position> positions = employeePositionService.getChildrenFromParentId(user.getId());
            List<String> positionIds = new ArrayList<>();
            positions.forEach((p)->positionIds.add(p.getId()));
            //获取岗位对应的角色
            List<DataRole> positionRoles = positionDataRoleService.getChildrenFromParentIds(positionIds);
            userRoles.addAll(positionRoles);
        }
        return userRoles;
    }

    /**
     * 获取一般用户有权限的业务实体Id清单
     * @param entityClassName 权限对象类名
     * @param featureCode 功能项代码
     * @param userId 用户Id
     * @return 业务实体Id清单
     */
    @Override
    @Cacheable(value = "getNormalUserAuthorizedEntities",key = "#entityClassName+'_'+#featureCode+'_'+#userId")
    public List<String> getNormalUserAuthorizedEntities(String entityClassName,String featureCode,String userId){
        //获取用户
        User user = findOne(userId);
        if (user==null){
            return Collections.emptyList();
        }
        if (user.getUserAuthorityPolicy()!=UserAuthorityPolicy.NormalUser){
            return Collections.emptyList();
        }
        //获取数据权限类型
        DataAuthorizeType dataAuthType = dataAuthorizeTypeService.getByEntityClassNameAndFeature(entityClassName,featureCode);
        if (dataAuthType==null){
            return  Collections.emptyList();
        }
        //一般用户，通过数据角色获取业务实体清单
        Set<String> entityIds = new HashSet<>();
        //一般用户的数据角色
        Set<DataRole> userRoles = getNormalUserDataRoles(user);
        if (userRoles.isEmpty()){
            return Collections.emptyList();
        }
        userRoles.forEach((r)->{
            List<String> ids = dataRoleAuthTypeValueService.getAssignedEntityIds(r.getId(), dataAuthType.getId());
            entityIds.addAll(ids);
        });
        return new ArrayList<>(entityIds);
    }

    /**
     * 获取用户可以分配的数据权限树形业务实体清单
     *
     * @param dataAuthTypeId 数据权限类型Id
     * @param userId         用户Id
     * @return 数据权限树形业务实体清单
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "UserCanAssignAuthTreeDataList",key = "'UserCanAssignAuthTreeDataList_'+#dataAuthTypeId+'_'+#userId")
    public List<AuthTreeEntityData> getUserCanAssignAuthTreeDataList(String dataAuthTypeId, String userId) {
        //获取数据权限类型
        DataAuthorizeType authorizeType =dataAuthorizeTypeService.findOne(dataAuthTypeId);
        if (authorizeType==null){
            return Collections.emptyList();
        }
        //获取用户
        User user = findOne(userId);
        if (user==null){
            return Collections.emptyList();
        }
        UserAuthorityPolicy authorityPolicy = user.getUserAuthorityPolicy();
        //判断是全局管理员，不能分配数据权限
        if (authorityPolicy==UserAuthorityPolicy.GlobalAdmin){
            return Collections.emptyList();
        }
        //判断是租户管理员，可以分配租户的所有数据
        if (authorityPolicy == UserAuthorityPolicy.TenantAdmin){
            //调用API服务，获取业务实体
            String appModuleCode = authorizeType.getAuthorizeEntityType().getAppModule().getApiBaseAddress();
            String path = String.format("%s/%s",authorizeType.getAuthorizeEntityType().getApiPath(),FIND_ALL_AUTH_TREE_ENTITY_DATA_METHOD);
            GenericType<List<AuthTreeEntityData>> resultClass = new GenericType<List<AuthTreeEntityData>>(){};
            return ApiClient.getEntityViaProxy(appModuleCode,path,resultClass,null);
        }
        //一般用户，通过数据角色获取业务实体清单
        Set<String> entityIds = new HashSet<>();
        //一般用户的数据角色
        Set<DataRole> userRoles = getNormalUserDataRoles(user);
        if (userRoles.isEmpty()){
            return Collections.emptyList();
        }
        userRoles.forEach((r)->{
            List<String> ids = dataRoleAuthTypeValueService.getAssignedEntityIds(r.getId(),dataAuthTypeId);
            entityIds.addAll(ids);
        });
        //通过业务实体Id清单获取树形业务实体
        //调用API服务，获取业务实体
        String appModuleCode = authorizeType.getAuthorizeEntityType().getAppModule().getApiBaseAddress();
        String path = String.format("%s/%s",authorizeType.getAuthorizeEntityType().getApiPath(),GET_AUTH_ENTITY_DATA_METHOD);
        GenericType<List<AuthTreeEntityData>> resultClass = new GenericType<List<AuthTreeEntityData>>(){};
        return ApiClient.postViaProxyReturnResult(appModuleCode,path,resultClass,entityIds);
    }

    /**
     * 查询可分配的功能角色
     *
     * @param featureRoleGroupId 功能角色组id
     * @param userId 用户id
     * @return 功能角色清单
     */
    public List<FeatureRole> getCanAssignedFeatureRoles(String featureRoleGroupId, String userId) {
        Set<FeatureRole> result = new HashSet<>();
        //获取可分配的功能角色
        List<FeatureRole> canAssigned = featureRoleService.findByFeatureRoleGroup(featureRoleGroupId);
        //获取已经分配的功能角色
        List<FeatureRole> assigned = userFeatureRoleService.getChildrenFromParentId(userId);
        result.addAll(canAssigned);
        result.removeAll(assigned);
        return new ArrayList<>(result);
    }

    /**
     * 查询可分配的数据角色
     *
     * @param dataRoleGroupId 数据角色组id
     * @param userId          用户id
     * @return 数据角色清单
     */
    public List<DataRole> getCanAssignedDataRoles(String dataRoleGroupId, String userId) {
        Set<DataRole> result = new HashSet<>();
        //获取可分配的数据角色
        List<DataRole> canAssigned = dataRoleService.findByDataRoleGroup(dataRoleGroupId);
        //获取已经分配的功能角色
        List<DataRole> assigned = userDataRoleService.getChildrenFromParentId(userId);
        result.addAll(canAssigned);
        result.removeAll(assigned);
        return new ArrayList<>(result);
    }

    /**
     * 测试后台作业服务方法
     *
     * @return 操作结果
     */
    @Override
    @IgnoreCheckSession
    public OperateResult taskOne(){
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return OperateResult.operationSuccess("执行成功");
    }

    /**
     * 测试后台作业服务方法
     *
     * @return 操作结果
     */
    @Override
    @IgnoreCheckSession
    public OperateResult taskTwo(){
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return OperateResult.operationSuccess("执行成功");
    }

    @Override
    public List<Executor> getExecutorsByUserIds(List<String> userIds){
        if (userIds == null || userIds.size() == 0) {
            return Collections.emptyList();
        }
        List<User> users = findByIds(userIds);
        List<Executor> executors = new ArrayList<>();
        for (User r : users) {
            //排除冻结的用户
            if (r.getFrozen()) {
                continue;
            }
            executors.add(construstExecutor(r));
        }
        return executors;
    }


    /**
     * 通过用户构造流程任务执行人
     *
     * @param user 用户
     */
    private Executor construstExecutor(User user) {
        Executor executor = new Executor();
        executor.setId(user.getId());
        executor.setName(user.getUserName());

        Employee employee = employeeDao.findOne(user.getId());
        if(employee!=null){
            executor.setCode(employee.getCode());
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
        }else{
            List<UserAccount> list= userAccountDao.findByUserId(user.getId());
            if(list!=null&&list.size()>0){
                executor.setCode(list.get(0).getAccount());
            }
        }
        return executor;
    }


}