//package org.james.gos.vaccines.common.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
///**
// * SpringSecurity 配置
// *
// * @author James Gosl
// * @since 2023/08/15 16:51
// */
//@Configuration
//public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        // 关闭csrf 校验
//        http.cors().disable();
//        /// 关闭权限限制
//        http.authorizeRequests().anyRequest().permitAll().and().logout().permitAll();
//    }
//}
