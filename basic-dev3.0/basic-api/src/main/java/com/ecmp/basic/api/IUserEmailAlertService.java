package com.ecmp.basic.api;

import com.ecmp.basic.entity.UserEmailAlert;
import com.ecmp.core.api.IBaseEntityService;
import com.ecmp.vo.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by WangShuFa on 2018/7/11.
 */
@Path("userEmailAlert")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "IUserEmailAlert 用户邮件提醒API")
public interface IUserEmailAlertService extends IBaseEntityService<UserEmailAlert> {

    /**
     * 通过用户ID列表获取用户邮件通知列表
     * @param userIdS
     * @return
     */
    @POST
    @Path("findByUserIds")
    @ApiOperation(value = "根据用户ID查找", notes = "根据用户ID查找")
    public List<UserEmailAlert> findByUserIds(List<String> userIdS);

    /**
     * 通过用户ID列表更新最新提醒时间
     * @param userIds
     * @return
     */
    @POST
    @Path("updateLastTimes")
    @ApiOperation(value = "通过用户ID列表更新最新提醒时间", notes = "通过用户ID列表更新最新提醒时间")
    OperateResult updateLastTimes(List<String> userIds);
}
