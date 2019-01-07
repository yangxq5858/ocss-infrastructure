package com.ecmp.notify.api;

import com.ecmp.notity.entity.EcmpMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.jws.Oneway;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：平台消息通知服务接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-06-15 15:43      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Path("notify")
@Api(value = "INotifyService", description = "平台消息通知的服务")
public interface INotifyService {
    /**
     * 发送平台消息通知
     * @param message 平台消息
     */
    @Oneway
    @POST
    @Path("send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "发送平台消息通知", notes = "发送一个平台消息到消息服务队列")
    void send(EcmpMessage message);

    /**
     * Hello World
     * @param message 消息
     * @return 问候消息
     */
    @GET
    @Path("hello")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation("Hello World")
    String hello(@QueryParam("message") String message);
}
