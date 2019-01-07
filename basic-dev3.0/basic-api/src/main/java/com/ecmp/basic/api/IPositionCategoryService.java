package com.ecmp.basic.api;

import com.ecmp.basic.entity.PositionCategory;
import com.ecmp.core.api.IBaseEntityService;
import com.ecmp.core.api.IBaseTreeService;
import com.ecmp.core.api.IFindAllService;
import com.ecmp.vo.OperateResult;
import com.ecmp.vo.OperateResultWithData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Collection;
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
 * <p/>
 * 实现功能：岗位类别API服务接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/17 10:36        秦有宝                      新建
 * <p/>
 * *************************************************************************************************
 */
@Path("positionCategory")
@Api(value = "IPositionCategoryService 岗位类别")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface IPositionCategoryService extends IBaseEntityService<PositionCategory>,IFindAllService<PositionCategory> {

    /**
     * 根据岗位类别id列表获取岗位类别
     */
    @GET
    @Path("findByIds")
    @ApiOperation(value = "根据岗位类别id列表获取岗位类别", notes = "根据岗位类别id列表获取岗位类别")
    List<PositionCategory> findByIds(@QueryParam("ids") Collection<String> ids);
}
