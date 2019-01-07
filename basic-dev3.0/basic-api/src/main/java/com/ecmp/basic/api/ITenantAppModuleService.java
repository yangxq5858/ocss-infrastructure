package com.ecmp.basic.api;

import com.ecmp.basic.entity.AppModule;
import com.ecmp.basic.entity.Tenant;
import com.ecmp.basic.entity.TenantAppModule;
import com.ecmp.core.api.IBaseRelationService;
import com.ecmp.vo.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：租户分配应用模块API服务定义
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-04 13:48      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Path("tenantAppModule")
@Api(value = "ITenantAppModuleService 租户分配应用模块")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ITenantAppModuleService extends IBaseRelationService<TenantAppModule,Tenant,AppModule> {

    /**
     * 创建分配关系
     * @param parentId 父实体Id
     * @param childIds 子实体Id清单
     * @return 操作结果
     */
    @POST
    @Path("saveRelations")
    @ApiOperation(value = "创建应用模块分配关系",notes = "通过租户Id和应用模块Id清单创建分配关系")
    OperateResult saveRelations(@QueryParam("parentId") String parentId, @QueryParam("childIds") List<String> childIds);

    /**
     * 获取当前用户可用的应用模块代码清单
     * @return 应用模块代码清单
     */
    @GET
    @Path("getAppModuleCodes")
    @ApiOperation(value = "获取当前用户可用的应用模块代码清单",notes = "获取当前用户对应租户可用的应用模块代码清单")
    List<String> getAppModuleCodes();

    /**
     * 获取当前用户可用的应用模块清单
     * @return 应用模块清单
     */
    @GET
    @Path("getTenantAppModules")
    @ApiOperation(value = "获取当前用户可用的应用模块清单",notes = "获取当前用户对应租户可用的应用模块清单")
    List<AppModule> getTenantAppModules();
}
