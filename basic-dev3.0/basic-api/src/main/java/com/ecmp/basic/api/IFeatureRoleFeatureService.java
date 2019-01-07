package com.ecmp.basic.api;

import com.ecmp.basic.entity.Feature;
import com.ecmp.basic.entity.FeatureRole;
import com.ecmp.basic.entity.FeatureRoleFeature;
import com.ecmp.core.api.IBaseRelationService;
import com.ecmp.vo.OperateResult;
import com.ecmp.vo.OperateResultWithData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：功能角色分配的功能项API服务定义
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-04 13:48      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Path("featureRoleFeature")
@Api(value = "IFeatureRoleFeatureService 功能角色分配的功能项")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IFeatureRoleFeatureService extends IBaseRelationService<FeatureRoleFeature,FeatureRole,Feature>{
}
