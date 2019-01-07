package com.ecmp.basic.api;

import com.ecmp.basic.entity.DataRole;
import com.ecmp.basic.entity.FeatureRole;
import com.ecmp.basic.entity.Position;
import com.ecmp.basic.entity.vo.Executor;
import com.ecmp.basic.entity.vo.PositionQueryParam;
import com.ecmp.core.api.IBaseEntityService;
import com.ecmp.core.api.IDataAuthEntityService;
import com.ecmp.core.api.IFindByPageService;
import com.ecmp.core.search.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：岗位API服务接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间                  变更人                 变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/17 13:53            高银军                  新建
 * <p/>
 * *************************************************************************************************
 */
@Path("position")
@Api(value = "IPositionService服务接口")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface IPositionService extends IBaseEntityService<Position>,IFindByPageService<Position>, IDataAuthEntityService<Position> {
    /**
     * 根据岗位id列表获取岗位
     */
    @GET
    @Path("findByIds")
    @ApiOperation(value = "根据岗位id列表获取岗位", notes = "根据岗位id列表获取岗位")
    List<Position> findByIds(@QueryParam("ids") Collection<String> ids);

    /**
     * 根据岗位类别的id来查询岗位
     *
     * @param categoryId 岗位类别id
     * @return 岗位清单
     */
    @GET
    @Path("findByCategoryId")
    @ApiOperation(value = "根据岗位类别的id来查询岗位", notes = "根据岗位类别的id来查询岗位")
    List<Position> findByCategoryId(@QueryParam("categoryId") String categoryId);

    /**
     * 根据组织机构的id获取岗位
     *
     * @param organizationId 组织机构的id
     * @return 岗位清单
     */
    @GET
    @Path("findByOrganizationId")
    @ApiOperation(value = "根据组织机构的id获取岗位", notes = "根据组织机构的id获取岗位")
    List<Position> findByOrganizationId(@QueryParam("organizationId") String organizationId);

    /**
     * 根据岗位查询参数获取获取岗位
     *
     * @param param 岗位查询参数
     * @return 岗位查询结果
     */
    @POST
    @Path("findByPositionQueryParam")
    @ApiOperation(value = "根据岗位查询参数获取岗位", notes = "根据岗位查询参数获取获取岗位")
    PageResult<Position> findByPositionQueryParam(PositionQueryParam param);

    /**
     * 根据岗位的id获取执行人
     *
     * @param positionId 岗位的id
     * @return 执行人清单
     */
    @GET
    @Path("getExecutorsByPositionId")
    @ApiOperation(value = "根据岗位的id获取执行人", notes = "根据岗位的id获取执行人")
    List<Executor> getExecutorsByPositionId(@QueryParam("positionId") String positionId);

    /**
     * 根据岗位的id列表获取执行人
     *
     * @param positionIds 岗位的id列表
     * @return 执行人清单
     */
    @GET
    @Path("getExecutorsByPositionIds")
    @ApiOperation(value = "根据岗位的id列表获取执行人", notes = "根据岗位的id列表获取执行人")
    List<Executor> getExecutorsByPositionIds(@QueryParam("positionIds") List<String> positionIds);

    /**
     * 通过岗位ids、组织维度ids和组织机构id来获取执行人
     *
     * @param positionIds 岗位的id列表
     * @param orgDimIds 组织维度的id列表
     * @param orgId 组织机构id
     * @return 执行人清单
     */
    @GET
    @Path("getExecutors")
    @ApiOperation(value = "通过岗位ids、组织维度ids和组织机构id来获取执行人", notes = "通过岗位ids、组织维度ids和组织机构id来获取执行人")
    List<Executor> getExecutors(@QueryParam("positionIds") List<String> positionIds,@QueryParam("orgDimIds") List<String> orgDimIds, @QueryParam("orgId") String orgId);

    /**
     * 根据岗位类别的id获取执行人
     *
     * @param posCateId 岗位类别的id
     * @return 执行人清单
     */
    @GET
    @Path("getExecutorsByPosCateId")
    @ApiOperation(value = "根据岗位类别的id获取执行人", notes = "根据岗位类别的id获取执行人")
    List<Executor> getExecutorsByPosCateId(@QueryParam("posCateId") String posCateId);

    /**
     * 根据岗位类别的id列表获取执行人
     *
     * @param posCateIds 岗位类别的id列表
     * @return 执行人清单
     */
    @GET
    @Path("getExecutorsByPosCateIds")
    @ApiOperation(value = "根据岗位类别的id列表获取执行人", notes = "根据岗位类别的id列表获取执行人")
    List<Executor> getExecutorsByPosCateIds(@QueryParam("posCateIds") List<String> posCateIds);

    /**
     * 查询可分配的功能角色
     *
     * @param featureRoleGroupId 功能角色组id
     * @param positionId 岗位id
     * @return 功能角色清单
     */
    @GET
    @Path("getCanAssignedFeatureRoles")
    @ApiOperation(value = "根据功能角色组的id与岗位id获取可分配的功能角色", notes = "根据功能角色组的id与岗位id获取可分配的功能角色")
    List<FeatureRole> getCanAssignedFeatureRoles(@QueryParam("featureRoleGroupId") String featureRoleGroupId,@QueryParam("positionId") String positionId);

    /**
     * 查询可分配的数据角色
     *
     * @param dataRoleGroupId 数据角色组id
     * @param positionId 岗位id
     * @return 数据角色清单
     */
    @GET
    @Path("getCanAssignedDataRoles")
    @ApiOperation(value = "根据数据角色组的id与岗位id获取可分配的数据角色", notes = "根据数据角色组的id与岗位id获取可分配的数据角色")
    List<DataRole> getCanAssignedDataRoles(@QueryParam("dataRoleGroupId") String dataRoleGroupId, @QueryParam("positionId") String positionId);

}
