package com.ecmp.basic.api;

import com.ecmp.basic.entity.*;
import com.ecmp.basic.entity.vo.EmployeeQueryParam;
import com.ecmp.basic.entity.vo.UserQueryParam;
import com.ecmp.basic.entity.vo.Executor;
import com.ecmp.core.api.IBaseEntityService;
import com.ecmp.core.api.IFindByPageService;
import com.ecmp.core.search.PageResult;
import com.ecmp.core.search.QuickSearchParam;
import com.ecmp.core.search.Search;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：企业员工API服务接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/5/5 13:36      秦有宝                     新建
 * <p/>
 * *************************************************************************************************
 */
@Path("employee")
@Api(value = "IEmployeeService 企业员工")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface IEmployeeService extends IBaseEntityService<Employee>,IFindByPageService<Employee> {
    /**
     * 根据查询参数获取企业员工(分页)
     *
     * @param employeeQueryParam 查询参数
     * @return 企业员工
     */
    @POST
    @Path("findByEmployeeParam")
    @ApiOperation(value = "根据查询参数获取企业员工", notes = "根据查询参数获取企业员工")
    PageResult<Employee> findByEmployeeParam(EmployeeQueryParam employeeQueryParam);

    /**
     * 根据员工的id列表获取员工
     *
     * @param ids 主键集合
     */
    @GET
    @Path("findByIds")
    @ApiOperation(value = "根据员工的id列表获取员工", notes = "根据员工的id列表获取员工")
    List<Employee> findByIds(@QueryParam("ids") final Collection<String> ids);

    /**
     * 根据组合条件获取员工
     */
    @POST
    @Path("findByFilters")
    @ApiOperation(value = "根据组合条件获取员工", notes = "根据组合条件获取员工")
    List<Employee> findByFilters(Search searchConfig);

    /**
     * 根据组织机构的id获取员工
     *
     * @param organizationId 组织机构的id
     * @return 员工用户查询结果
     */
    @GET
    @Path("findByOrganizationId")
    @ApiOperation(value = "根据组织机构的id获取员工", notes = "根据组织机构的id获取员工")
    List<Employee> findByOrganizationId(@QueryParam("organizationId") String organizationId);

    /**
     * 根据组织机构的id获取员工(不包含冻结)
     *
     * @param organizationId 组织机构的id
     * @return 员工清单
     */
    @GET
    @Path("findByOrganizationIdWithoutFrozen")
    @ApiOperation(value = "根据组织机构的id获取员工(不包含冻结)", notes = "根据组织机构的id获取员工(不包含冻结)")
    List<Employee> findByOrganizationIdWithoutFrozen(@QueryParam("organizationId")String organizationId);

    /**
     * 根据企业员工用户查询参数获取企业员工用户
     *
     * @param param 企业员工用户查询参数
     * @return 员工用户查询结果
     */
    @POST
    @Path("findByUserQueryParam")
    @ApiOperation(value = "获取企业员工用户", notes = "根据企业员工用户查询参数获取企业员工用户")
    PageResult<Employee> findByUserQueryParam(UserQueryParam param);

    /**
     * 根据企业员工的id列表获取执行人
     *
     * @param employeeIds 企业员工的id列表
     * @return 执行人清单
     */
    @GET
    @Path("getExecutorsByEmployeeIds")
    @ApiOperation(value = "根据企业员工的id列表获取执行人", notes = "根据企业员工的id列表获取执行人")
    List<Executor> getExecutorsByEmployeeIds(@QueryParam("employeeIds") List<String> employeeIds);

    /**
     * 查询可分配的功能角色
     *
     * @param featureRoleGroupId 功能角色组id
     * @param userId 用户id
     * @return 功能角色清单
     */
    @GET
    @Path("getCanAssignedFeatureRoles")
    @ApiOperation(value = "根据功能角色组的id与岗位id获取可分配的功能角色", notes = "根据功能角色组的id与岗位id获取可分配的功能角色")
    List<FeatureRole> getCanAssignedFeatureRoles(@QueryParam("featureRoleGroupId") String featureRoleGroupId, @QueryParam("userId") String userId);

    /**
     * 查询可分配的数据角色
     *
     * @param dataRoleGroupId 数据角色组id
     * @param userId 用户id
     * @return 数据角色清单
     */
    @GET
    @Path("getCanAssignedDataRoles")
    @ApiOperation(value = "根据数据角色组的id与岗位id获取可分配的数据角色", notes = "根据数据角色组的id与岗位id获取可分配的数据角色")
    List<DataRole> getCanAssignedDataRoles(@QueryParam("dataRoleGroupId") String dataRoleGroupId, @QueryParam("userId") String userId);

    /**
     * 通过租户代码获取租户管理员
     *
     * @param tenantCode 租户代码
     * @return 员工
     */
    @GET
    @Path("findAdminByTenantCode")
    @ApiOperation(value = "通过租户代码获取租户管理员", notes = "通过租户代码获取租户管理员")
    Employee findAdminByTenantCode(@QueryParam("tenantCode") String tenantCode);

    /**
     * 通过员工编号获取员工
     *
     * @param code 员工编号
     * @return 员工
     */
    @GET
    @Path("findByCode")
    @ApiOperation(value = "通过员工编号获取员工", notes = "通过员工编号获取员工")
    Employee findByCode(@QueryParam("code") String code);

    /**
     * 快速查询企业用户
     *
     * @param param 快速查询参数
     * @return 企业用户查询结果
     */
    @POST
    @Path("quickSearch")
    @ApiOperation(value = "快速查询企业用户", notes = "用用户名或员工编号快速查询企业用户")
    PageResult<Employee> quickSearch(QuickSearchParam param);

    /**
     * 快速查询企业用户作为流程执行人
     *
     * @param param 快速查询参数
     * @return 企业用户查询结果
     */
    @POST
    @Path("quickSearchExecutors")
    @ApiOperation(value = "快速查询企业用户", notes = "用用户名或员工编号快速查询企业用户")
    PageResult<Executor> quickSearchExecutors(QuickSearchParam param);

    /**
     * 根据组织机构IDS与岗位分类IDS获取执行人
     *
     * @param orgIds     组织机构IDS
     * @param postCatIds 岗位分类IDS
     * @return 执行人清单
     */
    @GET
    @Path("getExecutorsByPostCatAndOrg")
    @ApiOperation(value = "根据组织机构IDS与岗位分类IDS获取执行人", notes = "根据组织机构IDS与岗位分类IDS获取执行人")
    List<Executor> getExecutorsByPostCatAndOrg(@QueryParam("orgIds") List<String> orgIds, @QueryParam("postCatIds") List<String> postCatIds);

}
