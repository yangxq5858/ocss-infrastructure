package com.ecmp.core.service;

import com.ecmp.core.api.IMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.ws.rs.Path;

/**
 * usage:监控服务
 * <p>
 * </p>
 * User:liusonglin; Date:2018/6/13;ProjectName:ecmp-core;
 */
public class MonitorService implements IMonitorService {

    @Autowired
    private ApplicationContext context;

    @Override
    public String[] health() {
        return context.getBeanNamesForAnnotation(Path.class);
    }
}
