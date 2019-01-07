package com.ecmp.core.security.jwt;

import com.ecmp.context.ContextUtil;
import com.ecmp.core.common.WebShareHandle;
import com.ecmp.core.security.CoreAuthenticationToken;
import com.ecmp.core.security.CoreGrantedAuthority;
import com.ecmp.core.security.authentication.LoginService;
import com.ecmp.core.security.enums.JwtRedisEnum;
import com.ecmp.core.security.enums.JwtUrlEnum;
import com.ecmp.core.security.properties.SecurityProperties;
import com.ecmp.core.util.HttpUtils;
import com.ecmp.core.vo.OperateStatus;
import com.ecmp.enums.UserAuthorityPolicy;
import com.ecmp.util.IdGenerator;
import com.ecmp.util.JsonUtils;
import com.ecmp.util.JwtTokenUtil;
import com.ecmp.vo.SessionUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * JWT过滤器
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private String[] ignoringWebSecurity;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private LoginService loginService;
    /**
     * 重定向策略
     */
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void destroy() {
        ContextUtil.cleanUserToken();
        MDC.clear();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if (logger.isDebugEnabled()) {
            logger.debug("请求路径：【{}】，请求方式为：【{}】", servletPath, request.getMethod());
        }
        //如果是options请求是cors跨域预请求，设置allow对应头信息
        if (Objects.equals(RequestMethod.OPTIONS.toString(), request.getMethod())) {
            if (logger.isDebugEnabled()) {
                logger.debug("jwt不拦截此路径：【{}】，请求方式为：【{}】", servletPath, request.getMethod());
            }
            filterChain.doFilter(request, response);
            return;
        }

        // 排除路径
        if (ignoringWebSecurity != null && ignoringWebSecurity.length > 0) {
            for (String permitUrl : ignoringWebSecurity) {
                if (antPathMatcher.match(permitUrl, servletPath)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("jwt不拦截此路径：【{}】，请求方式为：【{}】", servletPath, request.getMethod());
                    }
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        }

        if (!securityProperties.getJwt().isPreventsGetMethod()) {
            if (Objects.equals(RequestMethod.GET.toString(), request.getMethod())) {
                if (logger.isDebugEnabled()) {
                    logger.debug("jwt不拦截此路径因为开启了不拦截GET请求：【{}】，请求方式为：【{}】", servletPath, request.getMethod());
                }
                filterChain.doFilter(request, response);
                return;
            }
        }

        //获取traceLogId
        Object traceId = request.getAttribute("traceId");
        if (Objects.isNull(traceId)) {
            traceId = IdGenerator.uuid2();
            request.setAttribute("traceId", traceId);
        }
        MDC.put("traceId", String.valueOf(traceId));

        String authToken = null;
        HttpSession session = request.getSession();
        // 获取Authorization
        String authHeader = request.getHeader(ContextUtil.AUTHORIZATION_KEY);
        if (StringUtils.isBlank(authHeader)) {
            String sid = request.getParameter(ContextUtil.REQUEST_TOKEN_KEY);
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
                authToken = WebShareHandle.getAccessToken(sid);
            }

            if (StringUtils.isBlank(authToken)) {
                SessionUser sessionUser = (SessionUser) session.getAttribute(WebShareHandle.SESSION_USER);
                if (sessionUser != null) {
                    authToken = sessionUser.getAccessToken();
                }
                if (StringUtils.isBlank(authToken)) {
                    //本地加载配置允许模拟
                    if (ContextUtil.isLocalConfig()) {
                        authToken = mockUserLogin(request, response);
                    } else {
                        //未认证
                        unAuthorized(request, response, "无有效token！");
                        return;
                    }
                }
            }
        } else {
            if (authHeader.startsWith("Bearer ")) {
                // 截取token
                authToken = authHeader.substring("Bearer ".length());
            } else {
                authToken = authHeader;
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("token: " + authToken);
        }

        // 判断token是否失效
        if (jwtTokenUtil.isTokenExpired(authToken)) {
            unAuthorized(request, response, "token已过期！");
            return;
        }

        String randomKey = jwtTokenUtil.getRandomKeyFromToken(authToken);
        String username = jwtTokenUtil.getSubjectFromToken(authToken);

        // 验证token是否合法
        if (StringUtils.isBlank(username) || StringUtils.isBlank(randomKey)) {
            unAuthorized(request, response, "token不合法！username【" + username + "】或randomKey【" + randomKey + "】可能为null！");
            return;
        }

        // 验证token是否存在（过期了也会消失）
        String token = WebShareHandle.getAccessToken(randomKey);
        if (Objects.isNull(token)) {
            unAuthorized(request, response, "token已过期！Redis里没查到key【" + JwtRedisEnum.getTokenKey(randomKey) + "】对应的value！");
            return;
        }

        // 判断传来的token和存到redis的token是否一致
        if (!Objects.equals(token, authToken)) {
            unAuthorized(request, response, "token不合法！前端传来的token【" + authToken + "】和redis里的token【" + token + "】不一致！");
            return;
        }

        SessionUser sessionUser = ContextUtil.getSessionUser(authToken);

        MDC.put("userId", sessionUser.getUserId());
        MDC.put("account", sessionUser.getAccount());
        MDC.put("userName", sessionUser.getUserName());
        MDC.put("tenantCode", sessionUser.getTenantCode());
        MDC.put("accessToken", sessionUser.getAccessToken());

        if (!sessionUser.isAnonymous()) {
            //前端页面需要 base.html
            if (session.getAttribute(WebShareHandle.SESSION_USER) == null) {
                session.setAttribute(WebShareHandle.SESSION_USER, sessionUser);
            }

            //权限
            ArrayList<CoreGrantedAuthority> authorities = new ArrayList<>();
            if (UserAuthorityPolicy.GlobalAdmin != sessionUser.getAuthorityPolicy()) {
                //功能项权限
                Map<String, Map<String, String>> featureMaps = WebShareHandle.getUserAuthorizedFeatureMap(sessionUser.getUserId());
                if (featureMaps == null) {
                    featureMaps = loginService.getUserAuthorizedFeatureMaps();
                    WebShareHandle.setUserAuthorizedFeatureMap(sessionUser.getUserId(), JsonUtils.toJson(featureMaps));
                }
                authorities.add(new CoreGrantedAuthority(sessionUser.getAuthorityPolicy().name(), featureMaps));
            }
            CoreAuthenticationToken authentication = new CoreAuthenticationToken(sessionUser, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            //未认证
            filterChain.doFilter(request, response);
            return;
        }

        // token过期时间
        long tokenExpireTime = jwtTokenUtil.getExpirationDateFromToken(authToken).getTime();
        // token还剩余多少时间过期
        long surplusExpireTime = (tokenExpireTime - System.currentTimeMillis()) / 1000;
        if (logger.isDebugEnabled()) {
            logger.debug("surplusExpireTime:" + surplusExpireTime);
        }
        /*
         * 退出登录不刷新token，因为假设退出登录操作，刷新token了，这样清除的是旧的token，相当于根本没退出成功
         */
        if (!StringUtils.equals(request.getRequestURL(), JwtUrlEnum.LOGOUT.url())) {
            // token过期时间小于等于多少秒，自动刷新token
            if (surplusExpireTime <= securityProperties.getJwt().getAutoRefreshTokenExpiration()) {
                // 重新生成token，放到header以及redis
                String newAuthToken = jwtTokenUtil.refreshToken(authToken, randomKey);
                WebShareHandle.setAccessToken(randomKey, newAuthToken);

                // 新token放到cookie
                CookieSerializer cookieSerializer = ContextUtil.getBean(CookieSerializer.class);
                cookieSerializer.writeCookieValue(new CookieSerializer.CookieValue(request, response, randomKey));

                response.setHeader(ContextUtil.AUTHORIZATION_KEY, "Bearer " + newAuthToken);
                response.setHeader(ContextUtil.REQUEST_TOKEN_KEY, randomKey);

                // 删除之前的token
                WebShareHandle.removeToken(randomKey);
                if (logger.isDebugEnabled()) {
                    logger.debug("重新生成token【{}】和randomKey【{}】", newAuthToken, randomKey);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private void unAuthorized(HttpServletRequest request, HttpServletResponse response, String msg) {
        if (logger.isInfoEnabled()) {
            logger.info("URI:{} - {}", request.getServletPath(), msg);
        }
        //认证错误处理
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        try {
            //异步请求，认证错误处理
            if (HttpUtils.isAsync(request)) {
                response.setCharacterEncoding("UTF-8");
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
                writer.write(JsonUtils.toJson(new OperateStatus(Boolean.FALSE, msg, HttpStatus.UNAUTHORIZED.value())));
                writer.close();
            } else {
                //非异步请求，认证错误处理
                redirectStrategy.sendRedirect(request, response, securityProperties.getSession().getSessionInvalidUrl());
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            SecurityContextHolder.clearContext();
            request.getSession().invalidate();
        }
    }

    private String mockUserLogin(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        SessionUser sessionUser = ContextUtil.mockUser(session.getId());

        logger.debug("当前模拟用户：{}", sessionUser);

        // 存到redis
        WebShareHandle.setAccessToken(sessionUser.getSessionId(), sessionUser.getAccessToken());

        session.setAttribute(ContextUtil.REQUEST_TOKEN_KEY, sessionUser.getSessionId());

        CookieSerializer cookieSerializer = ContextUtil.getBean(CookieSerializer.class);
        cookieSerializer.writeCookieValue(new CookieSerializer.CookieValue(request, response, sessionUser.getSessionId()));

        return sessionUser.getAccessToken();
    }
}
