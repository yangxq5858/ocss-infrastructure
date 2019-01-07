package com.ecmp.core.security.session;

import com.ecmp.core.security.session.template.AbstractSessionStrategyTemplate;
import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * session失效策略
 *
 * @author Vision.Mac 2018-06-06 13:45
 */
public class CoreInvalidSessionStrategy extends AbstractSessionStrategyTemplate implements InvalidSessionStrategy {

    /**
     * @param invalidSessionUrl ：session失效URL
     */
    public CoreInvalidSessionStrategy(String invalidSessionUrl) {
        super(invalidSessionUrl);
    }

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        super.onSessionInvalid(request, response);
    }
}
