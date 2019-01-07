package com.ecmp.basic.api;

import com.ecmp.basic.entity.SupplierUser;
import com.ecmp.basic.entity.vo.SupplierUserVo;
import com.ecmp.core.api.IBaseEntityService;
import com.ecmp.core.search.PageResult;
import com.ecmp.core.search.Search;
import com.ecmp.vo.OperateResult;
import com.ecmp.vo.OperateResultWithData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 供应商用户
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/3/6 20:23
 */
@Path("supplierUserService")
@Api(value = "ISupplierUserService 供应商用户")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ISupplierUserService extends IBaseEntityService<SupplierUser> {

    /**
     * 分页查询业务实体
     *
     * @param search 查询参数
     * @return 分页查询结果
     */
    @POST
    @Path("findVoByPage")
    @ApiOperation(value = "分页查询业务实体", notes = "分页查询业务实体")
    PageResult<SupplierUserVo> findVoByPage(Search search);

    /**
     * 保存供应商
     *
     * @param supplierUserVo 供应商信息
     * @return 操作结果
     */
    @POST
    @Path("saveSupplierUserVo")
    @ApiOperation(value = "保存供应商", notes = "保存供应商")
    OperateResultWithData<SupplierUserVo> saveSupplierUserVo(SupplierUserVo supplierUserVo);

    /**
     * 保存供应商管理员
     *
     * @param supplierUserVo 供应商信息
     * @param roleCode       角色代码的KEY
     * @return 操作结果
     */
    @POST
    @Path("saveSupplierManager")
    @ApiOperation(value = "保存供应商管理员", notes = "保存供应商管理员")
    OperateResult saveSupplierManager(SupplierUserVo supplierUserVo, @QueryParam("roleCodeKey") String roleCode);

    /**
     * 保存供应商管理员返回供应商用户ID
     *
     * @param supplierUserVo 供应商信息
     * @param roleCode       角色代码的KEY
     * @return 操作结果
     */
    @POST
    @Path("saveSupplierManager")
    @ApiOperation(value = "保存供应商管理员返回供应商用户ID", notes = "保存供应商管理员返回供应商用户ID")
    public OperateResultWithData<String> saveSupplierManagerBackId(SupplierUserVo supplierUserVo, @QueryParam("roleCodeKey") String roleCode);

    /**
     * 增加主数据供应商字段
     *
     * @param supplierUserVo 供应商用户VO  需要申请注册供应商ID，主数据供应商ID
     * @return 操作结果
     */
    @POST
    @Path("addSupplierIdToSupUser")
    @ApiOperation(value = "增加主数据供应商ID", notes = "增加主数据供应商ID")
    public OperateResult addSupplierIdToSupUser(SupplierUserVo supplierUserVo);

    /**
     * 根据供应商的ID查询供应商用户
     *
     * @param supplierId 供应商ID
     * @return 供应商用户
     */
    @GET
    @Path("findBySupplierId")
    @ApiOperation(value = "根据供应商的ID查询供应商用户", notes = "根据供应商的ID查询供应商用户")
    List<SupplierUser> findBySupplierId(@QueryParam("supplierId") String supplierId);

    /**
     * 冻结供应商管理员
     *
     * @param supplierId 供应商用户中的供应商ID
     * @param code       供应商用户的代码
     * @param frozen     是否冻结
     * @return 操作结果
     */
//    @POST
//    @Path("freeze")
//    @ApiOperation(value = "冻结供应商管理员", notes = "冻结供应商管理员")
//    OperateResult freeze(@QueryParam("supplierId") String supplierId, @QueryParam("code") String code, @QueryParam("frozen") boolean frozen);
}
