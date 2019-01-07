package com.ecmp.core.controller;

import com.ecmp.annotation.IgnoreAuthentication;
import com.ecmp.annotation.IgnoreCheckAuth;
import com.ecmp.util.DateUtils;
import com.ecmp.vo.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <strong>实现功能:</strong>
 * <p>公共控制器</p>
 *
 * @author <a href="mailto:chao2.ma@changhong.com">马超(Vision.Mac)</a>
 * @version 1.0.1 2017/5/25 16:55
 */
@Controller
@IgnoreAuthentication
@SuppressWarnings("unused")
public class CommonController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonController.class);

    /**
     * 未认证
     *
     * @return 返回未认证401页面
     */
    @IgnoreCheckAuth
//    @RequestMapping(value = WebShareHandle.UNAUTHORIZED_URL)
    @RequestMapping(value = "/unauthorized")
    public String unauthorized() {
        return "error/401";
    }

    /**
     * 无权限访问
     *
     * @return 返回无权限403页面
     */
    @IgnoreCheckAuth
//    @RequestMapping(value = WebShareHandle.FORBIDDEN_URL)
    @RequestMapping(value = "/forbidden")
    public String forbidden() {
        return "error/403";
    }

    /**
     * 监控服务是否健康
     */
    @IgnoreCheckAuth
    @ResponseBody
    @RequestMapping("/monitor/health")
    public ResponseData health(HttpServletRequest request) {
        String msg = DateUtils.formatTime(new Date()) + " Request Uri: " + request.getRequestURL();
        System.out.println(msg);
        return ResponseData.build().setMessage("OK");
    }

}
