package com.ecmp.core.security.session;

import com.ecmp.context.ContextUtil;
import com.ecmp.core.common.WebShareHandle;
import com.ecmp.core.security.CoreAuthenticationToken;
import com.ecmp.vo.SessionUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.session.web.http.CookieSerializer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/7/4 17:04
 */
public class CoreSessionAuthenticationStrategy implements SessionAuthenticationStrategy {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Performs Http session-related functionality when a new authentication occurs.
     *
     * @throws SessionAuthenticationException if it is decided that the authentication is
     *                                        not allowed for the session. This will typically be because the user has too many
     *                                        sessions open at once.
     */
    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) throws SessionAuthenticationException {
        try {
            CoreAuthenticationToken authenticationToken = (CoreAuthenticationToken) authentication;
            SessionUser sessionUser = authenticationToken.getDetails();
            String account = authenticationToken.getPrincipal();
            logger.info("account：【{}】", account);

            final String randomKey = request.getSession().getId();
            sessionUser.setSessionId(randomKey);
            final String token = ContextUtil.generateToken(sessionUser);
            //注入上下文
            ContextUtil.setSessionUser(sessionUser);

//            final String token = sessionUser.getAccessToken();
//            final String randomKey = jwtTokenUtil.getRandomKeyFromToken(token);
            logger.info("token：【{}】", token);

            // 判断是否开启允许多人同账号同时在线，若不允许的话则先删除之前的
//            if (securityProperties.getJwt().isPreventsLogin()) {
//                // T掉同账号已登录的用户token信息
//                batchDel(JwtRedisEnum.getTokenKey(account, "*"));
//                // 删除同账号已登录的用户认证信息
//                batchDel(JwtRedisEnum.getAuthenticationKey(account, "*"));
//            }

            // 存到redis
            WebShareHandle.setAccessToken(randomKey, token);

            response.setHeader(ContextUtil.AUTHORIZATION_KEY, "Bearer " + token);

            HttpSession session = request.getSession();
            session.setAttribute(ContextUtil.REQUEST_TOKEN_KEY, randomKey);
            logger.info("session.id:【{}】", randomKey);
            session.setAttribute(WebShareHandle.SESSION_USER, sessionUser);

            CookieSerializer cookieSerializer = ContextUtil.getBean(CookieSerializer.class);
            cookieSerializer.writeCookieValue(new CookieSerializer.CookieValue(request, response, randomKey));
        } catch (Exception e) {
            throw new SessionAuthenticationException(e.getMessage());
        }
    }

//    /**
//     * 批量删除redis的key
//     *
//     * @param key：redis-key
//     */
//    private void batchDel(String key) {
//        Set<String> set = redisTemplate.keys(key);
//        if (CollectionUtils.isNotEmpty(set)) {
//            for (String keyStr : set) {
//                logger.info("keyStr【{}】", keyStr);
//                redisTemplate.delete(keyStr);
//            }
//        }
//    }
}
