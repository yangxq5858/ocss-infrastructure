package com.ecmp.notify.api;

import com.ecmp.notity.entity.EmailMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.jws.Oneway;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：消息通知的服务接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-04-14 20:31      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Path("emailNotify")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "IEmailNotifyService", description = "EMAIL消息通知的服务")
public interface IEmailNotifyService {
    /**
     * 发送一封电子邮件
     * @param emailMessage 电子邮件消息
     */
    @Oneway
    @POST
    @Path("sendEmail")
    @ApiOperation(value = "发送一封电子邮件", notes = "发送一封邮件信息到邮件服务队列")
    void sendEmail(EmailMessage emailMessage);
}
