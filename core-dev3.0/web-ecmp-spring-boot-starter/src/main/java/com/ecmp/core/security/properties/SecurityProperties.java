package com.ecmp.core.security.properties;

import com.ecmp.core.security.properties.authentication.AuthenticationProperties;
import com.ecmp.core.security.properties.authorize.AuthorizeProperties;
import com.ecmp.core.security.properties.code.ValidateCodeProperties;
import com.ecmp.core.security.properties.jwt.JwtProperties;
import com.ecmp.core.security.properties.logout.LogoutProperties;
import com.ecmp.core.security.properties.rememberme.RememberMeProperties;
import com.ecmp.core.security.properties.session.SessionProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 总的Core模块Security配置类
 */
@ConfigurationProperties(prefix = "com.ecmp.core.security")
public class SecurityProperties {
    /**
     * 验证码配置
     */
    private ValidateCodeProperties code = new ValidateCodeProperties();
    /**
     * 退出登录基本配置
     */
    private LogoutProperties logout = new LogoutProperties();
    /**
     * 记住我基本配置
     */
    private RememberMeProperties rememberme = new RememberMeProperties();
    /**
     * session的基本配置
     */
    private SessionProperties session = new SessionProperties();
    /**
     * jwt的基本配置
     */
    private JwtProperties jwt = new JwtProperties();
    /**
     * 权限配置
     */
    private AuthorizeProperties authorize = new AuthorizeProperties();

    /**
     * 认证模块配置
     */
    private AuthenticationProperties authentication = new AuthenticationProperties();

    public ValidateCodeProperties getCode() {
        return code;
    }

    public void setCode(ValidateCodeProperties code) {
        this.code = code;
    }

    public LogoutProperties getLogout() {
        return logout;
    }

    public void setLogout(LogoutProperties logout) {
        this.logout = logout;
    }

    public RememberMeProperties getRememberme() {
        return rememberme;
    }

    public void setRememberme(RememberMeProperties rememberme) {
        this.rememberme = rememberme;
    }

    public SessionProperties getSession() {
        return session;
    }

    public void setSession(SessionProperties session) {
        this.session = session;
    }

    public JwtProperties getJwt() {
        return jwt;
    }

    public void setJwt(JwtProperties jwt) {
        this.jwt = jwt;
    }

    public AuthorizeProperties getAuthorize() {
        return authorize;
    }

    public void setAuthorize(AuthorizeProperties authorize) {
        this.authorize = authorize;
    }

    public AuthenticationProperties getAuthentication() {
        return authentication;
    }

    public void setAuthentication(AuthenticationProperties authentication) {
        this.authentication = authentication;
    }
}
