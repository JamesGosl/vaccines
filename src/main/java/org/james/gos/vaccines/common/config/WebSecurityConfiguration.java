package org.james.gos.vaccines.common.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * SpringSecurity 配置
 *
 * @author James Gosl
 * @since 2023/08/15 16:51
 */
@SpringBootConfiguration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(expressionInterceptUrlRegistry ->
                // 所有请求都放行，不使用SpringSecurity
                expressionInterceptUrlRegistry.anyRequest().permitAll());
    }
}