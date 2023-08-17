package org.james.gos.vaccines.common.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.james.gos.vaccines.common.interceptor.AccountHandlerInterceptor;
import org.james.gos.vaccines.common.interceptor.InfoHandlerInterceptor;
import org.james.gos.vaccines.common.interceptor.RequestInfoHandlerInterceptor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigInteger;

/**
 * SpringWebMVC  配置中心
 *
 * @author James Gosl
 * @since 2023/08/16 14:58
 */
@SpringBootConfiguration
public class WebConfiguration implements WebMvcConfigurer {

    /**
     * Jackson全局转化long类型为String，解决jackson序列化时传入前端Long类型缺失精度问题
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> {
            jacksonObjectMapperBuilder.serializerByType(BigInteger.class, ToStringSerializer.instance);
            jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance);
        };
    }

    // CSRF 安全设置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 是否发送Cookie
                .allowCredentials(true)
                // 放行哪些原始域
//                .allowedOrigins("*")
//                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }

    private static final String[] INCLUDE = {"/auth/**", "/account/**", "/user/**", "/vaccines/**", "/friend/**", "/system/**"};
    private static final String[] EXCLUDE = {"/account/login", "/system/info", "/vaccines/upload", "/vaccines/download"};

    // 拦截器设置
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AccountHandlerInterceptor()).addPathPatterns(INCLUDE).excludePathPatterns(EXCLUDE);
        registry.addInterceptor(new InfoHandlerInterceptor()).addPathPatterns("/system/info", "/vaccines/upload", "/vaccines/download");

//        registry.addInterceptor(new RequestInfoHandlerInterceptor()).addPathPatterns(INCLUDE).excludePathPatterns(EXCLUDE);
//        registry.addInterceptor(new DebugHandlerInterceptor()).addPathPatterns("/**");
    }
}
