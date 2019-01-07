package com.ecmp.basic.api;

import com.ecmp.basic.entity.Country;
import com.ecmp.basic.entity.vo.CurrencyVo;
import com.ecmp.core.api.IBaseEntityService;
import com.ecmp.core.api.IFindAllService;
import com.ecmp.core.search.PageResult;
import com.ecmp.core.search.Search;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * <p/>
 * 实现功能：国家的Api服务接口
 * <p/>
 *
 * @author 豆
 * @version 1.0.00
 */
@Path("country")
@Api(value = "ICountryService国家的Api服务接口")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ICountryService extends IBaseEntityService<Country>, IFindAllService<Country> {

    /**
     * 获取货币信息
     * @return 货币信息
     */
    @POST
    @Path("findCurrency")
    @ApiOperation(value = "获取货币信息",notes = "获取货币信息")
    PageResult<CurrencyVo> findCurrency(Search search);
    
    /**
     * 根据代码查询国家
     * @param code 代码
     * @return 国家信息
     */
    @GET
    @Path("findByCode")
    @ApiOperation(value = "根据代码查询国家",notes = "根据代码查询国家")
    Country findByCode(@QueryParam("code") String code);
}
