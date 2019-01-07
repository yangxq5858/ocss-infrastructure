package com.ecmp.spring.boot.autoconfigure;

import com.ecmp.annotation.IgnoreAuthentication;
import com.ecmp.annotation.IgnoreCheckAuth;
import com.ecmp.core.security.authentication.CoreAuthenticationFilter;
import com.ecmp.core.security.authentication.CoreAuthenticationProvider;
import com.ecmp.core.security.authentication.LoginService;
import com.ecmp.core.security.authentication.LoginServiceImpl;
import com.ecmp.core.security.authentication.rememberme.RedisTokenRepositoryImpl;
import com.ecmp.core.security.authorize.CheckAuthorizeService;
import com.ecmp.core.security.authorize.DefaultCheckAuthorizeService;
import com.ecmp.core.security.constant.CoreConstants;
import com.ecmp.core.security.jwt.JwtAuthenticationTokenFilter;
import com.ecmp.core.security.properties.SecurityProperties;
import com.ecmp.core.security.session.CoreChangeSessionIdAuthenticationStrategy;
import com.ecmp.log.util.LogUtil;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.Filter;
import javax.servlet.annotation.WebServlet;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 验证安全核心配置
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/7/1 10:00
 */
@ComponentScan("com.ecmp.core.security")
@EnableConfigurationProperties(SecurityProperties.class)
@ConditionalOnProperty(prefix = "com.ecmp.core.security.config", name = "enable")
public class WebSecurityCoreConfig extends WebSecurityConfigurerAdapter implements InitializingBean, CoreConstants {

    @Autowired
    private SecurityProperties securityProperties;
    // 记住我自动登录需要用到
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * session
     */
    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;
    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;
    /**
     * 退出处理器
     */
    @Autowired
    LogoutSuccessHandler logoutSuccessHandler;
    /**
     * 失败处理器
     */
    @Autowired
    AuthenticationFailureHandler authenticationFailureHandler;
    /**
     * 成功处理器
     */
    @Autowired
    AuthenticationSuccessHandler authenticationSuccessHandler;
    /**
     * 验证码
     */
    @Autowired
    private Filter validateCodeFilter;

    /**
     * 短信验证配置
     */
//    @Autowired
//    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private SessionAuthenticationStrategy sessionAuthenticationStrategy;

//    /**
//     * 加密用的
//     * <p>
//     * 默认注册一个加密bean
//     * 这样在UserDetailsService里才能用PasswordEncoder
//     */
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    /**
     * url权限检查映射关系
     * key：url
     * val:className
     */
    public static ConcurrentHashMap<String, String> urlHandlerMap = new ConcurrentHashMap<>();
    /**
     * key: className
     * val: 当前控制器类的所有url
     */
    public static ConcurrentHashMap<String, Set<String>> handlersMap = new ConcurrentHashMap<>();
    /**
     * 忽略登录认证检查的功能
     * 不需要登录验证的
     */
    public static Set<String> unauthorizedUrlSet = new HashSet<>();
    /**
     * 忽略权限检查的功能
     * 需要登录验证的
     */
    public static Set<String> forbiddenUrlSet = new HashSet<>();

