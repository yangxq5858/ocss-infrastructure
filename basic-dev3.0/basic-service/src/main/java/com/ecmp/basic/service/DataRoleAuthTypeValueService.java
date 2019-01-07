package com.ecmp.basic.service;

import com.ecmp.basic.api.IDataRoleAuthTypeValueService;
import com.ecmp.basic.dao.DataRoleAuthTypeValueDao;
import com.ecmp.basic.entity.DataAuthorizeType;
import com.ecmp.basic.entity.DataRoleAuthTypeValue;
import com.ecmp.basic.entity.vo.DataRoleRelation;
import com.ecmp.config.util.ApiClient;
import com.ecmp.context.ContextUtil;
import com.ecmp.core.dao.BaseEntityDao;
import com.ecmp.core.entity.auth.AuthEntityData;
import com.ecmp.core.entity.auth.AuthTreeEntityData;
import com.ecmp.core.service.BaseEntityService;
import com.ecmp.vo.OperateResult;
import com.ecmp.vo.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.GenericType;
import java.util.*;

/**
 * 实现功能：数据角色分配权限类型的值业务逻辑实现
 *
 * @author 王锦光(wangj)
 * @version 1.0.00      2017-05-04 14:04
 */
@Service
public class DataRoleAuthTypeValueService extends BaseEntityService<DataRoleAuthTypeValue>
        implements IDataRoleAuthTypeValueService {
    static final String GET_AUTH_ENTITY_DATA_METHOD = "getAuthEntityDataByIds";
    private static final String GET_AUTH_TREE_ENTITY_DATA_METHOD = "getAuthTreeEntityDataByIds";
    static final String FIND_ALL_AUTH_ENTITY_DATA_METHOD = "findAllAuthEntityData";
    static final String FIND_ALL_AUTH_TREE_ENTITY_DATA_METHOD = "findAllAuthTreeEntityData";
    @Autowired
    private DataRoleAuthTypeValueDao dao;

    @Override
    protected BaseEntityDao<DataRoleAuthTypeValue> getDao() {
        return dao;
    }

    @Autowired
    private DataAuthorizeTypeService dataAuthorizeTypeService;
    @Autowired
    private UserService userService;

    /**
     * 创建数据角色分配关系
     *
     * @param relation 数据角色分配参数
     * @return 操作结果
     */
    @Override
    public OperateResult insertRelations(DataRoleRelation relation) {
        if (Objects.isNull(relation) || relation.getEntityIds().isEmpty()) {
            return OperateResult.operationSuccess("core_00010", 0);
        }
        //排除已经存在的分配关系
        DataRoleRelation existRelation = dao.getDataRoleRelation(relation.getDataRoleId(), relation.getDataAuthorizeTypeId());
        Set<String> addEntityIds = new HashSet<>();
        addEntityIds.addAll(relation.getEntityIds());
        addEntityIds.removeAll(existRelation.getEntityIds());
        //创建需要创建的分配关系
        List<DataRoleAuthTypeValue> relations = new ArrayList<>();
        addEntityIds.forEach((e) -> relations.add(new DataRoleAuthTypeValue(relation.getDataRoleId(), relation.getDataAuthorizeTypeId(), e)));
        //提交数据库
        if (!relations.isEmpty()) {
            save(relations);
        }
        //成功创建{0}个分配关系！
        return OperateResult.operationSuccess("core_00010", relations.size());
    }

    /**
     * 移除数据角色的分配关系
     *
     * @param relation 数据角色分配参数
     * @return 操作结果
     */
    @Override
    @Transactional
    public OperateResult removeRelations(DataRoleRelation relation) {
        if (Objects.isNull(relation) || relation.getEntityIds().isEmpty()) {
            return OperateResult.operationSuccess("core_00011", 0);
        }
        List<DataRoleAuthTypeValue> values = dao.getRelationValues(relation);
        //执行删除
        if (values != null && !values.isEmpty()) {
            dao.deleteAll(values);
        }
        //成功移除{0}个分配关系！
        return OperateResult.operationSuccess("core_00011", values != null ? values.size() : 0);
    }

    /**
     * 通过数据角色和权限类型获取已分配的业务实体数据
     *
     * @param roleId     数据角色Id
     * @param authTypeId 权限类型Id
     * @return 业务实体数据
     */
    @Override
    @Transactional(readOnly = true)
    public List<AuthEntityData> getAssignedAuthDataList(String roleId, String authTypeId) {
        //获取数据权限类型
        DataAuthorizeType authorizeType = dataAuthorizeTypeService.findOne(authTypeId);
        if (Objects.isNull(authorizeType)) {
            return Collections.emptyList();
        }
        List<String> entityIds = dao.getAssignedEntityIds(roleId, authTypeId);
        if (Objects.isNull(entityIds) || entityIds.isEmpty()) {
            return Collections.emptyList();
        }
        //调用API服务，获取业务实体
        String appModuleCode = authorizeType.getAuthorizeEntityType().getAppModule().getApiBaseAddress();
        String path = String.format("%s/%s", authorizeType.getAuthorizeEntityType().getApiPath(), GET_AUTH_ENTITY_DATA_METHOD);
        GenericType<List<AuthEntityData>> resultClass = new GenericType<List<AuthEntityData>>() {
        };
        return ApiClient.postViaProxyReturnResult(appModuleCode, path, resultClass, entityIds);
    }

    /**
     * 通过数据角色和权限类型获取未分配的业务实体数据
     *
     * @param roleId     数据角色Id
     * @param authTypeId 权限类型Id
     * @return 业务实体数据
     */
    @Override
    @Transactional(readOnly = true)
    public List<AuthEntityData> getUnassignedAuthDataList(String roleId, String authTypeId) {
        Set<AuthEntityData> dataSet = new HashSet<>();
        //获取当前用户
        SessionUser sessionUser = ContextUtil.getSessionUser();
        //获取当前用户可分配的数据
        List<AuthEntityData> canAssigned = userService.getUserCanAssignAuthDataList(authTypeId, sessionUser.getUserId());
        //获取已经分配的数据
        List<AuthEntityData> assigned = getAssignedAuthDataList(roleId, authTypeId);
        dataSet.addAll(canAssigned);
        dataSet.removeAll(assigned);
        return new ArrayList<>(dataSet);
    }

    /**
     * 通过数据角色和权限类型获取已分配的树形业务实体数据
     *
     * @param roleId     数据角色Id
     * @param authTypeId 权限类型Id
     * @return 树形业务实体数据
     */
    @Override
    @Transactional(readOnly = true)
    public List<AuthTreeEntityData> getAssignedAuthTreeDataList(String roleId, String authTypeId) {
        //获取数据权限类型
        DataAuthorizeType authorizeType = dataAuthorizeTypeService.findOne(authTypeId);
        if (Objects.isNull(authorizeType)) {
            return Collections.emptyList();
        }
        List<String> entityIds = dao.getAssignedEntityIds(roleId, authTypeId);
        if (Objects.isNull(entityIds) || entityIds.isEmpty()) {
            return Collections.emptyList();
        }
        //调用API服务，获取业务实体
        String appModuleCode = authorizeType.getAuthorizeEntityType().getAppModule().getApiBaseAddress();
        String path = String.format("%s/%s", authorizeType.getAuthorizeEntityType().getApiPath(), GET_AUTH_TREE_ENTITY_DATA_METHOD);
        GenericType<List<AuthTreeEntityData>> resultClass = new GenericType<List<AuthTreeEntityData>>() {
        };
        return ApiClient.postViaProxyReturnResult(appModuleCode, path, resultClass, entityIds);
    }

    /**
     * 通过数据角色和权限类型获取未分配的树形业务实体数据(不去除已分配的节点)
     *
     * @param roleId     数据角色Id
     * @param authTypeId 权限类型Id
     * @return 树形业务实体数据
     */
    @Override
    @Transactional(readOnly = true)
    public List<AuthTreeEntityData> getUnassignedAuthTreeDataList(String roleId, String authTypeId) {
        //获取当前用户
        SessionUser sessionUser = ContextUtil.getSessionUser();
        //获取当前用户可分配的数据
        return userService.getUserCanAssignAuthTreeDataList(authTypeId, sessionUser.getUserId());
    }

    /**
     * 通过数据角色和权限类型获取已分配的业务实体Id清单
     *
     * @param roleId     数据角色Id
     * @param authTypeId 权限类型Id
     * @return 业务实体Id清单
     */
    @Transactional(readOnly = true)
    List<String> getAssignedEntityIds(String roleId, String authTypeId) {
        return dao.getAssignedEntityIds(roleId, authTypeId);
    }

    /**
     * 判断数据角色是否已经存在分配权限对象
     *
     * @param roleId 数据角色Id
     * @return 是否已经存在分配
     */
    @Transactional(readOnly = true)
    boolean isAlreadyAssign(String roleId, String dataAuthTypeId) {
        return dao.isAlreadyAssign(roleId, dataAuthTypeId);
    }
}
