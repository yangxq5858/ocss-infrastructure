package com.ecmp.basic.api;

import com.ecmp.basic.entity.AppModule;
import com.ecmp.core.api.IBaseEntityService;
import com.ecmp.core.api.IFindAllService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * *************************************************************************************************
 * <br>
 * 实现功能：
 * 应用模块的服务接口
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 版本          变更时间             变更人                     变更原因
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 1.0.00     2017/4/19  16:55     余思豆(yusidou)                 新建
 * <br>
 * *************************************************************************************************<br>
 */
@Path("appModule")
@Api(value = "IAppModuleService 应用模块的服务接口")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IAppModuleService extends IBaseEntityService<AppModule>, IFindAllService<AppModule> {

    /**
     * 通过代码查询应用模块
     *
     * @param code 应用模块代码
     * @return 应用模块
     */
    @GET
    @Path("findByCode")
    @ApiOperation(value = "查询应用模块", notes = "通过代码查询应用模块")
    AppModule findByCode(@QueryParam("code") String code);
}
