package com.ecmp.core.security.properties.rememberme;

/**
 * 记住我配置
 *
 * @author Vision.Mac 2018-06-05 15:59
 */
public class RememberMeProperties {

    /** 默认1小时 */
    private int seconds = 3600;

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
