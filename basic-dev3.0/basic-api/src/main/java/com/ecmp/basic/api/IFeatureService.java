package com.ecmp.basic.api;

import com.ecmp.basic.entity.Feature;
import com.ecmp.core.api.IBaseEntityService;
import com.ecmp.core.api.IFindByPageService;
import com.ecmp.core.search.Search;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * *************************************************************************************************
 * <br>
 * 实现功能：功能项的API服务接口
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 版本          变更时间                  变更人                 变更原因
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 1.0.00      2017/4/19 16:45              李汶强                  新建
 * 1.0.00      2017/5/10 17:58             高银军                   修改
 * <br>
 * *************************************************************************************************<br>
 */

@Path("feature")
@Api(value = "IFeatureService 功能项的服务接口")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IFeatureService extends IBaseEntityService<Feature>, IFindByPageService<Feature> {
    /**
     * 根据功能项组id查询功能项
     *
     * @param featureGroupId 功能项组的id
     * @return 查询的结果
     */
    @GET
    @Path("findByFeatureGroupId")
    @ApiOperation(notes = "根据功能项组id查询功能项", value = "根据功能项组id查询功能项")
    List<Feature> findByFeatureGroupId(@QueryParam("featureGroupId") String featureGroupId);

    /**
     * 根据应用模块id查询功能项
     *
     * @param appModuleId 应用模块id
     * @return 功能项清单
     */
    @GET
    @Path("findByAppModuleId")
    @ApiOperation(value = "根据应用模块id查询功能项", notes = "根据应用模块id查询功能项")
    List<Feature> findByAppModuleId(@QueryParam("appModuleId") String appModuleId);

    /**
     * 根据过滤条件获取功能项
     *
     * @param search 过滤条件
     * @return 功能项列表
     */
    @POST
    @Path("findByFilters")
    @ApiOperation(value = "根据过滤条件获取功能项", notes = "根据过滤条件获取功能项")
    List<Feature> findByFilters(Search search);

}
