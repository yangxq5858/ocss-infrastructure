package com.ecmp.core.security.authentication;

import com.ecmp.core.security.enums.LoginTypeEnum;
import com.ecmp.core.security.properties.SecurityProperties;
import com.ecmp.core.vo.OperateStatus;
import com.ecmp.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 自定义成功处理器
 * 为什么不实现接口，而是继承SavedRequestAwareAuthenticationSuccessHandler类的方式？
 * 因为SavedRequestAwareAuthenticationSuccessHandler这个类记住了你上一次的请求路径，比如：
 * 你请求user.html。然后被拦截到了登录页，这时候你输入完用户名密码点击登录，会自动跳转到user.html，而不是主页面。
 * 若是前后分离项目则实现接口即可，因为我弄的是通用的权限组件，所以选择了继承
 */
public class CoreAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        logger.info("验证成功！");
        if (Objects.equals(securityProperties.getAuthentication().getLoginType(), LoginTypeEnum.JSON)) {
            OperateStatus result = new OperateStatus(
                    true, HttpStatus.OK.getReasonPhrase()
            ).data(authentication);

            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JsonUtils.toJson(result));
        } else {
            // 存到session里，方便取值。
            //request.getSession().setAttribute("authentication", authentication);
            logger.info("session.authentication:【{}】", JsonUtils.toJson(request.getSession().getAttribute("authentication")));
            // 支持跳转到自定义页面
            if (StringUtils.isNotBlank(securityProperties.getAuthentication().getLoginSuccessPage())) {
                redirectStrategy.sendRedirect(request, response, securityProperties.getAuthentication().getLoginSuccessPage());
            } else {
                // 会帮我们跳转到上一次请求的页面上
                super.onAuthenticationSuccess(request, response, authentication);
            }
        }
    }
}
