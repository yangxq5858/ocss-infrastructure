package com.ecmp.basic.api;

import com.ecmp.basic.entity.Employee;
import com.ecmp.basic.entity.EmployeePosition;
import com.ecmp.basic.entity.Position;
import com.ecmp.core.api.IBaseRelationService;
import com.ecmp.vo.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：企业员工用户分配岗位的API服务定义
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/5/11 20:21      秦有宝                     新建
 * <p/>
 * *************************************************************************************************
 */
@Path("employeePosition")
@Api(value = "IEmployeePositionService 企业员工用户分配岗位")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface IEmployeePositionService extends IBaseRelationService<EmployeePosition, Employee, Position> {

    /**
     * 通过子实体Id获取父实体清单
     *
     * @param childId 子实体Id
     * @return 父实体清单
     */
    @GET
    @Path("getParentsFromChildId")
    @ApiOperation(value = "通过岗位获取企业员工清单",notes = "通过岗位Id获取已分配的企业员工清单")
    List<Employee> getParentsFromChildId(@QueryParam("childId") String childId);

    /**
     * 通过父实体清单创建分配关系
     *
     * @param childId   子实体Id
     * @param parentIds 父实体Id清单
     * @return 操作结果
     */
    @POST
    @Path("insertRelationsByParents")
    @ApiOperation(value = "创建岗位分配关系",notes = "通过岗位Id和企业员工Id清单创建分配关系")
    OperateResult insertRelationsByParents(@QueryParam("childId")String childId,@QueryParam("parentIds") List<String> parentIds);

    /**
     * 通过父实体清单移除分配关系
     *
     * @param childId   子实体Id
     * @param parentIds 父实体Id清单
     * @return 操作结果
     */
    @DELETE
    @Path("removeRelationsByParents")
    @ApiOperation(value = "移除岗位分配关系",notes = "通过岗位Id和企业员工Id清单移除分配关系")
    OperateResult removeRelationsByParents(@QueryParam("childId")String childId, @QueryParam("parentIds")List<String> parentIds);
}
