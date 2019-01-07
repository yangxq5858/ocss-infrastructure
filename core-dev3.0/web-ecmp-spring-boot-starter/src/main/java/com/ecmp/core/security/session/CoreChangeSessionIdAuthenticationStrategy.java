package com.ecmp.core.security.session;

import com.ecmp.context.ContextUtil;
import com.ecmp.core.common.WebShareHandle;
import com.ecmp.core.security.CoreAuthenticationToken;
import com.ecmp.vo.SessionUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionEvent;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

/**
 * Spring security 登录成功后，防止session的固化攻击，会将旧的sessionId销毁，
 * 重新生成一个新的sessionId, 因此此处需要做一下处理 ,
 * 重写 ChangeSessionIdAuthenticationStrategy 增加customOnAuthentication自定义处理逻辑
 * @see org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy
 * @author Vision.Mac
 * @version 1.0.1 2019/1/6 15:45
 */
public class CoreChangeSessionIdAuthenticationStrategy implements
        SessionAuthenticationStrategy, ApplicationEventPublisherAware {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Used for publishing events related to session fixation protection, such as
     * {@link SessionFixationProtectionEvent}.
     */
    private ApplicationEventPublisher applicationEventPublisher = new CoreChangeSessionIdAuthenticationStrategy.NullEventPublisher();
    /**
     * If set to {@code true}, a session will always be created, even if one didn't exist
     * at the start of the request. Defaults to {@code false}.
     */
    private boolean alwaysCreateSession;

    private final Method changeSessionIdMethod;

    public CoreChangeSessionIdAuthenticationStrategy() {
        Method changeSessionIdMethod = ReflectionUtils
                .findMethod(HttpServletRequest.class, "changeSessionId");
        if (changeSessionIdMethod == null) {
            throw new IllegalStateException(
                    "HttpServletRequest.changeSessionId is undefined. Are you using a Servlet 3.1+ environment?");
        }
        this.changeSessionIdMethod = changeSessionIdMethod;
    }

    /**
     * Called when a user is newly authenticated.
     * <p>
     * If a session already exists, and matches the session Id from the client, a new
     * session will be created, and the session attributes copied to it (if
     * {@code migrateSessionAttributes} is set). If the client's requested session Id is
     * invalid, nothing will be done, since there is no need to change the session Id if
     * it doesn't match the current session.
     * <p>
     * If there is no session, no action is taken unless the {@code alwaysCreateSession}
     * property is set, in which case a session will be created if one doesn't already
     * exist.
     */
    public void onAuthentication(Authentication authentication,
                                 HttpServletRequest request, HttpServletResponse response) {
        boolean hadSessionAlready = request.getSession(false) != null;

        if (!hadSessionAlready && !alwaysCreateSession) {
            // Session fixation isn't a problem if there's no session
            return;
        }

        // Create new session if necessary
        HttpSession session = request.getSession();

        if (hadSessionAlready && request.isRequestedSessionIdValid()) {

            String originalSessionId;
            String newSessionId;
            Object mutex = WebUtils.getSessionMutex(session);
            synchronized (mutex) {
                // We need to migrate to a new session
                originalSessionId = session.getId();

                ReflectionUtils.invokeMethod(this.changeSessionIdMethod, request);
                session = request.getSession();
                newSessionId = session.getId();
            }

            if (originalSessionId.equals(newSessionId)) {
                logger.warn("Your servlet container did not change the session ID when a new session was created. You will"
                        + " not be adequately protected against session-fixation attacks");
            }

            //ecmp
            customOnAuthentication(originalSessionId, authentication, request, response);

            onSessionChange(originalSessionId, session, authentication);
        }
    }

    private void customOnAuthentication(String originalSessionId, Authentication authentication, HttpServletRequest request, HttpServletResponse response) throws SessionAuthenticationException {
        try {
            CoreAuthenticationToken authenticationToken = (CoreAuthenticationToken) authentication;
            SessionUser sessionUser = authenticationToken.getDetails();
            String account = authenticationToken.getPrincipal();
            logger.info("account：【{}】", account);

            HttpSession session = request.getSession();
            final String randomKey = session.getId();
            sessionUser.setSessionId(randomKey);
            final String token = ContextUtil.generateToken(sessionUser);
            logger.info("token：【{}】", token);
            //注入上下文
            ContextUtil.setSessionUser(sessionUser);


            // 存到redis
            WebShareHandle.removeToken(originalSessionId);
            WebShareHandle.setAccessToken(randomKey, token);

            response.setHeader(ContextUtil.AUTHORIZATION_KEY, "Bearer " + token);

            session.setAttribute(ContextUtil.REQUEST_TOKEN_KEY, randomKey);
            logger.info("session.id:【{}】", randomKey);
            session.setAttribute(WebShareHandle.SESSION_USER, sessionUser);

            CookieSerializer cookieSerializer = ContextUtil.getBean(CookieSerializer.class);
            cookieSerializer.writeCookieValue(new CookieSerializer.CookieValue(request, response, randomKey));
        } catch (Exception e) {
            throw new SessionAuthenticationException(e.getMessage());
        }
    }

    /**
     * Called when the session has been changed and the old attributes have been migrated
     * to the new session. Only called if a session existed to start with. Allows
     * subclasses to plug in additional behaviour. *
     * <p>
     * The default implementation of this method publishes a
     * {@link SessionFixationProtectionEvent} to notify the application that the session
     * ID has changed. If you override this method and still wish these events to be
     * published, you should call {@code super.onSessionChange()} within your overriding
     * method.
     *
     * @param originalSessionId the original session identifier
     * @param newSession the newly created session
     * @param auth the token for the newly authenticated principal
     */
    protected void onSessionChange(String originalSessionId, HttpSession newSession,
                                   Authentication auth) {
        applicationEventPublisher.publishEvent(new SessionFixationProtectionEvent(auth,
                originalSessionId, newSession.getId()));
    }

    /**
     * Sets the {@link ApplicationEventPublisher} to use for submitting
     * {@link SessionFixationProtectionEvent}. The default is to not submit the
     * {@link SessionFixationProtectionEvent}.
     *
     * @param applicationEventPublisher the {@link ApplicationEventPublisher}. Cannot be
     * null.
     */
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        Assert.notNull(applicationEventPublisher,
                "applicationEventPublisher cannot be null");
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void setAlwaysCreateSession(boolean alwaysCreateSession) {
        this.alwaysCreateSession = alwaysCreateSession;
    }

    protected static final class NullEventPublisher implements ApplicationEventPublisher {
        public void publishEvent(ApplicationEvent event) {
        }

        public void publishEvent(Object event) {
        }
    }
}
