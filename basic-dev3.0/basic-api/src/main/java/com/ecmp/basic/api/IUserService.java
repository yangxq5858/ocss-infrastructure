package com.ecmp.basic.api;

import com.ecmp.basic.entity.User;
import com.ecmp.basic.entity.vo.Executor;
import com.ecmp.basic.entity.vo.MenuVo;
import com.ecmp.core.api.IBaseEntityService;
import com.ecmp.core.api.IFindByPageService;
import com.ecmp.core.entity.auth.AuthEntityData;
import com.ecmp.core.entity.auth.AuthTreeEntityData;
import com.ecmp.vo.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：用户API服务接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/14 9:42        秦有宝                      新建
 * <p/>
 * *************************************************************************************************
 */

@Path("user")
@Api(value = "IUserService 用户")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface IUserService extends IBaseEntityService<User>, IFindByPageService<User> {

    /**
     * 根据用户id查询用户
     *
     * @param id 用户id
     * @return 用户
     */
    @GET
    @Path("findByUserId")
    @ApiOperation(notes = "根据用户id查询用户", value = "根据用户id查询一个用户")
    User findById(@QueryParam("id")String id);

    /**
     * 获取用户有权限的操作菜单树(VO)
     * @param userId 用户Id
     * @return 操作菜单树
     */
    @GET
    @Path("getUserAuthorizedMenus")
    @ApiOperation(value = "获取用户有权限的操作菜单树",notes = "获取用户有权限的功能项对应的菜单树")
    List<MenuVo> getUserAuthorizedMenus(@QueryParam("userId") String userId);

    /**
     * 获取用户前端权限检查的功能项键值
     * @param userId 用户Id
     * @return 功能项键值
     */
    @GET
    @Path("getUserAuthorizedFeatureMaps")
    @ApiOperation(value = "获取用户前端权限检查的功能项键值",notes = "获取用户前端权限检查的功能项键值(以应用模块代码分组)")
    Map<String,Map<String,String>> getUserAuthorizedFeatureMaps(@QueryParam("userId") String userId);

    /**
     * 获取用户可以分配的数据权限业务实体清单
     * @param dataAuthTypeId 数据权限类型Id
     * @param userId 用户Id
     * @return 数据权限业务实体清单
     */
    @GET
    @Path("getUserCanAssignAuthDataList")
    @ApiOperation(value = "获取用户可以分配的数据权限业务实体清单",notes = "通过数据权限类型获取用户可以分配的数据权限业务实体清单")
    List<AuthEntityData> getUserCanAssignAuthDataList(@QueryParam("dataAuthTypeId") String dataAuthTypeId,@QueryParam("userId") String userId);

    /**
     * 获取一般用户有权限的业务实体Id清单
     * @param entityClassName 权限对象类名
     * @param featureCode 功能项代码
     * @param userId 用户Id
     * @return 业务实体Id清单
     */
    @GET
    @Path("getNormalUserAuthorizedEntities")
    @ApiOperation(value = "获取一般用户有权限的业务实体Id清单",notes = "通过权限对象类名和功能项代码获取一般用户有权限的业务实体Id清单")
    List<String> getNormalUserAuthorizedEntities(@QueryParam("entityClassName") String entityClassName,@QueryParam("featureCode") String featureCode,@QueryParam("userId") String userId);

    /**
     * 获取用户可以分配的数据权限树形业务实体清单
     * @param dataAuthTypeId 数据权限类型Id
     * @param userId 用户Id
     * @return 数据权限树形业务实体清单
     */
    @GET
    @Path("getUserCanAssignAuthTreeDataList")
    @ApiOperation(value = "获取用户可以分配的数据权限树形业务实体清单",notes = "通过数据权限类型获取用户可以分配的数据权限树形业务实体清单")
    List<AuthTreeEntityData> getUserCanAssignAuthTreeDataList(@QueryParam("dataAuthTypeId") String dataAuthTypeId, @QueryParam("userId") String userId);

    /**
     * 测试后台作业服务方法
     *
     * @return 操作结果
     */
    @POST
    @Path("taskOne")
    @ApiOperation(value = "测试后台作业服务方法",notes = "测试后台作业服务方法")
    OperateResult taskOne();

    /**
     * 测试后台作业服务方法
     *
     * @return 操作结果
     */
    @POST
    @Path("taskTwo")
    @ApiOperation(value = "测试后台作业服务方法",notes = "测试后台作业服务方法")
    OperateResult taskTwo();


    /**
     * 根据用户的id列表获取执行人（如果有员工信息，另赋值组织机构和岗位信息）
     *
     * @param userIds 用户的id列表
     * @return 执行人清单
     */
    @GET
    @Path("getExecutorsByUserIds")
    @ApiOperation(value = "根据用户的id列表获取执行人", notes = "根据用户的id列表获取执行人")
    List<Executor> getExecutorsByUserIds(@QueryParam("userIds") List<String> userIds);
}
