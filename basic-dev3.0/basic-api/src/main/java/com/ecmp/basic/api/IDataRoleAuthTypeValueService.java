package com.ecmp.basic.api;

import com.ecmp.basic.entity.vo.DataRoleRelation;
import com.ecmp.core.entity.auth.AuthEntityData;
import com.ecmp.core.entity.auth.AuthTreeEntityData;
import com.ecmp.vo.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：数据角色分配权限类型的值API服务定义
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-04 13:48      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Path("dataRoleAuthTypeValue")
@Api(value = "IDataRoleAuthTypeValueService 数据角色分配权限类型的值")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IDataRoleAuthTypeValueService {
    /**
     * 创建数据角色的分配关系
     * @param relation 数据角色分配参数
     * @return 操作结果
     */
    @POST
    @Path("insertRelations")
    @ApiOperation(value = "创建数据角色的分配关系",notes = "通过数据角色分配参数创建分配关系")
    OperateResult insertRelations(DataRoleRelation relation);

    /**
     * 移除数据角色的分配关系
     * @param relation 数据角色分配参数
     * @return 操作结果
     */
    @POST
    @Path("removeRelations")
    @ApiOperation(value = "移除数据角色的分配关系",notes = "通过数据角色分配参数移除分配关系")
    OperateResult removeRelations(DataRoleRelation relation);

    /**
     * 通过数据角色和权限类型获取已分配的业务实体数据
     * @param roleId 数据角色Id
     * @param authTypeId 权限类型Id
     * @return 业务实体数据
     */
    @GET
    @Path("getAssignedAuthDatas")
    @ApiOperation(value = "通过数据角色和权限类型获取已分配的业务实体数据",notes = "通过数据角色Id和数据权限类型Id获取已分配的业务实体数据")
    List<AuthEntityData> getAssignedAuthDataList(@QueryParam("roleId") String roleId,@QueryParam("authTypeId") String authTypeId);

    /**
     * 通过数据角色和权限类型获取未分配的业务实体数据
     * @param roleId 数据角色Id
     * @param authTypeId 权限类型Id
     * @return 业务实体数据
     */
    @GET
    @Path("getUnassignedAuthDataList")
    @ApiOperation(value = "通过数据角色和权限类型获取未分配的业务实体数据",notes = "通过数据角色Id和数据权限类型Id获取未分配的业务实体数据")
    List<AuthEntityData> getUnassignedAuthDataList(@QueryParam("roleId") String roleId,@QueryParam("authTypeId") String authTypeId);

    /**
     * 通过数据角色和权限类型获取已分配的树形业务实体数据
     * @param roleId 数据角色Id
     * @param authTypeId 权限类型Id
     * @return 树形业务实体数据
     */
    @GET
    @Path("getAssignedAuthTreeDataList")
    @ApiOperation(value = "通过数据角色和权限类型获取已分配的树形业务实体数据",notes = "通过数据角色Id和数据权限类型Id获取已分配的树形业务实体数据")
    List<AuthTreeEntityData> getAssignedAuthTreeDataList(@QueryParam("roleId") String roleId, @QueryParam("authTypeId") String authTypeId);

    /**
     * 通过数据角色和权限类型获取未分配的树形业务实体数据(不去除已分配的节点)
     * @param roleId 数据角色Id
     * @param authTypeId 权限类型Id
     * @return 树形业务实体数据
     */
    @GET
    @Path("getUnassignedAuthTreeDataList")
    @ApiOperation(value = "通过数据角色和权限类型获取未分配的树形业务实体数据",notes = "通过数据角色Id和数据权限类型Id获取未分配的树形业务实体数据")
    List<AuthTreeEntityData> getUnassignedAuthTreeDataList(@QueryParam("roleId") String roleId,@QueryParam("authTypeId") String authTypeId);
}
