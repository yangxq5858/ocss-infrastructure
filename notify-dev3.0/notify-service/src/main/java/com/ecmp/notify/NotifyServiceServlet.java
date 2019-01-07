package com.ecmp.notify;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * <strong>实现功能:</strong>
 * <p>SpringBoot War Servlet</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2018-06-29 11:04
 */
public class NotifyServiceServlet extends SpringBootServletInitializer {
    /**
     * Configure the application. Normally all you would need to do is to add sources
     * (e.g. config classes) because other settings have sensible defaults. You might
     * choose (for instance) to add default command line arguments, or set an active
     * Spring profile.
     *
     * @param builder a builder for the application context
     * @return the application builder
     * @see SpringApplicationBuilder
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(NotifyServiceApplication.class);
    }
}
