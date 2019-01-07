package com.ecmp.core.service;

import com.ecmp.core.api.IMonitorService;
import com.ecmp.util.DateUtils;
import com.ecmp.vo.ResponseData;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.ws.rs.Path;
import java.util.Date;

/**
 * usage:监控服务
 * <p>
 * </p>
 * User:liusonglin; Date:2018/6/13;ProjectName:ecmp-core;
 */
public class MonitorService implements IMonitorService {

    @Override
    public ResponseData health() {
        String msg = DateUtils.formatTime(new Date()) + " Request Uri: " + MDC.get("requestUrl");
        System.out.println(msg);
        return ResponseData.build().setMessage("OK");
    }
}
