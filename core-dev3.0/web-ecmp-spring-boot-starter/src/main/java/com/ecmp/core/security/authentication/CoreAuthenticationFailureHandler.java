package com.ecmp.core.security.authentication;

import com.ecmp.core.security.enums.LoginTypeEnum;
import com.ecmp.core.security.properties.SecurityProperties;
import com.ecmp.core.vo.OperateStatus;
import com.ecmp.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 自定义失败处理器
 */
public class CoreAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        logger.error("验证失败！", exception);

        if (Objects.equals(securityProperties.getAuthentication().getLoginType(), LoginTypeEnum.JSON)) {
            OperateStatus result = new OperateStatus(
                    false, exception.getMessage()
            ).data(exception.getClass().getSimpleName());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JsonUtils.toJson(result));
        } else {
            response.setContentType("text/html;charset=UTF-8");
            // 判断是否有自定义错误页面,有则失败跳转到自定义页面。
            if (StringUtils.isNotBlank(securityProperties.getAuthentication().getLoginErrorPage())) {
                // 将异常信息存到session对象里，若前后不分离的话，则可以从中取出异常信息展示到页面上。
                // 若前后分离，建议采取JSON的方式。
                request.getSession().setAttribute("exception", exception);
                redirectStrategy.sendRedirect(request, response, securityProperties.getAuthentication().getLoginErrorPage());
            } else {
                super.onAuthenticationFailure(request, response, exception);
            }
        }
    }
}
