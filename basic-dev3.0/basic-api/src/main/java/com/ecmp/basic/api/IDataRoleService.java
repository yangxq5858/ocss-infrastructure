package com.ecmp.basic.api;

import com.ecmp.basic.entity.DataRole;
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
 * 实现功能：数据角色API服务定义
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-04 13:48      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Path("dataRole")
@Api(value = "IDataRoleService 数据角色")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IDataRoleService extends IBaseEntityService<DataRole> {

    /**
     * 通过角色组Id获取角色清单
     * @param roleGroupId 角色组Id
     * @return 角色清单
     */
    @GET
    @Path("findByDataRoleGroup")
    @ApiOperation(value = "通过角色组获取角色清单",notes = "通过角色组Id获取角色清单")
    List<DataRole> findByDataRoleGroup(@QueryParam("roleGroupId") String roleGroupId);
}
