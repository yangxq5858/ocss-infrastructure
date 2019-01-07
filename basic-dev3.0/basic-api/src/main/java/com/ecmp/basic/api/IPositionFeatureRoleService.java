package com.ecmp.basic.api;

import com.ecmp.basic.entity.*;
import com.ecmp.core.api.IBaseRelationService;
import io.swagger.annotations.Api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：岗位分配的功能角色API服务定义
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/5/16 9:55      秦有宝                     新建
 * <p/>
 * *************************************************************************************************
 */
@Path("positionFeatureRole")
@Api(value = "IPositionFeatureRoleService 岗位分配功能角色")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface IPositionFeatureRoleService extends IBaseRelationService<PositionFeatureRole, Position,FeatureRole> {

}
