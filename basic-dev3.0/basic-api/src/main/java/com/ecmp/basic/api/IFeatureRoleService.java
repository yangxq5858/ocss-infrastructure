package com.ecmp.basic.api;

import com.ecmp.basic.entity.Employee;
import com.ecmp.basic.entity.FeatureRole;
import com.ecmp.basic.entity.Position;
import com.ecmp.basic.entity.User;
import com.ecmp.core.api.IBaseEntityService;
import com.ecmp.vo.OperateResult;
import com.ecmp.vo.OperateResultWithData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：功能角色API服务定义
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-04 13:48      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Path("featureRole")
@Api(value = "IFeatureRoleService 功能角色")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IFeatureRoleService extends IBaseEntityService<FeatureRole> {

    /**
     * 通过角色组Id获取角色清单
     * @param roleGroupId 角色组Id
     * @return 角色清单
     */
    @GET
    @Path("findByFeatureRoleGroup")
    @ApiOperation(value = "通过角色组获取角色清单",notes = "通过角色组Id获取角色清单")
    List<FeatureRole> findByFeatureRoleGroup(@QueryParam("roleGroupId") String roleGroupId);

    /**
     * 根据功能角色的id获取已分配的用户
     *
     * @param featureRoleId 功能角色的id
     * @return 用户清单
     */
    @GET
    @Path("getAssignedEmployeesByFeatureRole")
    @ApiOperation(value = "根据功能角色的id获取已分配的用户", notes = "根据功能角色的id获取已分配的用户")
    List<User> getAssignedEmployeesByFeatureRole(@QueryParam("featureRoleId") String featureRoleId);

    /**
     * 根据功能角色的id获取已分配的岗位
     *
     * @param featureRoleId 功能角色的id
     * @return 岗位清单
     */
    @GET
    @Path("getAssignedPositionsByFeatureRole")
    @ApiOperation(value = "根据功能角色的id获取已分配的岗位", notes = "根据功能角色的id获取已分配的岗位")
    List<Position> getAssignedPositionsByFeatureRole(@QueryParam("featureRoleId") String featureRoleId);

    /**
     * 根据代码查询实体
     *
     * @param code 代码
     * @return 实体
     */
    @GET
    @Path("findByCode")
    @ApiOperation(value = "根据代码查询实体", notes = "根据代码查询实体")
    FeatureRole findByCode(@QueryParam("code") String code);
}
