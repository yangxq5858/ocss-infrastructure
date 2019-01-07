package com.ecmp.basic.service;

import com.ecmp.basic.api.IPositionService;
import com.ecmp.basic.dao.OrganizationDao;
import com.ecmp.basic.dao.PositionDao;
import com.ecmp.basic.entity.*;
import com.ecmp.basic.entity.vo.Executor;
import com.ecmp.basic.entity.vo.PositionQueryParam;
import com.ecmp.context.util.NumberGenerator;
import com.ecmp.core.dao.BaseEntityDao;
import com.ecmp.core.search.PageResult;
import com.ecmp.core.search.Search;
import com.ecmp.core.search.SearchFilter;
import com.ecmp.core.service.BaseEntityService;
import com.ecmp.vo.OperateResult;
import com.ecmp.vo.OperateResultWithData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：岗位的业务逻辑服务
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间                  变更人                 变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/17 23:03            高银军                  新建
 * <p/>
 * *************************************************************************************************
 */
@Service
public class PositionService extends BaseEntityService<Position> implements IPositionService {
    @Autowired
    private PositionDao positionDao;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private EmployeePositionService employeePositionService;
    @Autowired
    private FeatureRoleService featureRoleService;
    @Autowired
    private DataRoleService dataRoleService;
    @Autowired
    private PositionFeatureRoleService positionFeatureRoleService;
    @Autowired
    private PositionDataRoleService positionDataRoleService;

    @Override
    protected BaseEntityDao<Position> getDao() {
        return positionDao;
    }

    /**
     * 数据保存操作
     *
     * @param entity 岗位
     */
    @Override
    public OperateResultWithData<Position> save(Position entity) {
        //检查同一部门下的岗位名称是否存在
        if (positionDao.isOrgAndNameExist(entity.getOrganization().getId(), entity.getName(), entity.getId())) {
            //00048= 该组织机构下的岗位【{0}】已存在，请重新输入！
            return OperateResultWithData.operationFailure("00048", entity.getName());
        }
        if (StringUtils.isBlank(entity.getCode())) {
            entity.setCode(NumberGenerator.getNumber(Position.class));
        }
        return super.save(entity);
    }

    /**
     * 删除数据保存数据之前额外操作回调方法 子类根据需要覆写添加逻辑即可
     *
     * @param s 待删除数据对象主键
     */
    @Override
    protected OperateResult preDelete(String s) {
        if (positionDataRoleService.isExistByParent(s)) {
            //00039 = 岗位存在已经分配的数据角色，禁止删除！
            return OperateResult.operationFailure("00039");
        }
        if (positionFeatureRoleService.isExistByParent(s)) {
            //岗位存在已经分配的功能角色，禁止删除！
            return OperateResult.operationFailure("00013");
        }
        if (employeePositionService.isExistByChild(s)) {
            //岗位存在已经分配的企业员工用户，禁止删除！
            return OperateResult.operationFailure("00017");
        }
        return super.preDelete(s);
    }

    /**
     * 根据岗位类别的id来查询岗位
     *
     * @param categoryId 岗位类别id
     * @return 岗位清单
     */
    @Override
    public List<Position> findByCategoryId(String categoryId) {
        return positionDao.findByPositionCategoryId(categoryId);
    }

    /**
     * 根据组织机构的id获取岗位
     *
     * @param organizationId 组织机构的id
     * @return 岗位清单
     */
    @Override
    public List<Position> findByOrganizationId(String organizationId) {
        return positionDao.findByOrganizationId(organizationId);
    }

