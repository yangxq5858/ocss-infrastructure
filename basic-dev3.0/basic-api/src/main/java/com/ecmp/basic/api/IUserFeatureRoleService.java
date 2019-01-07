package com.ecmp.basic.api;

import com.ecmp.basic.entity.FeatureRole;
import com.ecmp.basic.entity.User;
import com.ecmp.basic.entity.UserFeatureRole;
import com.ecmp.core.api.IBaseRelationService;
import io.swagger.annotations.Api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：用户分配的功能角色API服务定义
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/5/16 10:01      秦有宝                     新建
 * <p/>
 * *************************************************************************************************
 */
@Path("userFeatureRole")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "IUserFeatureRoleService 用户分配功能角色")
public interface IUserFeatureRoleService extends IBaseRelationService<UserFeatureRole, User,FeatureRole> {

}
