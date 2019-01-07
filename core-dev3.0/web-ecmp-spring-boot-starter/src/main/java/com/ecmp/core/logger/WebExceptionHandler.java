package com.ecmp.core.logger;

import com.ecmp.core.util.HttpUtils;
import com.ecmp.core.vo.OperateStatus;
import com.ecmp.log.util.LogUtil;
import com.ecmp.util.JsonUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.InternalServerErrorException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * *************************************************************************************************<br>
 * <br>
 * 实现功能：<br>
 * <br>
 * ------------------------------------------------------------------------------------------------<br>
 * 版本          变更时间             变更人                     变更原因<br>
 * ------------------------------------------------------------------------------------------------<br>
 * 1.0.00      2017/4/18 22:37      马超(Vision.Mac)                新建<br>
 * <br>
 * *************************************************************************************************<br>
 */
public class WebExceptionHandler extends SimpleMappingExceptionResolver {

//    private static MqProducer<ExceptionLog> producer = new MqProducer<ExceptionLog>(ExceptionLog.EXCEPTION_TOPIC);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //记录异常
//        recordException(request, handler, ex);
        LogUtil.error(ExceptionUtils.getMessage(ex), JsonUtils.toJson(request.getParameterMap()), ex);

        ModelAndView mav = super.resolveException(request, response, handler, ex);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        String accept = request.getHeader("accept");
        if (accept != null && !(accept.contains("application/json") || HttpUtils.isAsync(request))) {
            if (mav != null && handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                request.setAttribute("className", handlerMethod.getBeanType().getName());
                request.setAttribute("methodName", handlerMethod.getMethod().getName());
                request.setAttribute("requestUrl", request.getRequestURI());
                request.setAttribute("requestData", JsonUtils.toJson(request.getParameterMap()));
                request.setAttribute("message", ExceptionUtils.getMessage(ex));
                request.setAttribute("stackTrace", ExceptionUtils.getStackTrace(ex));
            }
        } else {
            //用utf8来解析返回的数据
            response.setHeader("Content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
            //servlet用UTF-8转码，而不是用默认的ISO8859
            response.setCharacterEncoding("UTF-8");
            try {
                // json 请求返回
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
                //OperateStatus operateStatus = OperateStatus.defaultFailure();
                OperateStatus operateStatus;
                if (ex instanceof InternalServerErrorException) {
                    operateStatus = OperateStatus.defaultFailure();
                    operateStatus.setData(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                } else {
                    operateStatus = new OperateStatus(Boolean.FALSE, ex.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
                String json = JsonUtils.toJson(operateStatus);
                writer.write(json);
                writer.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
        return mav;
    }

    /**
     * 收集异常信息并通过队列发送
     */
    /*private void recordException(HttpServletRequest request, Object handler, Exception ex) {
        String stackTrace = "";
        SessionUser sessionUser = ContextUtil.getSessionUser();
        try {
            stackTrace = ExceptionUtils.getStackTrace(ex);

            ExceptionLog exLog = new ExceptionLog();
            exLog.setRuntime(ExceptionLog.Runtime.Web);
            exLog.setOccurrenceTime(new Date());
            exLog.setAppModule(ContextUtil.getAppCode());
            exLog.setMessage(ExceptionUtils.getMessage(ex));
            exLog.setStackTrace(ExceptionUtils.getStackTrace(ex));
            exLog.setInnerException(ExceptionUtils.getRootCauseMessage(ex));
            exLog.setExceptionType(ClassUtils.getShortClassName(ex, ""));

            if (handler != null && handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                exLog.setClassName(handlerMethod.getBeanType().getName());
                exLog.setMethodName(handlerMethod.getMethod().getName());
            }
            exLog.setUserId(sessionUser.getUserId());
            exLog.setUserAccount(sessionUser.getAccount());
            exLog.setUserName(sessionUser.getUserInfo());
            exLog.setTenantCode(sessionUser.getTenantCode());
            exLog.setHost(HttpUtils.getIpAddress(request) + ":" + request.getRemotePort()
                    + " > " + ContextUtil.getHost() + ":" + request.getServerPort());
            exLog.setRequestUrl(request.getRequestURI());
            exLog.setRequestData(JsonUtils.toJson(request.getParameterMap()));

            producer.sendAsyn(UUID.randomUUID().toString(), exLog);
        } finally {
            logger.error(stackTrace);
        }
    }*/
}
