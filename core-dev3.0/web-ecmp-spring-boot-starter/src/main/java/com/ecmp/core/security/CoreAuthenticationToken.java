package com.ecmp.core.security;

import com.ecmp.vo.SessionUser;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2017/11/7 15:51
 */
public class CoreAuthenticationToken implements Authentication, Serializable {
    private static final long serialVersionUID = 4890339016491736832L;
    private String tenantCode;
    private String account;
    private String password;
    private SessionUser user;
    private ArrayList<? extends GrantedAuthority> authorities;

    /**
     * This constructor should only be used by <code>AuthenticationManager</code> or
     * <code>AuthenticationProvider</code> implementations that are satisfied with
     * producing a trusted (i.e. {@link #isAuthenticated()} = <code>true</code>)
     * authentication token.
     *
     * @param tenantCode 租户代码
     * @param account    账号
     * @param password   密码
     */
    public CoreAuthenticationToken(String tenantCode, String account, String password) {
        this.tenantCode = tenantCode;
        this.account = account;
        this.password = password;
    }

    /**
     * This constructor should only be used by <code>AuthenticationManager</code> or
     * <code>AuthenticationProvider</code> implementations that are satisfied with
     * producing a trusted (i.e. {@link #isAuthenticated()} = <code>true</code>)
     * authentication token.
     * <p>
     * //     * @param tenantCode  租户代码
     * //     * @param account     账号
     * //     * @param password    密码
     *
     * @param authorities 权限集合
     */
    @JsonCreator
    public CoreAuthenticationToken(@JsonProperty("user") SessionUser user,
                                   @JsonProperty("authorities") ArrayList<CoreGrantedAuthority> authorities) {
        this.tenantCode = user.getTenantCode();
        this.account = user.getAccount();
        this.user = user;
        this.authorities = authorities;
    }

    @JsonIgnore(true)
    public String getTenantCode() {
        return tenantCode;
    }

    @JsonIgnore(true)
    public String getAccount() {
        return account;
    }

    @Override
    @JsonIgnore(true)
    public String getCredentials() {
        return password;
    }

    @Override
    @JsonIgnore(true)
    public String getPrincipal() {
        return account;
    }

    @Override
    @JsonProperty("user")
    public SessionUser getDetails() {
        return user;
    }

    /**
     * Set by an <code>AuthenticationManager</code> to indicate the authorities that the
     * principal has been granted. Note that classes should not rely on this value as
     * being valid unless it has been set by a trusted <code>AuthenticationManager</code>.
     * <p>
     * Implementations should ensure that modifications to the returned collection array
     * do not affect the state of the Authentication object, or use an unmodifiable
     * instance.
     * </p>
     *
     * @return the authorities granted to the principal, or an empty collection if the
     * token has not been authenticated. Never null.
     */
    @Override
    @JsonProperty("authorities")
    public ArrayList<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Used to indicate to {@code AbstractSecurityInterceptor} whether it should present
     * the authentication token to the <code>AuthenticationManager</code>. Typically an
     * <code>AuthenticationManager</code> (or, more often, one of its
     * <code>AuthenticationProvider</code>s) will return an immutable authentication token
     * after successful authentication, in which case that token can safely return
     * <code>true</code> to this method. Returning <code>true</code> will improve
     * performance, as calling the <code>AuthenticationManager</code> for every request
     * will no longer be necessary.
     * <p>
     * For security reasons, implementations of this interface should be very careful
     * about returning <code>true</code> from this method unless they are either
     * immutable, or have some way of ensuring the properties have not been changed since
     * original creation.
     *
     * @return true if the token has been authenticated and the
     * <code>AbstractSecurityInterceptor</code> does not need to present the token to the
     * <code>AuthenticationManager</code> again for re-authentication.
     */
    @Override
    @JsonIgnore(true)
    public boolean isAuthenticated() {
        return Objects.nonNull(user) && Objects.nonNull(user.getAccessToken());
    }

    /**
     * See {@link #isAuthenticated()} for a full description.
     * <p>
     * Implementations should <b>always</b> allow this method to be called with a
     * <code>false</code> parameter, as this is used by various classes to specify the
     * authentication token should not be trusted. If an implementation wishes to reject
     * an invocation with a <code>true</code> parameter (which would indicate the
     * authentication token is trusted - a potential security risk) the implementation
     * should throw an {@link IllegalArgumentException}.
     *
     * @param isAuthenticated <code>true</code> if the token should be trusted (which may
     *                        result in an exception) or <code>false</code> if the token should not be trusted
     * @throws IllegalArgumentException if an attempt to make the authentication token
     *                                  trusted (by passing <code>true</code> as the argument) is rejected due to the
     *                                  implementation being immutable or implementing its own alternative approach to
     *                                  {@link #isAuthenticated()}
     */
    @Override
    @JsonIgnore(true)
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
    }

    /**
     * Returns the name of this principal.
     *
     * @return the name of this principal.
     */
    @Override
    @JsonIgnore(true)
    public String getName() {
        return account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CoreAuthenticationToken)) {
            return false;
        }
        CoreAuthenticationToken that = (CoreAuthenticationToken) o;
        return isAuthenticated() == that.isAuthenticated() &&
                Objects.equals(getTenantCode(), that.getTenantCode()) &&
                Objects.equals(getAccount(), that.getAccount()) &&
                Objects.equals(getCredentials(), that.getCredentials()) &&
                Objects.equals(getDetails(), that.getDetails()) &&
                Objects.equals(getAuthorities(), that.getAuthorities());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTenantCode(), getAccount(), getCredentials(), getDetails(), getAuthorities(), isAuthenticated());
    }
}
