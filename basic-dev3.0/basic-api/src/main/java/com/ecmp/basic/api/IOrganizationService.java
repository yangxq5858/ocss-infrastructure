package com.ecmp.basic.api;

import com.ecmp.basic.entity.Organization;
import com.ecmp.basic.entity.vo.OrganizationDimension;
import com.ecmp.core.api.IBaseTreeService;
import com.ecmp.core.api.IDataAuthTreeEntityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：组织机构API服务接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/19 15:44        秦有宝                      新建
 * <p/>
 * *************************************************************************************************
 */
@Path("organization")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "IOrganizationService 组织机构")
public interface IOrganizationService extends IBaseTreeService<Organization>, IDataAuthTreeEntityService<Organization> {

    /**
     * 通过代码获取组织机构
     *
     * @param code 代码
     * @return 组织机构
     */
    @GET
    @Path("findByCode")
    @ApiOperation(value = "通过代码获取组织机构", notes = "通过代码获取组织机构")
    Organization findByCode(@QueryParam("code") String code);

    /**
     * 通过租户代码获取组织机构根节点
     *
     * @param tenantCode 租户代码
     * @return 组织机构
     */
    @GET
    @Path("findRootByTenantCode")
    @ApiOperation(value = "通过租户代码获取组织机构根节点", notes = "通过租户代码获取组织机构根节点")
    Organization findRootByTenantCode(@QueryParam("tenantCode") String tenantCode);

    /**
     * 获取组织机构树
     *
     * @return 组织机构树
     */
    @GET
    @Path("findOrgTree")
    @ApiOperation(value = "获取组织机构树", notes = "获取组织机构树")
    Organization findOrgTree();

    /**
     * 获取组织机构树(不包含冻结)
     *
     * @return 组织机构树清单
     */
    @GET
    @Path("findOrgTreeWithoutFrozen")
    @ApiOperation(value = "获取组织机构树(不包含冻结)", notes = "获取组织机构树(不包含冻结)")
    List<Organization> findOrgTreeWithoutFrozen();

    /**
     * 获取所有组织机构树
     *
     * @return 所有组织机构树
     */
    @GET
    @Path("findAllOrgs")
    @ApiOperation(value = "获取所有组织机构树", notes = "获取所有组织机构树")
    List<Organization> findAllOrgs();

    /**
     * 根据指定的节点id获取树
     *
     * @param nodeId 节点ID
     * @return 返回已指定节点ID为根的树
     */
    @GET
    @Path("getTree4Unfrozen")
    @ApiOperation(value = "根据指定的节点id获取树", notes = "根据指定的节点id获取树")
    Organization getTree4Unfrozen(@QueryParam("nodeId") String nodeId);

    /**
     * 通过组织机构id获取组织机构清单
     *
     * @param nodeId 组织机构id
     * @return 组织机构清单（非树形）
     */
    @GET
    @Path("getChildrenNodes4Unfrozen")
    @ApiOperation(value = "获取非树形组织机构清单", notes = "通过组织机构id获取非树形组织机构清单")
    List<Organization> getChildrenNodes4Unfrozen(@QueryParam("nodeId") String nodeId);

    /**
     * 获取组织机构维度清单
     *
     * @return 组织机构维度清单
     */
    @GET
    @Path("findOrganizationDimension")
    @ApiOperation(value = "获取组织机构维度清单", notes = "获取组织机构维度清单")
    List<OrganizationDimension> findOrganizationDimension();

}
