package com.ecmp.core.security.authentication;

import com.ecmp.vo.SessionUser;

import java.util.Map;

/**
 * *************************************************************************************************
 * <br>
 * 实现功能：
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 版本          变更时间             变更人                     变更原因
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 1.0.00      2017/6/1 16:42      马超(Vision.Mac)                新建
 * <br>
 * *************************************************************************************************
 */
public interface LoginService {
    /**
     * 用户登陆
     *
     * @param tenantCode 租户代码
     * @param account    账号
     * @param password   密码（MD5散列值）
     * @return 用户信息
     */
    SessionUser login(String tenantCode, String account, String password);

    /**
     * 获取用户前端权限检查的功能项键值
     *
     * @return 功能项键值
     */
    Map<String, Map<String, String>> getUserAuthorizedFeatureMaps();

    /**
     * 获取用户前端权限检查的功能项键值
     *
     * @return 功能项键值
     */
    Map<String, Map<String, String>> getUserAuthorizedFeatureMaps(String userId);

    /**
     * 用户退出登录
     */
    void logout();
}
