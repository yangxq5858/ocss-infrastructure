package com.ecmp.core.security.authentication;

import com.ecmp.context.ContextUtil;
import com.ecmp.core.common.WebShareHandle;
import com.ecmp.core.security.properties.SecurityProperties;
import com.ecmp.core.vo.OperateStatus;
import com.ecmp.util.JsonUtils;
import com.ecmp.vo.SessionUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.session.web.http.CookieSerializer;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 退出登录成功处理器
 *
 * @author Vision.Mac 2018-06-04 16:27
 */
public class CoreLogoutSuccessHandler implements LogoutSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(CoreLogoutSuccessHandler.class);

    @Autowired
    private LoginService loginService;
    @Autowired
    private SecurityProperties securityProperties;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String sid = null;
        HttpSession session = request.getSession();
        String authToken = null;
        // 获取Authorization
        String authHeader = request.getHeader(ContextUtil.AUTHORIZATION_KEY);
        if (StringUtils.isBlank(authHeader)) {
            sid = request.getParameter(ContextUtil.REQUEST_TOKEN_KEY);
            if (StringUtils.isBlank(sid) || StringUtils.equalsAnyIgnoreCase(sid, "undefined", "null")) {
                sid = (String) session.getAttribute(ContextUtil.REQUEST_TOKEN_KEY);
                if (StringUtils.isBlank(sid) || StringUtils.equalsAnyIgnoreCase(sid, "undefined", "null")) {
                    CookieSerializer cookieSerializer = ContextUtil.getBean(CookieSerializer.class);
                    List<String> cookieValues = cookieSerializer.readCookieValues(request);
                    if (cookieValues != null && cookieValues.size() > 0) {
                        sid = cookieValues.get(0);
                    }
                }
            }

            if (StringUtils.isNotBlank(sid)) {
                //通过会话id换取token
                authToken = WebShareHandle.getAccessToken(sid);
            }
        } else {
            // 截取token
            if (authHeader.startsWith("Bearer ")) {
                authToken = authHeader.substring("Bearer ".length());
            } else {
                authToken = authHeader;
            }
        }

        String logoutSuccessUrl = null;
        try {
            if (StringUtils.isNotBlank(authToken)) {
                SessionUser sessionUser = ContextUtil.getSessionUser(authToken);
                //用户指定的退出地址
                logoutSuccessUrl = sessionUser.getLogoutUrl();

                loginService.logout();

                WebShareHandle.removeToken(sessionUser.getSessionId());
                //logger.info("删除【{}】成功", JwtRedisEnum.getTokenKey(sessionUser.getSessionId()));
            }
        } catch (Exception e) {
            if (StringUtils.isNotBlank(sid)) {
                WebShareHandle.removeToken(sid);
            }
        } finally {
            ContextUtil.cleanUserToken();

            session.invalidate();
            //只有cookie失效掉，才能换成新的sessionId
            if (request.getCookies() != null) {
                Cookie cookie = request.getCookies()[0];
                // 让cookie过期
                cookie.setMaxAge(0);
            }

            logger.info("退出成功");

            //如果用户未指定退出地址，则使用平台默认配置
            if (StringUtils.isBlank(logoutSuccessUrl)) {
                logoutSuccessUrl = securityProperties.getLogout().getLogoutSuccessUrl();
            }

            /*
             * 如果配置了退出到的页面，则跳转到自定义的页面上去。否则返回JSON
             */
            if (StringUtils.isNotBlank(logoutSuccessUrl)) {
                logger.info("退出成功，跳转到了【{}】", logoutSuccessUrl);
                redirectStrategy.sendRedirect(request, response, logoutSuccessUrl);
            } else {
                logger.info("退出成功，成功的返回了JSON！");
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().print(JsonUtils.toJson(new OperateStatus(true, "退出成功").data(null)));
            }
        }
    }
}
