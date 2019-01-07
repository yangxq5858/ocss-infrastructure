package com.ecmp.core.security.authentication;

import com.ecmp.core.common.WebShareHandle;
import com.ecmp.core.security.CoreAuthenticationToken;
import com.ecmp.core.security.CoreGrantedAuthority;
import com.ecmp.core.security.exception.MultiTenantException;
import com.ecmp.context.ContextUtil;
import com.ecmp.util.JsonUtils;
import com.ecmp.vo.LoginStatus;
import com.ecmp.vo.SessionUser;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Map;

/**
 * 自定义认证处理类
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2017/11/7 14:45
 */
public class CoreAuthenticationProvider implements AuthenticationProvider {
    private final LoginService loginService;

    public CoreAuthenticationProvider(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CoreAuthenticationToken token = (CoreAuthenticationToken) authentication;
        String tenantCode = token.getTenantCode();
        String username = token.getPrincipal();
        String password = token.getCredentials();

        SessionUser sessionUser;
        try {
            sessionUser = loginService.login(tenantCode, username, password);
        } catch (Exception e) {
            throw new AuthenticationServiceException("Basic userAccount/login服务异常", e);
        }

        if (sessionUser == null) {
            //登陆失败，用户名/密码无效！
            throw new UsernameNotFoundException(ContextUtil.getMessage("ecmp_web_00003"));
        } else if (LoginStatus.failure == sessionUser.getLoginStatus()) {
            //登陆失败，用户名/密码无效！
            throw new UsernameNotFoundException(ContextUtil.getMessage("ecmp_web_00003"));
        } else if (LoginStatus.multiTenant == sessionUser.getLoginStatus()) {
            //登陆失败，请输入租户信息！
            throw new MultiTenantException(ContextUtil.getMessage("ecmp_web_00004"));
        } else if (LoginStatus.frozen == sessionUser.getLoginStatus()) {
            //用户已被禁用
            throw new DisabledException(ContextUtil.getMessage("ecmp_web_00005"));
        } else if (LoginStatus.locked == sessionUser.getLoginStatus()) {
            //账号已被锁定
            throw new LockedException(ContextUtil.getMessage("ecmp_web_00006"));
        }
        /*else if (!userDetails.isAccountNonExpired()) {
            throw new AccountExpiredException("账号已过期");
        }*/
        try {
            //注入上下文
            ContextUtil.setSessionUser(sessionUser);

            //功能项权限
            Map<String, Map<String, String>> featureMaps = loginService.getUserAuthorizedFeatureMaps();

            WebShareHandle.setUserAuthorizedFeatureMap(sessionUser.getUserId(), JsonUtils.toJson(featureMaps));
            //权限
            ArrayList<CoreGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new CoreGrantedAuthority(sessionUser.getAuthorityPolicy().name(), featureMaps));
            //授权
            return new CoreAuthenticationToken(sessionUser, authorities);
        } catch (Exception e) {
            throw new AuthenticationServiceException("登录加载权限异常", e);
        }
    }

    /**
     * @param authentication 认证
     * @return 返回true后才会执行上面的authenticate方法, 这步能确保authentication能正确转换类型
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return CoreAuthenticationToken.class.equals(authentication);
    }
}
