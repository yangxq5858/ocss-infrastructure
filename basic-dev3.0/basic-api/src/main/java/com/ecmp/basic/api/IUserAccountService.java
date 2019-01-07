package com.ecmp.basic.api;

import com.ecmp.basic.entity.UserAccount;
import com.ecmp.enums.UserType;
import com.ecmp.basic.entity.vo.UserAccountVo;
import com.ecmp.basic.entity.vo.UserPasswordVo;
import com.ecmp.core.api.IBaseEntityService;
import com.ecmp.vo.OperateResultWithData;
import com.ecmp.vo.SessionUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.jws.Oneway;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：用户帐号API服务接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间                  变更人                 变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/17 14:28            高银军                  新建
 * 1.0.00      2017/4/25 19:26            秦有宝                  增加方法(login)
 * <p/>
 * *************************************************************************************************
 */
@Path("userAccount")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "IUserAccountService 用户帐号")
public interface IUserAccountService extends IBaseEntityService<UserAccount> {
    /**
     * 保存用户帐号
     *
     * @param userAccount 用户帐号
     * @return 保存后的用户帐号
     */
    @POST
    @Path("saveAccount")
    @ApiOperation(value = "保存用户帐号", notes = "保存用户帐号")
    OperateResultWithData<UserAccount> saveAccount(UserAccount userAccount);

    /**
     * 修改用户帐号密码
     *
     * @param userPasswordVo 用户帐号
     * @return 修改密码后的用户帐号
     */
    @POST
    @Path("updatePassword")
    @ApiOperation(value = "修改用户帐号密码", notes = "修改用户帐号密码")
    OperateResultWithData<UserAccount> updatePassword(UserPasswordVo userPasswordVo);

    /**
     * 根据用户Id查询用户帐号
     *
     * @param userId 用户id
     * @return 操作结果
     */
    @GET
    @Path("findByUserId")
    @ApiOperation(value = "根据用户id查询用户帐号", notes = "根据用户id查询用户帐号")
    List<UserAccountVo> findByUserId(@QueryParam("userId") String userId);

    /**
     * 根据用户类型和租户代码查询用户帐号
     *
     * @param tenantCode 租户代码
     * @param userType   用户类型
     * @return 用户帐号清单
     */
    @GET
    @Path("findByTenantCodeAndUserType")
    @ApiOperation(value = "根据用户类型和租户代码查询用户帐号", notes = "根据用户类型和租户代码查询用户帐号")
    List<UserAccount> findByTenantCodeAndUserUserType(@QueryParam("tenantCode") String tenantCode, @QueryParam("userType") UserType userType);

    /**
     * 根据用户账号和租户代码查询用户账户
     *
     * @param account    用户账号
     * @param tenantCode 租户代码
     * @return 用户账户列表
     */
    @GET
    @Path("findByAccountAndTenantCode")
    @ApiOperation(value = "根据账号和租户代码查询用户帐号", notes = "根据账号和租户代码查询用户帐号")
    UserAccount findByAccountAndTenantCode(@QueryParam("account") String account, @QueryParam("tenantCode") String tenantCode);

    /**
     * 用户登陆
     *
     * @param tenantCode 租户代码
     * @param account    账号
     * @param password   密码（MD5散列值）
     * @return 用户信息
     */
    @POST
    @Path("login")
    @ApiOperation(value = "用户登录", notes = "用户登录")
    SessionUser login(@ApiParam(name = "appId", value = "应用标识", required = true) @QueryParam("appId") String appId,
                      @ApiParam(name = "tenantCode", value = "租户代码", required = true) @QueryParam("tenantCode") String tenantCode,
                      @ApiParam(name = "account", value = "用户账号", required = true) @QueryParam("account") String account,
                      @ApiParam(name = "password", value = "密码(MD5散列值)", required = true) @QueryParam("password") String password);

    /**
     * 用户退出登录
     *
     * @param userId 用户Id
     */
    @POST
    @Path("logout")
    //@Oneway
    @ApiOperation(value = "退出登录", notes = "用户退出登录")
    Boolean logout(@ApiParam(name = "userId", value = "用户ID", required = true) @QueryParam("userId") String userId);
}
