package com.ecmp.spring.boot.autoconfigure;

import com.ecmp.context.BaseApplicationContext;
import com.ecmp.context.ContextUtil;
import com.ecmp.log.util.LogUtil;
import org.springframework.boot.ECMPEnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/5/30 17:46
 */
@SuppressWarnings("unchecked")
public class WebEnvironmentPostProcessor extends ECMPEnvironmentPostProcessor {

    @Override
    public PropertiesPropertySource setECMPDefaultConfig(ConfigurableEnvironment environment, Properties properties) {
        //禁用spring boot自动配置
        //禁用数据源配置
        excludeConfigClass(DataSourceAutoConfiguration.class);

        /* thymeleaf */
        //网页所在路径
        properties.put("spring.thymeleaf.prefix", "classpath:/views/");
        //网页后缀名
        properties.put("spring.thymeleaf.suffix", ".html");
        //网页类型
        properties.put("spring.thymeleaf.mode", "HTML");
        //网页编码格式
        properties.put("spring.thymeleaf.encoding", "UTF-8");

        //时间戳统一转换
        if (!environment.containsProperty("spring.jackson.date-format")) {
            properties.put("spring.jackson.date-format", "yyyy-MM-dd HH:mm:ss");
        }
        if (!environment.containsProperty("spring.jackson.time-zone")) {
            properties.put("spring.jackson.time-zone", "GMT+8");
        }
        if (!environment.containsProperty("spring.jackson.default-property-inclusion")) {
            properties.put("spring.jackson.default-property-inclusion", "non_null");
        }

        /* 自定义扩展WebSecurityCoreConfig开关 */
        if (!environment.containsProperty("com.ecmp.core.security.config.enable")) {
            properties.put("com.ecmp.core.security.config.enable", "true");
        }

        //图形验证码长度
        if (!environment.containsProperty("com.ecmp.core.security.code.image.length")) {
            properties.put("com.ecmp.core.security.code.image.length", "4");
        }
        //图形验证码宽度
        if (!environment.containsProperty("com.ecmp.core.security.code.image.width")) {
            properties.put("com.ecmp.core.security.code.image.width", "150");
        }
        //图形验证码高度
        if (!environment.containsProperty("com.ecmp.core.security.code.image.height")) {
            properties.put("com.ecmp.core.security.code.image.height", "80");
        }
        //图形验证码过期时间
        if (!environment.containsProperty("com.ecmp.core.security.code.image.expireIn")) {
            properties.put("com.ecmp.core.security.code.image.expireIn", "120");
        }
        //需要图形验证码拦截的url
        //properties.setProperty("com.ecmp.core.security.code.image.url", "/login");

        /*//短信验证码长度
        properties.setProperty("com.ecmp.core.security.code.sms.length", "6");
        //短信验证码过期时间
        properties.setProperty("com.ecmp.core.security.code.sms.expireIn", "120");
        //需要短信验证码拦截的url
        properties.setProperty("com.ecmp.core.security.code.sms.url", "/mobile");*/


//        properties.setProperty("com.ecmp.core.security.code.repository", "redis");

        //需要设置同ip在一定时间内最多可访问同url几次的url路径
//        properties.setProperty("com.ecmp.core.security.authorize.ipValidateUrl", "/index");
        //同ip在一定时间内最多可访问同url几次的时间配置（秒）
//        properties.setProperty("com.ecmp.core.security.authorize.ipValidateSeconds", "5");
        //同ip在一定时间内最多可访问同url几次的次数配置
//        properties.setProperty("com.ecmp.core.security.authorize.ipValidateCount", "5");

        //无需登录即可访问的url
        if (!environment.containsProperty("com.ecmp.core.security.authorize.permitUrls")) {
            properties.put("com.ecmp.core.security.authorize.permitUrls", "/, /static/**, **/logo.ico, **/favicon.ico");
        }
        //若出现401状态码，则跳转到固定page
        if (!environment.containsProperty("com.ecmp.core.security.authorize.unAuthorizePage")) {
            properties.put("com.ecmp.core.security.authorize.unAuthorizePage", "/login");
        }

        //REDIRECT/JSON 重定向到页面还是返回JSON
//        properties.setProperty("com.ecmp.core.security.authentication.loginType", "REDIRECT");
        //REDIRECT的方式，认证失败后跳转到的页面url
        //properties.setProperty("com.ecmp.core.security.authentication.loginErrorPage", "http://decmp.changhong.com/basic-web/login");
        //REDIRECT的方式，认证成功后跳转到的页面url
//        properties.setProperty("com.ecmp.core.security.authentication.loginSuccessPage", "/index");

        //退出登录接口，缺省值为/logout
        if (!environment.containsProperty("com.ecmp.core.security.logout.logoutUrl")) {
            properties.put("com.ecmp.core.security.logout.logoutUrl", "/logout");
        }
        //缺省值：/default-login.html,是跳转到URL还是返回JSON，只要这里配置了URL（并且不为缺省值），那就是跳转到URL
        if (!environment.containsProperty("com.ecmp.core.security.logout.logoutSuccessUrl")) {
            properties.put("com.ecmp.core.security.logout.logoutSuccessUrl", "/login");
        }

        //记住我时长（缺省值3600s） 需要提前建表
//        properties.setProperty("com.ecmp.core.security.rememberme.seconds", "7200");

        //1：放开frame的拦截权限。缺省值；0：不允许frame嵌套
//        properties.setProperty("com.ecmp.core.security.frame.disableStatus", "1");

        //session失效/被踢掉时跳转的地址，默认不配置，不配置则代表返回JSON格式
        if (!environment.containsProperty("com.ecmp.core.security.session.sessionInvalidUrl")) {
            properties.put("com.ecmp.core.security.session.sessionInvalidUrl", "/login?expired");
        }
        //同一个用户在系统中最大的session数。默认-1，不限制
        if (!environment.containsProperty("com.ecmp.core.security.session.maximumSessions")) {
            properties.put("com.ecmp.core.security.session.maximumSessions", "-1");
        }

        return new PropertiesPropertySource("ECMP-Web-Gloabl-Config", properties);
    }


    @Override
    public int getOrder() {
        return -999990;
    }
}
