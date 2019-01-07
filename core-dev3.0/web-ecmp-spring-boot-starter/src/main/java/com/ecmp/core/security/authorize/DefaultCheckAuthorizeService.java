package com.ecmp.core.security.authorize;

import com.ecmp.context.ContextUtil;
import com.ecmp.core.common.WebShareHandle;
import com.ecmp.enums.UserAuthorityPolicy;
import com.ecmp.vo.SessionUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/7/2 23:18
 */
public class DefaultCheckAuthorizeService implements CheckAuthorizeService {

//    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 权限检查
     *
     * @return 返回true时，允许访问
     */
    @Override
    public boolean hasPermission(HttpServletRequestWrapper request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String servletPath = request.getServletPath();
        if (Objects.equals(RequestMethod.OPTIONS.toString(), request.getMethod())) {
            return true;
        }

        SessionUser sessionUser = ContextUtil.getSessionUser();
        if (sessionUser == null || sessionUser.isAnonymous()) {
            return false;
        }

        //全局管理员不受权限控制
        if (UserAuthorityPolicy.GlobalAdmin.equals(sessionUser.getAuthorityPolicy())) {
            return true;
        }
        //租户管理员对分配给的应用模块功能不受权限控制
        if (UserAuthorityPolicy.TenantAdmin.equals(sessionUser.getAuthorityPolicy())) {
            return true;
        }

//        for (String permitUrl : WebSecurityCoreConfig.unauthorizedUrlSet) {
//            if (antPathMatcher.match(permitUrl, servletPath)) {
//                return true;
//            }
//        }
//
//        for (String permitUrl : WebSecurityCoreConfig.forbiddenUrlSet) {
//            if (antPathMatcher.match(permitUrl, servletPath)) {
//                return true;
//            }
//        }

        //获取当前模块的功能项键值对(key=功能项代码，value=功能项uri)
        Map<String, String> userAuthorizedFeatureMaps = WebShareHandle.getUserAuthorizedFeatureMap(sessionUser.getUserId(), ContextUtil.getAppCode());
        if (userAuthorizedFeatureMaps != null && !userAuthorizedFeatureMaps.isEmpty()) {
        /*
            依据约定获取实体功能基路径，如请求 http://127.0.0.1/user/show，基路径为 /user
            目的是获取实体功能的授权功能项代码，供前端页面控制使用
         */
//          String urlTemp;
            Map<String, String> resultMap = new HashMap<>();
            for (Map.Entry<String, String> entry : userAuthorizedFeatureMaps.entrySet()) {
                resultMap.put(entry.getKey(), "1");
            }

            //前端页面需要 base.html
            request.setAttribute(WebShareHandle.REQUEST_FEATURE_MAPS, resultMap);

            String tem;
            String[] servletArr = servletPath.split("[/]");
            String servletTem = StringUtils.join(servletArr);
            for (String uri : userAuthorizedFeatureMaps.values()) {
                String[] arr = uri.split("[/]");
                if (uri.contains("?")) {
                    if (arr.length > servletArr.length) {
                        for (int i = 0; i < servletArr.length; i++) {
                            if (servletArr[i].equals(arr[i])) {
                                return true;
                            }
                        }
                    }
                } else {
                    tem = StringUtils.join(arr);
                    if (servletTem.equals(tem)) {
                        return true;
                    }
                }
            }

//            isMatch = userAuthorizedFeatureMaps.values().stream().anyMatch(uri ->
//                    StringUtils.equalsAny(servletPath, "/" + uri, uri, uri + "/", "/" + uri + "/")
//                            || StringUtils.equalsAny(uri, "/" + servletPath, servletPath, servletPath + "/", "/" + servletPath + "/")
//                            || (uri.contains("?") && ("/" + uri).contains(servletPath))
//            );
        }
        return false;
    }
}
