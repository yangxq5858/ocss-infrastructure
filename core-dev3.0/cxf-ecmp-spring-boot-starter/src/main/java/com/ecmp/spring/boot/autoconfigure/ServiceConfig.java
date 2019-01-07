package com.ecmp.spring.boot.autoconfigure;

import com.ecmp.core.service.MonitorService;
import org.springframework.context.annotation.Bean;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/6/21 17:25
 */
public class ServiceConfig {

    @Bean
    public MonitorService monitorService() {
        return new MonitorService();
    }
}
