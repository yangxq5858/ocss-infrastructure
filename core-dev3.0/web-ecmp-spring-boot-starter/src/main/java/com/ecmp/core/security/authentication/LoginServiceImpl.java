package com.ecmp.core.security.authentication;

import com.ecmp.config.util.ApiClient;
import com.ecmp.context.ContextUtil;
import com.ecmp.context.common.ConfigConstants;
import com.ecmp.log.util.LogUtil;
import com.ecmp.util.JsonUtils;
import com.ecmp.vo.SessionUser;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
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
@SuppressWarnings("unchecked")
public class LoginServiceImpl implements LoginService {
    /**
     * 用户登陆
     *
     * @param tenantCode 租户代码
     * @param account    账号
     * @param password   密码（MD5散列值）
     * @return 用户信息
     */
    @Override
    public SessionUser login(String tenantCode, String account, String password) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("appId", ContextUtil.getAppId());
        params.put("tenantCode", tenantCode);
        params.put("account", account);
        params.put("password", DigestUtils.md5Hex(password));
        String path = "userAccount/login";
        return ApiClient.postViaProxyReturnResult(ConfigConstants.BASIC_API, path, SessionUser.class, params);
    }

    /**
     * 获取用户前端权限检查的功能项键值
     *
     * @return 功能项键值
     */
    @Override
    public Map<String, Map<String, String>> getUserAuthorizedFeatureMaps() {
        return getUserAuthorizedFeatureMaps(ContextUtil.getUserId());
    }

    /**
     * 获取用户前端权限检查的功能项键值
     *
     * @return 功能项键值
     */
    @Override
    public Map<String, Map<String, String>> getUserAuthorizedFeatureMaps(String userId) {
        Map<String, Object> params = new HashMap<String, Object>();
        String path = "user/getUserAuthorizedFeatureMaps";
        params.put("userId", userId);
        return ApiClient.getEntityViaProxy(ConfigConstants.BASIC_API, path, HashMap.class, params);
    }

    /**
     * 用户退出登录
     */
    @Override
    public void logout() {
        // 输出当前用户
        LogUtil.bizLog("退出用户："+ JsonUtils.toJson(ContextUtil.getSessionUser()));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", ContextUtil.getUserId());
        String path = "userAccount/logout";
        ApiClient.postViaProxyReturnResult(ConfigConstants.BASIC_API, path, Boolean.class, params);
    }
}
