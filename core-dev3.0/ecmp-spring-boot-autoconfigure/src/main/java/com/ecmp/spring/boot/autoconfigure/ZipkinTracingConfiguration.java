package com.ecmp.spring.boot.autoconfigure;

import brave.Tracer;
import brave.Tracing;
import brave.context.slf4j.MDCCurrentTraceContext;
import brave.http.HttpTracing;
import com.ecmp.config.util.ZipkinTracingClientFilter;
import com.ecmp.config.util.ZipkinTracingContainerFilter;
import com.ecmp.context.ContextUtil;
import com.ecmp.context.common.ConfigConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import zipkin2.codec.SpanBytesEncoder;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.okhttp3.OkHttpSender;

/**
 * Zipkin调用链监控配置
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2018-04-25 10:33
 */
@ConditionalOnProperty(name = ZipkinTracingConfiguration.CONFIG_KEY, havingValue = "true")
public class ZipkinTracingConfiguration {

    public static final String CONFIG_KEY = "com.ecmp.zipkin.enable";

    // 构建客户端发送工具
    @Bean
    public Sender sender() {
        String zipkinServer = ContextUtil.getGlobalProperty(ConfigConstants.ZIPKIN_SERVER_KEY);
        return OkHttpSender.create(zipkinServer);
    }

    // 构建异步reporter
    @Bean
    public AsyncReporter<zipkin2.Span> spanReporter() {
        Sender sender = sender();
        return AsyncReporter.builder(sender).build(SpanBytesEncoder.JSON_V2);
    }

    // 构建tracing上下文
    @Bean
    public Tracing tracing() {
        AsyncReporter<zipkin2.Span> spanReporter = spanReporter();
        String serviceName = ContextUtil.getAppCode();
        return Tracing.newBuilder()
                .localServiceName(serviceName)
                .spanReporter(spanReporter)
                .currentTraceContext(MDCCurrentTraceContext.create())
                .build();
    }

    // decides how to name and tag spans. By default they are named the same as the http method.
    @Bean
    public HttpTracing httpTracing(Tracing tracing) {
        return HttpTracing.create(tracing);
    }

    // 使用tracer创建span
    @Bean
    public Tracer tracer() {
        Tracing tracing = tracing();
        return tracing.tracer();
    }

    // 拦截jax-rs 客户端
    @Bean(name = ZipkinTracingClientFilter.BEAN_ID)
    public ZipkinTracingClientFilter tracingClientFilter(HttpTracing tracing) {
        return new ZipkinTracingClientFilter(tracing);
    }

    //　拦截jax-rs 服务端
    @Bean
    public ZipkinTracingContainerFilter tracingContainerFilter(HttpTracing tracing) {
        return new ZipkinTracingContainerFilter(tracing);
    }
}