    /**
     * 根据岗位查询参数获取获取岗位
     *
     * @param param 岗位查询参数
     * @return 岗位查询结果
     */
    @Override
    public PageResult<Position> findByPositionQueryParam(PositionQueryParam param) {
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
     * 查询可分配的功能角色
     *
     * @param featureRoleGroupId 功能角色组id
     * @param positionId         岗位id
     * @return 功能角色清单
     */
    @Override
    public List<FeatureRole> getCanAssignedFeatureRoles(String featureRoleGroupId, String positionId) {
        Set<FeatureRole> result = new HashSet<>();
        //获取可分配的功能角色
        List<FeatureRole> canAssigned = featureRoleService.findByFeatureRoleGroup(featureRoleGroupId);
        //获取已经分配的功能角色
        List<FeatureRole> assigned = positionFeatureRoleService.getChildrenFromParentId(positionId);
        result.addAll(canAssigned);
        result.removeAll(assigned);
        return new ArrayList<>(result);
    }

    /**
     * 查询可分配的数据角色
     *
     * @param dataRoleGroupId 数据角色组id
     * @param positionId      岗位id
     * @return 数据角色清单
     */
    @Override
    public List<DataRole> getCanAssignedDataRoles(String dataRoleGroupId, String positionId) {
        Set<DataRole> dataRoles = new HashSet<>();
        //获取可分配的数据角色
        List<DataRole> canAssigned = dataRoleService.findByDataRoleGroup(dataRoleGroupId);
        //获取已经分配的数据角色
        List<DataRole> assigned = positionDataRoleService.getChildrenFromParentId(positionId);
        dataRoles.addAll(canAssigned);
        dataRoles.removeAll(assigned);
        return new ArrayList<>(dataRoles);
    }

    /**
     * 根据岗位的id获取执行人
     *
     * @param positionId 岗位的id
     * @return 执行人清单
     */
    @Override
    public List<Executor> getExecutorsByPositionId(String positionId) {
        Position position = findOne(positionId);
        if (Objects.isNull(position)){
            return new ArrayList<>();
        }
        List<Employee> employees = employeePositionService.getParentsFromChildId(positionId);
        return constructExecutors(employees, position);

    }

    /*
    通过员工与岗位构造执行人
     */
    private List<Executor> constructExecutors(List<Employee> employees, Position position) {
        List<Executor> executors = new ArrayList<>();
        for (Employee r : employees) {
            //排除冻结的用户
            if (r.getUser().getFrozen()) {
                continue;
            }
            Executor executor = new Executor();
            executor.setId(r.getId());
            executor.setCode(r.getCode());
            executor.setName(r.getUser().getUserName());
            executor.setOrganizationId(r.getOrganization().getId());
            executor.setOrganizationName(r.getOrganization().getName());
            executor.setOrganizationCode(r.getOrganization().getCode());
            executor.setPositionId(position.getId());
            executor.setPositionName(position.getName());
            executor.setPositionCode(position.getCode());
            executor.setRemark(r.getOrganization().getName() + "-" + position.getName());
            executors.add(executor);
        }
        return executors;
    }

    /**
     * 根据岗位的id列表获取执行人
     *
     * @param positionIds 岗位的id列表
     * @return 执行人清单
     */
    @Override
    public List<Executor> getExecutorsByPositionIds(List<String> positionIds) {
        List<Executor> executors = new ArrayList<>();
        for (String positionId : positionIds) {
            executors.addAll(getExecutorsByPositionId(positionId));
        }
        //去除重复执行人
        Set<Executor> executorSet = new HashSet<>(executors);
        return new ArrayList<>(executorSet);
    }

    /**
     * 通过岗位ids、组织维度ids和组织机构id来获取执行人
     *
     * @param positionIds 岗位的id列表
     * @param orgDimIds   组织维度的id列表
     * @param orgId       组织机构id
     * @return 执行人清单
     */
    @Override
    public List<Executor> getExecutors(List<String> positionIds, List<String> orgDimIds, String orgId) {
        List<Executor> executors = getExecutorsByPositionIds(positionIds).stream().filter(r -> r.getOrganizationId().equals(orgId)).collect(Collectors.toList());
        return executors;
    }

    /**
     * 根据岗位类别的id获取执行人
     *
     * @param posCateId 岗位类别的id
     * @return 执行人清单
     */
    @Override
    public List<Executor> getExecutorsByPosCateId(String posCateId) {
        List<Position> positions = positionDao.findByPositionCategoryId(posCateId);
        List<String> ids = new ArrayList<>();
        positions.forEach((r) -> {
            String id = r.getId();
            ids.add(id);
        });
        return getExecutorsByPositionIds(ids);
    }

    /**
     * 根据岗位类别的id列表获取执行人
     *
     * @param posCateIds 岗位类别的id列表
     * @return 执行人清单
     */
    @Override
    public List<Executor> getExecutorsByPosCateIds(List<String> posCateIds) {
        if (Objects.isNull(posCateIds) || posCateIds.isEmpty()) {
            return Collections.emptyList();
        }
        SearchFilter filter = new SearchFilter("positionCategory.id", posCateIds, SearchFilter.Operator.IN);
        List<Position> positions = findByFilter(filter);
        if (Objects.isNull(positions)) {
            return new ArrayList<>();
        }
        List<String> ids = new ArrayList<>();
        positions.forEach((r) -> {
            String id = r.getId();
            ids.add(id);
        });
        return getExecutorsByPositionIds(ids);
    }
}
