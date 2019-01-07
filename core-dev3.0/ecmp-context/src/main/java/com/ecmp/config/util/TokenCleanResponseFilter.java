package com.ecmp.config.util;

import com.ecmp.context.ContextUtil;
import org.slf4j.MDC;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2017/11/17 13:49
 */
@Provider
//@Priority(value = 5)
public class TokenCleanResponseFilter implements ContainerResponseFilter {
    /**
     * Filter method called after a response has been provided for a request
     * (either by a {@link javax.ws.rs.container.ContainerRequestFilter request filter} or by a
     * matched resource method.
     * <p>
     * Filters in the filter chain are ordered according to their {@code javax.annotation.Priority}
     * class-level annotation value.
     * </p>
     *
     * @param requestContext  request context.
     * @param responseContext response context.
     * @throws IOException if an I/O exception occurs.
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        ContextUtil.cleanUserToken();
        MDC.clear();
    }
}
