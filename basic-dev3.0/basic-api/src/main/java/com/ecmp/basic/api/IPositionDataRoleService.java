package com.ecmp.basic.api;

import com.ecmp.basic.entity.*;
import com.ecmp.core.api.IBaseRelationService;
import io.swagger.annotations.Api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：岗位分配的数据角色API服务定义
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/5/16 9:55      王锦光（wangj）                     新建
 * <p/>
 * *************************************************************************************************
 */
@Path("positionDataRole")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "IPositionDataRoleService 岗位分配数据角色")
public interface IPositionDataRoleService extends IBaseRelationService<PositionDataRole, Position,DataRole> {

}
