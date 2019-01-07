package com.ecmp.core.aop.aspect;

import com.ecmp.context.ContextUtil;
import com.ecmp.util.JsonUtils;
import com.ecmp.vo.SessionUser;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;

/**
 * <strong>实现功能:</strong>
 * <p>
 * AOP方式收集异常日志
 * </p>
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2017/4/18 21:39
 */
@Aspect
@Order(3)//使用@Order注解指定切面的优先级，值越小优先级越高
//@Component
public class ServiceLogCollection {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceLogCollection.class);
//    private static MqProducer<ExceptionLog> producer = new MqProducer<ExceptionLog>(ExceptionLog.EXCEPTION_TOPIC);

    /**
     * @within(org.springframework.stereotype.Service) 拦截带有 @Service 注解的类的所有方法
     * 方法开始之前执行
     */
    @Before("com.ecmp.core.aop.pointcut.Pointcuts.loggerPointcut()")
    public void before(JoinPoint joinPoint) {
        SessionUser sessionUser = ContextUtil.getSessionUser();
        MDC.put("userId", sessionUser.getUserId());
        MDC.put("account", sessionUser.getAccount());
        MDC.put("userName", sessionUser.getUserName());
        MDC.put("tenantCode", sessionUser.getTenantCode());
        MDC.put("accessToken", sessionUser.getAccessToken());

        String argJson = " ";
        Object[] args = joinPoint.getArgs();
        try {
            argJson = args != null ? JsonUtils.toJson(args) : " ";
            MDC.put("args", argJson);
        } catch (Exception ignored) {
        }
        if (LOGGER.isDebugEnabled()) {
            String methodName = joinPoint.getSignature().getName();
            LOGGER.debug("\n\r用户:{}\n\r方法:{}\n\r参数:{}\n\rToken:{}",
                    sessionUser.toString(), methodName, argJson, sessionUser.getAccessToken());
        }
    }
//
//    /**
//     * @within(org.springframework.stereotype.Service) 拦截带有 @Service 注解的类的所有方法
//     * 方法执行之后执行
//     * 注：无论该方法是否出现异常都会执行
//     */
//    @After("servicePointcut()")
//    public void after(JoinPoint joinPoint) {
//        String methodName = joinPoint.getSignature().getName();
//        Object[] args = joinPoint.getArgs();
//        LOGGER.debug("The method " + methodName + " ends with " + Arrays.asList(args));
//    }
//
//    /**
//     * @within(org.springframework.stereotype.Service) 拦截带有 @Service 注解的类的所有方法
//     * 方法正常结束后执行的代码
//     * 返回通知是可以访问到方法的返回值的
//     */
//    @AfterReturning(value = "servicePointcut()", returning = "result")
//    public void afterReturning(JoinPoint joinPoint, Object result) {
//        String methodName = joinPoint.getSignature().getName();
//        LOGGER.debug("The method " + methodName + " return with " + result);
//    }

    /**
     * @param joinPoint 常用方法:
     *                  Object[] getArgs(): 返回执行目标方法时的参数。
     *                  Signature getSignature(): 返回被增强的方法的相关信息。
     *                  Object getTarget(): 返回被织入增强处理的目标对象。
     *                  Object getThis(): 返回 AOP 框架为目标对象生成的代理对象。
     * @param throwable 异常对象
     *                  拦截service层异常，记录异常日志，并设置对应的异常信息
     */
    @AfterThrowing(pointcut = "com.ecmp.core.aop.pointcut.Pointcuts.loggerPointcut()", throwing = "throwable")
    public void afterThrowing(JoinPoint joinPoint, Throwable throwable) throws Exception {
        LOGGER.error(ExceptionUtils.getMessage(throwable), throwable);
    }
}
