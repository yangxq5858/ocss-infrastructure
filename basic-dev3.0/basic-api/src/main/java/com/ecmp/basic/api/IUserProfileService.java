package com.ecmp.basic.api;

import com.ecmp.basic.entity.UserProfile;
import com.ecmp.basic.entity.vo.PersonalSettingInfo;
import com.ecmp.core.api.IBaseEntityService;
import com.ecmp.notity.entity.UserNotifyInfo;
import com.ecmp.vo.OperateResultWithData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：用户配置API服务接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间                  变更人                 变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/14 15:45            高银军                  新建
 * <p/>
 * *************************************************************************************************
 */
@Path("userProfile")
@Api(value = "IUserProfileService 用户配置")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface IUserProfileService extends IBaseEntityService<UserProfile> {
    /**
     * 查询个人设置基本信息
     *
     * @param userId 用户id
     * @return 用户配置
     */
    @GET
    @Path("findPersonalSettingInfo")
    @ApiOperation(value = "查询个人设置基本信息", notes = "查询个人设置基本信息")
    PersonalSettingInfo findPersonalSettingInfo(@QueryParam("userId") String userId);

    /**
     * 查询一个用户配置
     *
     * @param userId 用户id
     * @return 用户配置
     */
    @GET
    @Path("findByUserId")
    @ApiOperation(value = "查询一个用户配置", notes = "查询一个用户配置")
    UserProfile findByUserId(@QueryParam("userId") String userId);

    /**
     * 根据用户id列表获取通知信息
     *
     * @param userIds 用户id集合
     */
    @POST
    @Path("findNotifyInfoByUserIds")
    @ApiOperation(value = "获取通知信息列表", notes = "根据用户id列表获取通知信息列表")
    List<UserNotifyInfo> findNotifyInfoByUserIds(List<String> userIds);

    /**
     * 获取当前用户的记账用户
     *
     * @return 记账用户
     */
    @GET
    @Path("findAccountor")
    @ApiOperation(value = "获取当前用户的记账用户", notes = "获取当前用户的记账用户")
    String findAccountor();
}