    /**
     * Invoked by a BeanFactory after it has set all bean properties supplied
     * (and satisfied BeanFactoryAware and ApplicationContextAware).
     * <p>This method allows the bean instance to perform initialization only
     * possible when all bean properties have been set and to throw an
     * exception in the event of misconfiguration.
     */
    @Override
    public void afterPropertiesSet() {
        try {
            ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false);
            //servlet3注解
            scan.addIncludeFilter(new AnnotationTypeFilter(WebServlet.class));
            //spring mvc注解
            scan.addIncludeFilter(new AnnotationTypeFilter(Controller.class));

            String basePackage = "com.ecmp.*";
            Set<BeanDefinition> beanDefinitions = scan.findCandidateComponents(basePackage);

            ClassPool pool = ClassPool.getDefault();
            /*
                The default ClassPool returned by a static method ClassPool.
                getDefault() searches the same path that the underlying JVM (Java virtual machine) has.
                If a program is running on a web application server such as JBoss and Tomcat,
                the ClassPool object may not be able to find user classes since such
                a web application server uses multiple class loaders as well as the system class loader.
                In that case, an additional class path must be registered to the ClassPool. Suppose that pool refers to a ClassPool object:
             */
            pool.insertClassPath(new ClassClassPath(DefaultCheckAuthorizeService.class));

            String className;
            CtClass ctClass;
            String[] cPathArr;
            String[] mPathArr;
            boolean ignoreClass;
            boolean ignoreMethod;
            boolean ignoreAuthenticationClass;
            boolean ignoreAuthenticationMethod;
            StringBuilder pathBuilder = new StringBuilder();
            for (BeanDefinition beanDefinition : beanDefinitions) {
                ignoreClass = false;
                ignoreAuthenticationClass = false;

                className = beanDefinition.getBeanClassName();
                ctClass = pool.get(className);
                //spring mvc注解
                Controller controller = (Controller) ctClass.getAnnotation(Controller.class);
                if (Objects.nonNull(controller)) {
                    //获取类上的RequestMapping注解
                    RequestMapping requestMapping = (RequestMapping) ctClass.getAnnotation(RequestMapping.class);
                    if (requestMapping != null) {
                        cPathArr = new String[]{};
                        //类上的基路径整合
                        cPathArr = ArrayUtils.addAll(cPathArr, requestMapping.path());
                        cPathArr = ArrayUtils.addAll(cPathArr, requestMapping.value());
                        if (cPathArr != null && cPathArr.length > 0) {
                            //类上全局忽略登录认证检查
                            IgnoreAuthentication ignoreClass1 = (IgnoreAuthentication) ctClass.getAnnotation(IgnoreAuthentication.class);
                            if (ignoreClass1 != null && ignoreClass1.value()) {
                                ignoreAuthenticationClass = true;
                            }
                            //类上全局忽略权限检查
                            IgnoreCheckAuth ignoreCheckAuthClass = (IgnoreCheckAuth) ctClass.getAnnotation(IgnoreCheckAuth.class);
                            if (ignoreCheckAuthClass != null && ignoreCheckAuthClass.value()) {
                                ignoreClass = true;
                            }

                            CtMethod[] ctMethods = ctClass.getMethods();
                            if (ctMethods != null && ctMethods.length > 0) {
                                Set<String> pathSet = new HashSet<>();
                                for (String cpath : cPathArr) {
                                    cpath = cpath.startsWith("/") ? cpath : "/" + cpath;
                                    for (CtMethod ctMethod : ctMethods) {
                                        requestMapping = (RequestMapping) ctMethod.getAnnotation(RequestMapping.class);
                                        if (requestMapping == null) {
                                            continue;
                                        }
                                        ignoreMethod = false;
                                        ignoreAuthenticationMethod = false;
                                        //方法上忽略权限检查
                                        IgnoreCheckAuth ignoreCheckAuthMethod = (IgnoreCheckAuth) ctMethod.getAnnotation(IgnoreCheckAuth.class);
                                        if (ignoreCheckAuthMethod != null && ignoreCheckAuthMethod.value()) {
                                            ignoreMethod = true;
                                        }
                                        IgnoreAuthentication ignoreMethod1 = (IgnoreAuthentication) ctMethod.getAnnotation(IgnoreAuthentication.class);
                                        if (ignoreMethod1 != null && ignoreMethod1.value()) {
                                            ignoreAuthenticationMethod = true;
                                        }
                                        mPathArr = new String[]{};
                                        //类上的基路径整合
                                        mPathArr = ArrayUtils.addAll(mPathArr, requestMapping.path());
                                        mPathArr = ArrayUtils.addAll(mPathArr, requestMapping.value());
                                        if (mPathArr.length > 0) {
                                            for (String mpath : mPathArr) {
                                                pathBuilder.setLength(0);
                                                if (cpath.endsWith("/")) {
                                                    pathBuilder.append(cpath);
                                                    if (mpath.startsWith("/")) {
                                                        pathBuilder.append(mpath.substring(1));
                                                    } else {
                                                        pathBuilder.append(mpath);
                                                    }
                                                } else {
                                                    pathBuilder.append(cpath);
                                                    if (mpath.startsWith("/")) {
                                                        pathBuilder.append(mpath);
                                                    } else {
                                                        pathBuilder.append("/").append(mpath);
                                                    }
                                                }

                                                pathSet.add(pathBuilder.toString());
                                                urlHandlerMap.put(pathBuilder.toString(), className);
                                                if (ignoreMethod || ignoreClass) {
                                                    forbiddenUrlSet.add(pathBuilder.toString());
                                                }
                                                if (ignoreAuthenticationMethod || ignoreAuthenticationClass) {
                                                    unauthorizedUrlSet.add(pathBuilder.toString());
                                                }
                                            }
                                        } else {
                                            pathBuilder.setLength(0);
                                            pathBuilder.append(cpath);
                                            pathSet.add(pathBuilder.toString());
                                            urlHandlerMap.put(pathBuilder.toString(), className);
                                            if (ignoreMethod || ignoreClass) {
                                                forbiddenUrlSet.add(pathBuilder.toString());
                                            }
                                            if (ignoreAuthenticationMethod || ignoreAuthenticationClass) {
                                                unauthorizedUrlSet.add(pathBuilder.toString());
                                            }
                                        }
                                    }
                                }
                                handlersMap.put(className, pathSet);
                            } else {
                                LogUtil.warn("{}无可用方法", className);
                            }
                        } else {
                            LogUtil.warn("{}类上@RequestMapping标注未设置路径", className);
                        }
                    } else {
                        //类上全局忽略权限检查
                        IgnoreCheckAuth ignoreCheckAuthClass = (IgnoreCheckAuth) ctClass.getAnnotation(IgnoreCheckAuth.class);
                        if (ignoreCheckAuthClass != null && ignoreCheckAuthClass.value()) {
                            ignoreClass = true;
                        }
                        IgnoreAuthentication ignoreClass1 = (IgnoreAuthentication) ctClass.getAnnotation(IgnoreAuthentication.class);
                        if (ignoreClass1 != null && ignoreClass1.value()) {
                            ignoreAuthenticationClass = true;
                        }

                        CtMethod[] ctMethods = ctClass.getMethods();
                        if (ctMethods != null && ctMethods.length > 0) {
                            Set<String> pathSet = new HashSet<>();
                            for (CtMethod ctMethod : ctMethods) {
                                requestMapping = (RequestMapping) ctMethod.getAnnotation(RequestMapping.class);
                                if (requestMapping == null) {
                                    continue;
                                }
                                ignoreMethod = false;
                                ignoreAuthenticationMethod = false;
                                //方法上忽略权限检查
                                IgnoreCheckAuth ignoreCheckAuthMethod = (IgnoreCheckAuth) ctMethod.getAnnotation(IgnoreCheckAuth.class);
                                if (ignoreCheckAuthMethod != null && ignoreCheckAuthMethod.value()) {
                                    ignoreMethod = true;
                                }
                                IgnoreAuthentication ignoreMethod1 = (IgnoreAuthentication) ctMethod.getAnnotation(IgnoreAuthentication.class);
                                if (ignoreMethod1 != null && ignoreMethod1.value()) {
                                    ignoreAuthenticationMethod = true;
                                }
                                mPathArr = new String[]{};
                                //类上的基路径整合
                                mPathArr = ArrayUtils.addAll(mPathArr, requestMapping.path());
                                mPathArr = ArrayUtils.addAll(mPathArr, requestMapping.value());
                                for (String mpath : mPathArr) {
                                    pathBuilder.setLength(0);
                                    if (mpath.startsWith("/")) {
                                        pathBuilder.append(mpath);
                                    } else {
                                        pathBuilder.append("/").append(mpath);
                                    }

                                    pathSet.add(pathBuilder.toString());
                                    urlHandlerMap.put(pathBuilder.toString(), className);
                                    if (ignoreMethod || ignoreClass) {
                                        forbiddenUrlSet.add(pathBuilder.toString());
                                    }
                                    if (ignoreAuthenticationMethod || ignoreAuthenticationClass) {
                                        unauthorizedUrlSet.add(pathBuilder.toString());
                                    }
                                }
                            }
                            handlersMap.put(className, pathSet);
                        } else {
                            LogUtil.warn("{}无可用方法", className);
                        }
                    }
                } else {
                    WebServlet webServlet = (WebServlet) ctClass.getAnnotation(WebServlet.class);
                    if (Objects.nonNull(webServlet)) {
                        cPathArr = new String[]{};
                        cPathArr = ArrayUtils.addAll(cPathArr, webServlet.value());
                        cPathArr = ArrayUtils.addAll(cPathArr, webServlet.urlPatterns());
                        if (cPathArr != null && cPathArr.length > 0) {
                            Set<String> pathSet = new HashSet<>();
                            //类上全局忽略权限检查
                            IgnoreCheckAuth ignoreCheckAuthClass = (IgnoreCheckAuth) ctClass.getAnnotation(IgnoreCheckAuth.class);
                            if (ignoreCheckAuthClass != null && ignoreCheckAuthClass.value()) {
                                ignoreClass = true;
                            }
                            IgnoreAuthentication ignoreClass1 = (IgnoreAuthentication) ctClass.getAnnotation(IgnoreAuthentication.class);
                            if (ignoreClass1 != null && ignoreClass1.value()) {
                                ignoreAuthenticationClass = true;
                            }
                            for (String cpath : cPathArr) {
                                cpath = cpath.startsWith("/") ? cpath : "/" + cpath;
                                pathSet.add(cpath);
                                urlHandlerMap.put(cpath, className);
                                if (ignoreClass) {
                                    forbiddenUrlSet.add(cpath);
                                }
                                if (ignoreAuthenticationClass) {
                                    unauthorizedUrlSet.add(cpath);
                                }
                            }
                            handlersMap.put(className, pathSet);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public LoginService loginService() {
        return new LoginServiceImpl();
    }

    //暴露AuthenticationManager注册成Bean供@EnableGlobalMethodSecurity使用
    @Bean
    @Override
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> list = new ArrayList<>();
        list.add(authenticationProvider());
        return new ProviderManager(list);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
//        AuthenticationProvider authenticationProvider = new CustomAuthenticationProvider(userDetailsService());
        AuthenticationProvider authenticationProvider = new CoreAuthenticationProvider(loginService());
        return authenticationProvider;
    }

    @Bean
    public CoreAuthenticationFilter authenticationFilter() {
        CoreAuthenticationFilter cuzAuthenticationFilter = new CoreAuthenticationFilter();
        cuzAuthenticationFilter.setAuthenticationManager(authenticationManager());
        cuzAuthenticationFilter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
        cuzAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        cuzAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        cuzAuthenticationFilter.setUsernameParameter("account");
        cuzAuthenticationFilter.setPasswordParameter("password");
        return cuzAuthenticationFilter;
    }

    /**
     * 在此配置不过滤的请求
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        //每一个请求对应一个空的filter链,这里一般不要配置过多,
        // 因为查找处是一个for循环,过多就导致每个请求都需要循环一遍直到找到
        web.ignoring().antMatchers(ignoringWebSecurity()).antMatchers(HttpMethod.OPTIONS);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 先加上这句话，否则登录的时候会出现403错误码，Could not verify the provided CSRF token because your session was not found.
        http.csrf().disable();
        // 放开frame的拦截权限
        http.headers().frameOptions().disable();
        http.anonymous().disable();
        //无需权限即可访问的urls
        http.authorizeRequests().antMatchers(getPermitAllUrl()).permitAll();
        //不拦截OPTIONS请求
        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll();


        //将自定义验证码的过滤器加到其他过滤器之前
        http.addFilterBefore(validateCodeFilter, AbstractPreAuthenticatedProcessingFilter.class);

        http
                .cors().and()
                // 退出登录
                .logout()
                .logoutUrl(securityProperties.getLogout().getLogoutUrl())
                //统一在自定义的CoreLogoutSuccessHandler中处理会话失效及清理认证信息，故设置为false
                .invalidateHttpSession(false).clearAuthentication(false)
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                /*// 记住我
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(securityProperties.getRememberme().getSeconds())
                .userDetailsService(userDetailsService)
                .and()*/
                .sessionManagement()
                .sessionAuthenticationStrategy(new CoreChangeSessionIdAuthenticationStrategy())
                // session失效处理器
                .invalidSessionStrategy(invalidSessionStrategy)
                // session最大并发数
                .maximumSessions(securityProperties.getSession().getMaximumSessions())
                // 被踢后的处理
                .expiredSessionStrategy(sessionInformationExpiredStrategy)
                .and()
                .and()
                //自定义的Filter替换security默认的UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage(DEFAULT_LOGIN_URL)
                .loginProcessingUrl(DEFAULT_LOGIN_URL)
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
        //.and().apply(smsCodeAuthenticationSecurityConfig)
        ;

        //所有请求需要认证
        http.authorizeRequests().anyRequest().authenticated();
        //所有请求权限检查
        http.authorizeRequests().anyRequest().access("@checkAuthorizeService.hasPermission(request,authentication)");
    }

    /**
     * 不走spring security
     */
    @Bean("ignoringWebSecurity")
    @ConditionalOnMissingBean
    public String[] ignoringWebSecurity() {
        Set<String> urls = new HashSet<>();
        urls.add("/static/**");
        urls.add("/**/*.css");
        urls.add("/**/*.js");
        urls.add("/**/*.png");
        urls.add("/**/*.ico");
        urls.add("/error");
        urls.add("/captcha");
        urls.add("/monitor/health");
        urls.add("/actuator");
        urls.add("/instances");
        urls.add("/ureport/**");
        urls.add("/ssoLogin/**");
        urls.add(DEFAULT_VALIDATE_CODE_URL_PREFIX + "/**");

        String permitUrls = securityProperties.getAuthorize().getPermitUrls();
        if (StringUtils.isNotEmpty(permitUrls) && StringUtils.isNotBlank(permitUrls)) {
            // 将配置文件读出来的url去除空白
            permitUrls = permitUrls.replace(" ", "");
            String[] urlArray = StringUtils.splitByWholeSeparator(permitUrls, ",");
            urls.addAll(Arrays.asList(urlArray));
        }

        urls.addAll(unauthorizedUrlSet);

        return urls.toArray(new String[0]);
    }

    /**
     * 获取所有的无需权限即可访问的urls
     */
    @Bean("permitAllUrl")
    @ConditionalOnMissingBean
    public String[] getPermitAllUrl() {
        Set<String> urls = new LinkedHashSet<>();
        urls.add(DEFAULT_LOGIN_URL);
        urls.add(DEFAULT_MOBILE_URL);
        urls.add("/captcha");
        urls.add("/static/**");
        urls.add("/**/*.css");
        urls.add("/**/*.js");
        urls.add("/**/*.png");
        urls.add("/**/*.ico");

        String permitUrls = securityProperties.getAuthorize().getPermitUrls();
        if (StringUtils.isNotEmpty(permitUrls) && StringUtils.isNotBlank(permitUrls)) {
            // 将配置文件读出来的url去除空白
            permitUrls = permitUrls.replace(" ", "");
            String[] urlArray = StringUtils.splitByWholeSeparator(permitUrls, ",");
            urls.addAll(Arrays.asList(urlArray));
        }
        urls.addAll(forbiddenUrlSet);

        return arrayCopy(ignoringWebSecurity(), urls.toArray(new String[0]));
    }

    private static String[] arrayCopy(String[] data1, String[] data2) {
        String[] data3 = new String[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        RedisTokenRepositoryImpl tokenRepository = new RedisTokenRepositoryImpl();
        tokenRepository.setRedisTemplate(redisTemplate);
        return tokenRepository;
    }

    @Bean
    @ConditionalOnMissingBean
    public CheckAuthorizeService checkAuthorizeService() {
        return new DefaultCheckAuthorizeService();
    }
}
