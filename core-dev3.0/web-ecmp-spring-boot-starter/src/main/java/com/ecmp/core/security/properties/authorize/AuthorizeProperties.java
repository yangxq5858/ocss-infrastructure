package com.ecmp.core.security.properties.authorize;

/**
 * 权限模块配置
 */
public class AuthorizeProperties {

    /**
     * 无需权限即可访问的url
     */
    private String permitUrls;

    /**
     * 访问无权限的接口时，跳转到哪个页面
     */
    private String unAuthorizePage;

    /**
     * 限制ip在一定时间内访问多少次数的url，若不写此url，则不会生效，也相当于开关
     */
    private String ipValidateUrl;

    /**
     * 限制ip在多少秒内能访问同一url多少次的秒数
     */
    private int ipValidateSeconds = 3;

    /**
     * 限制ip在多少秒内能访问同一url的次数
     */
    private int ipValidateCount = 10;

    public String getPermitUrls() {
        return permitUrls;
    }

    public void setPermitUrls(String permitUrls) {
        this.permitUrls = permitUrls;
    }

    public String getUnAuthorizePage() {
        return unAuthorizePage;
    }

    public void setUnAuthorizePage(String unAuthorizePage) {
        this.unAuthorizePage = unAuthorizePage;
    }

    public String getIpValidateUrl() {
        return ipValidateUrl;
    }

    public void setIpValidateUrl(String ipValidateUrl) {
        this.ipValidateUrl = ipValidateUrl;
    }

    public int getIpValidateSeconds() {
        return ipValidateSeconds;
    }

    public void setIpValidateSeconds(int ipValidateSeconds) {
        this.ipValidateSeconds = ipValidateSeconds;
    }

    public int getIpValidateCount() {
        return ipValidateCount;
    }

    public void setIpValidateCount(int ipValidateCount) {
        this.ipValidateCount = ipValidateCount;
    }
}
