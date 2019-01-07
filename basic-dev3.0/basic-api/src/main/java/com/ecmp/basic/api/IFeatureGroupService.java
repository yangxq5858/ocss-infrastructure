package com.ecmp.basic.api;

import com.ecmp.basic.entity.FeatureGroup;
import com.ecmp.core.api.IBaseEntityService;
import com.ecmp.core.api.IFindAllService;
import com.ecmp.vo.OperateResult;
import com.ecmp.vo.OperateResultWithData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * *************************************************************************************************
 * <br>
 * 实现功能：
 * 功能项组的服务接口
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 版本          变更时间             变更人                     变更原因
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 1.0.00     2017/4/19  17:27    余思豆(yusidou)                 新建
 * <br>
 * *************************************************************************************************<br>
 */
@Path("featureGroup")
@Api(value = "IFeatureGroupService 功能项组的服务接口")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface IFeatureGroupService extends IBaseEntityService<FeatureGroup>,IFindAllService<FeatureGroup> {
    /**
     * 模糊查询
     *
     * @return 实体清单
     */
    @GET
    @Path("findByNameLike")
    @ApiOperation(value = "模糊查询数据", notes = "模糊查询数据")
    List<FeatureGroup> findByNameLike(@QueryParam("name") String name);

    /**
     * 根据应用模块id查询功能项组
     *
     * @param appModuleId 应用模块id
     * @return 功能项组
     */
    @GET
    @Path("findByAppModuleId")
    @ApiOperation(value = "根据应用模块id查询功能项组", notes = "根据应用模块id查询功能项组")
    List<FeatureGroup> findByAppModuleId(@QueryParam("appModuleId") String appModuleId);
}
