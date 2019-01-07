package com.ecmp.spring.boot.autoconfigure;

import com.ecmp.context.BaseContextSupport;
import com.ecmp.context.ContextUtil;
import com.ecmp.context.common.ConfigConstants;
import com.ecmp.core.logger.WebExceptionHandler;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.resource.VersionResourceResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Spring MVC 配置类
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2017/7/14 16:48
 */
@ComponentScan(value = "com.ecmp")
public class WebConfig extends WebMvcConfigurationSupport {
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    /*@Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setProviderClass(HibernateValidator.class);
        localValidatorFactoryBean.setValidationMessageSource(reloadableResourceBundleMessageSource());
        return localValidatorFactoryBean;
    }

    @Bean
    public ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource() {
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        reloadableResourceBundleMessageSource.setBasenames("classpath:CustomValidationMessages");
        Properties properties = new Properties();
        properties.put("fileEncodings", "utf-8");
        reloadableResourceBundleMessageSource.setFileEncodings(properties);
        reloadableResourceBundleMessageSource.setCacheSeconds(120);
        return reloadableResourceBundleMessageSource;
    }*/

    /**
     * 设置springdispatch默认名字为default
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        // 开启默认转发
        configurer.enable();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/static/", "classpath*:/META-INF/resources/static/")
//                .addResourceLocations("/static/", "classpath*:/static/")
                .setCachePeriod(60 * 60 * 24 * 365) /* one year */
                .resourceChain(!BaseContextSupport.isDevEnv())
                .addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                //设置允许跨域的路径
                .addMapping("/**")
                //设置允许跨域请求的域名
                .allowedOrigins("*")
                //设置允许的方法
                .allowedMethods("GET", "HEAD", "POST","PUT", "DELETE", "OPTIONS")
                //是否允许证书 不再默认开启
                .allowCredentials(true)
                //跨域允许时间
                .maxAge(3600);
    }

  /*  @Bean
    public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
        return new ResourceUrlEncodingFilter();
    }*/

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LocaleChangeInterceptor());
//        MappedInterceptor mappedInterceptor = new MappedInterceptor(
//                new String[]{"/**"},
//                new String[]{"/", "/login", "/static/**", "/error/**"},
//                new AuthHandlerInterceptorAdapter());
//        registry.addInterceptor(mappedInterceptor);
    }

//    /**
//     * 消息转换器配置
//     */
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(JsonUtils.mapper());
//
//        List<MediaType> mediaTypeList = new ArrayList<MediaType>();
//        mediaTypeList.add(MediaType.APPLICATION_JSON);
//        mediaTypeList.add(MediaType.APPLICATION_JSON_UTF8);
//        mediaTypeList.add(new MediaType("application", "*+json", DEFAULT_CHARSET));
//        jacksonConverter.setSupportedMediaTypes(mediaTypeList);
//
//        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(DEFAULT_CHARSET);
//        stringConverter.setWriteAcceptCharset(false);
//
//        //保持以下顺序
//        //"application/json" "application/*+json"
//        converters.add(jacksonConverter);
//        //"application/xml" "text/xml" "application/*+xml"
//        converters.add(new Jaxb2RootElementHttpMessageConverter());
//        //"application/xml" "text/xml" "application/*+xml"
//        converters.add(new SourceHttpMessageConverter<>());
//        //"application/x-www-form-urlencoded" "multipart/form-data"
//        converters.add(new AllEncompassingFormHttpMessageConverter());
//        //"application/octet-stream" "*/*"
//        converters.add(new ByteArrayHttpMessageConverter());
//        //"text/plain" "*/*"
//        converters.add(stringConverter);
//        //"*/*"
//        converters.add(new ResourceHttpMessageConverter());
//    }

    @Resource
    private void configureThymeleafStaticVars(ThymeleafViewResolver viewResolver) {
        if (viewResolver != null) {
            //设置全局静态资源服务器地址
            String staticResourceCDN = ContextUtil.getProperty(ConfigConstants.STATIC_RESOURCE_CDN);
            Map<String, Object> vars = new HashMap<>();
            vars.put("static_resource_url", staticResourceCDN);
            vars.put("SysName", ContextUtil.getGlobalProperty(ConfigConstants.SYS_NAME));
            viewResolver.setStaticVariables(vars);
        }
    }

//    /**
//     * 使用thymeleaf解析
//     */
//    @Bean
//    public SpringResourceTemplateResolver templateResolver() {
//        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
//        resolver.setPrefix("/WEB-INF/views/");
//        resolver.setSuffix(".html");
//        resolver.setTemplateMode(TemplateMode.HTML);
//        resolver.setCharacterEncoding("UTF-8");
//        //是否缓存页面，开发时设置为false
//        boolean isDev = ContextUtil.isDevEnv();
//        resolver.setCacheable(!isDev);
//        return resolver;
//    }

//    /**
//     * 模版引擎
//     */
//    @Bean
//    public SpringTemplateEngine templateEngine() {
//        SpringTemplateEngine engine = new SpringTemplateEngine();
//        engine.setTemplateResolver(templateResolver());
//        return engine;
//    }
//
//    /**
//     * thymeleaf模版引擎视图解析器
//     */
//    @Bean
//    public ViewResolver thymeleafViewResolver() {
//        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
//        resolver.setTemplateEngine(templateEngine());
//        resolver.setCharacterEncoding("UTF-8");
//        resolver.setContentType("text/html;charset=UTF-8");
//        resolver.setOrder(2);
//        return resolver;
//    }

//    /**
//     * 文件上传
//     */
//    @Bean
//    public CommonsMultipartResolver multipartResolver() {
//        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
//        //设置上传文件的最大尺寸为100MB
//        resolver.setMaxUploadSize(104857600);
//        resolver.setDefaultEncoding("UTF-8");
//        resolver.setMaxInMemorySize(104857600);
//        return resolver;
//    }

    /**
     * 将Controller抛出的异常转到特定View
     */
    @Bean
    public WebExceptionHandler webExceptionHandler() {
        WebExceptionHandler handler = new WebExceptionHandler();
        Properties prope = new Properties();
        prope.setProperty("java.lang.Throwable", "error/500");
        handler.setExceptionMappings(prope);
        //默认错误页面，当找不到上面mappings中指定的异常对应视图时，使用本默认配置
        handler.setDefaultErrorView("error/500");
        //默认HTTP状态码
        handler.setDefaultStatusCode(500);
        return handler;
    }

//    @Bean
//    public ErrorPageFilter errorPageFilter() {
//        return new ErrorPageFilter();
//    }
//
//    @Bean
//    public FilterRegistrationBean disableSpringBootErrorFilter(ErrorPageFilter filter) {
//        FilterRegistrationBean<ErrorPageFilter> filterRegistrationBean = new FilterRegistrationBean<>();
//        filterRegistrationBean.setFilter(filter);
//        filterRegistrationBean.setEnabled(false);
//        return filterRegistrationBean;
//    }
}
