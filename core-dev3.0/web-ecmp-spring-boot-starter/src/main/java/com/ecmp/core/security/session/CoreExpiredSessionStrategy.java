package com.ecmp.core.security.session;

import com.ecmp.core.security.session.template.AbstractSessionStrategyTemplate;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * session过期（被T掉）的策略
 */
public class CoreExpiredSessionStrategy extends AbstractSessionStrategyTemplate implements SessionInformationExpiredStrategy {

    /**
     * @param invalidSessionUrl：session过期URL
     */
    public CoreExpiredSessionStrategy(String invalidSessionUrl) {
        super(invalidSessionUrl);
    }

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        super.onSessionInvalid(event.getRequest(), event.getResponse());
    }

    @Override
    protected boolean isConcurrency() {
        return true;
    }
}
