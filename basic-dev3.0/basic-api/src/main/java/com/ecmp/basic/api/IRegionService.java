package com.ecmp.basic.api;

import com.ecmp.basic.entity.Region;
import com.ecmp.core.api.IBaseTreeService;
import com.ecmp.core.api.IFindByPageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * <p/>
 * 实现功能：行政区域的API服务接口
 * <p/>
 *
 * @author 豆
 * @version 1.0.00
 */
@Path("region")
@Api(value = "IRegionService 行政区域的服务接口")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IRegionService extends IBaseTreeService<Region>, IFindByPageService<Region> {

    /**
     * 获取所有行政区域树
     *
     * @return 行政区域树形对象集合
     */
    @GET
    @Path("getRegionTree")
    @ApiOperation(notes = "查询所有的行政区域树", value = "查询所有的行政区域树")
    List<Region> getRegionTree();

    /**
     * 通过国家id查询行政区域树
     *
     * @param countryId 国家id
     * @return 行政区域树清单
     */
    @GET
    @Path("getRegionTreeByCountry")
    @ApiOperation(value = "行政区域树", notes = "通过国家id查询行政区域树")
    List<Region> getRegionTreeByCountry(@QueryParam("countryId") String countryId);

    /**
     * 通过国家id查询省
     */
    @GET
    @Path("getProvinceByCountry")
    @ApiOperation(value = "通过国家id查询省", notes = "通过国家id查询省")
    List<Region> getProvinceByCountry(@QueryParam("countryId") String countryId);

    /**
     * 通过省id查询市
     */
    @GET
    @Path("getCityByProvince")
    @ApiOperation(value = "通过省id查询市", notes = "通过省id查询市")
    List<Region> getCityByProvince(@QueryParam("provinceId") String provinceId);
}
