package com.ecmp.core.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * http工具类
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2017/5/15 23:39
 */
public class HttpUtils {

    private HttpUtils() {
    }

    /**
     * 是否是异步请求
     */
    public static boolean isAsync(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    /**
     * 读取指定cookie
     * 从客户端读取Cookie时，包括maxAge在内的其他属性都是不可读的，也不会被提交。
     * 浏览器提交Cookie时只会提交name与value属性。maxAge属性只被浏览器用来判断Cookie是否过期
     */
    public static Cookie getCookieByName(HttpServletRequest request, String cookieName) {
        Cookie cookie = null;
        if (StringUtils.isNotBlank(cookieName)) {
            //获取一个cookie数组
            Cookie[] cookies = request.getCookies();
            if (null == cookies) {
                return null;
            } else {
                for (Cookie ck : cookies) {
                    if (cookieName.equals(ck.getName())) {
                        cookie = ck;
                        break;
                    }
                }
            }
        }
        return cookie;
    }

    public static String getCookieValueByName(HttpServletRequest request, String cookieName) {
        String value = null;
        Cookie cookie = getCookieByName(request, cookieName);
        if (cookie != null) {
            value = cookie.getValue();
        }
        return value;
    }

    /**
     * 添加cookie
     */
    public static void addCookie(HttpServletResponse response, String name, String value, String domain, String path) {
        Cookie cookie = new Cookie(name.trim(), value.trim());
        // todo 设置为30min
        cookie.setMaxAge(30 * 60);
        cookie.setDomain(domain);
        cookie.setPath(path);
        response.addCookie(cookie);
    }

    /**
     * 修改cookie
     * 修改、删除Cookie时，新的Cookie除value、maxAge之外的所有属性，
     * 例如name、path、domain等，都要与原Cookie完全一样。
     * 否则，浏览器将视为两个不同的Cookie不予覆盖，导致修改、删除失败。
     */
    public static void editCookie(HttpServletRequest request, HttpServletResponse response, String name, String value) {
        Cookie cookie = getCookieByName(request, name);
        if (cookie != null) {
            cookie.setValue(value);
            // 设置为30min
            cookie.setMaxAge(30 * 60);
            response.addCookie(cookie);
        }
    }

    /**
     * 删除cookie
     */
    public static void delCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie cookie = getCookieByName(request, name);
        if (cookie != null) {
            cookie.setValue(null);
            // 立即销毁cookie
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     */
    public static String getIpAddress(HttpServletRequest request) {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (String ip1 : ips) {
                if (!("unknown".equalsIgnoreCase(ip1))) {
                    ip = ip1;
                    break;
                }
            }
        }
        return ip;
    }
}
