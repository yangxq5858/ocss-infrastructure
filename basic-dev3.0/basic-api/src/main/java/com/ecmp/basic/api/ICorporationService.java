package com.ecmp.basic.api;

import com.ecmp.basic.entity.Corporation;
import com.ecmp.core.api.IBaseEntityService;
import com.ecmp.core.api.IDataAuthEntityService;
import com.ecmp.core.api.IFindAllService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * *************************************************************************************************
 * <br>
 * 实现功能：公司服务接口
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 版本          变更时间             变更人                     变更原因
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 1.0.00      2017/6/2 16:45    余思豆(yusidou)                 新建
 * <br>
 * *************************************************************************************************<br>
 */
@Path("corporation")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "ICorporationService 公司的服务接口")
public interface ICorporationService
        extends IBaseEntityService<Corporation>, IFindAllService<Corporation>, IDataAuthEntityService<Corporation> {

    /**
     * 根据公司代码查询公司
     *
     * @param code 公司代码
     * @return 公司
     */
    @GET
    @Path("findByCode")
    @ApiOperation(value = "根据公司代码查询公司", notes = "根据公司代码查询公司")
    Corporation findByCode(@QueryParam("code")String code);

}
