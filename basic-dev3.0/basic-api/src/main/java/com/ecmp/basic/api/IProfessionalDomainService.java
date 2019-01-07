package com.ecmp.basic.api;

import com.ecmp.basic.entity.ProfessionalDomain;
import com.ecmp.core.api.IBaseTreeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 领域的服务接口
 * Author:jamson
 * date:2018/3/13
 */
@Path("professionalDomainService")
@Api(value = "IProfessionalDomainService 领域的服务接口")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IProfessionalDomainService extends IBaseTreeService<ProfessionalDomain> {

    /**
     * 获取整个领域树
     *
     * @return 领域树形对象集合
     */
    @GET
    @Path("getDomainTree")
    @ApiOperation(notes = "查询所有的领域树", value = "查询所有的领域树")
    List<ProfessionalDomain> getDomainTree();
}
