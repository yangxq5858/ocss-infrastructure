package com.ecmp.basic.api;

import com.ecmp.basic.entity.ExpertUser;
import com.ecmp.basic.entity.vo.ExpertUserVo;
import com.ecmp.core.search.PageResult;
import com.ecmp.core.search.Search;
import com.ecmp.vo.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * 专家用户
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/3/6 20:23
 */
@Path("expertUser")
@Api(value = "IExpertUserService 专家用户")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface IExpertUserService {

    /**
     * 分页查询业务实体
     *
     * @param search 查询参数
     * @return 分页查询结果
     */
    @POST
    @Path("findVoByPage")
    @ApiOperation(value = "分页查询业务实体", notes = "分页查询业务实体")
    PageResult<ExpertUserVo> findVoByPage(Search search);

    /**
     * 通过ID将该实体冻结/解冻
     *
     * @param id     实体ID
     * @param frozen 是否冻结，是true,否false
     * @return 操作结果
     */
    @POST
    @Path("freeze")
    @ApiOperation(value = "通过ID将该实体冻结/解冻", notes = "通过ID将该实体冻结/解冻")
    OperateResult freeze(@QueryParam("id") String id, Boolean frozen);

    /**
     * 通过专家用户中专家的ID将该实体冻结/解冻
     *
     * @param expertId 实体ID
     * @param frozen   是否冻结，是true,否false
     * @return 操作结果
     */
    @POST
    @Path("freezeByExpertId")
    @ApiOperation(value = "通过专家用户中专家的ID将该实体冻结/解冻", notes = "通过专家用户中专家的ID将该实体冻结/解冻")
    OperateResult freezeByExpertId(@QueryParam("expertId") String expertId, Boolean frozen);

    /**
     * 保存专家用户
     *
     * @param expertUserVo 专家用户
     * @return 操作结果
     */
    @POST
    @Path("save")
    @ApiOperation(value = "保存专家用户", notes = "保存专家用户")
    OperateResult save(ExpertUserVo expertUserVo);

    /**
     * 根据专家用户中的专家的ID删除业务实体
     *
     * @param expertId 专家用户中的专家的ID
     * @return 操作结果
     */
    @DELETE
    @Path("deleteByExpertId")
    @ApiOperation(value = "根据专家用户中的专家的ID删除业务实体", notes = "根据专家用户中的专家的ID删除业务实体")
    OperateResult deleteByExpertId(String expertId);

    /**
     * 通过Id获取一个业务实体
     *
     * @param id 业务实体Id
     * @return 业务实体
     */
    @GET
    @Path("findOne")
    @ApiOperation(value = "获取一个业务实体", notes = "通过Id获取一个业务实体")
    ExpertUser findOne(@QueryParam("id") String id);

}
