package com.ecmp.basic.api;

import com.ecmp.basic.entity.DataAuthorizeType;
import com.ecmp.basic.entity.vo.DataAuthorizeTypeVo;
import com.ecmp.core.api.IBaseEntityService;
import com.ecmp.core.api.IFindAllService;
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
 * 实现功能：数据权限类型API服务定义
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-04 13:48      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Path("dataAuthorizeType")
@Api(value = "IDataAuthorizeTypeService 数据权限类型")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IDataAuthorizeTypeService extends IBaseEntityService<DataAuthorizeType>,IFindAllService<DataAuthorizeType> {
    /**
     * 通过数据角色Id获取数据权限类型（VO）
     * @param roleId 数据角色Id
     * @return 数据权限类型
     */
    @GET
    @Path("getByDataRole")
    @ApiOperation(value = "获取数据权限类型清单",notes = "通过数据角色Id获取数据权限类型（VO）")
    List<DataAuthorizeTypeVo> getByDataRole(@QueryParam("roleId") String roleId);

    /**
     * 通过数据角色Id获取数据权限类型（VO）
     * @param roleId 数据角色Id
     * @return 数据权限类型
     */
    @GET
    @Path("getByAppModuleAndDataRole")
    @ApiOperation(value = "获取数据权限类型清单",notes = "通过应用模块Id和数据角色Id获取数据权限类型（VO）")
    List<DataAuthorizeTypeVo> getByAppModuleAndDataRole(@QueryParam("appModuleId") String appModuleId,@QueryParam("roleId") String roleId);
}
