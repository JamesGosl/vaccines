package org.james.gos.vaccines.common.config;

import org.james.gos.vaccines.common.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 安全相关配置
 *
 * @author James Gosl
 * @since 2023/08/14 20:26
 */
@SpringBootConfiguration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfiguration {

    @Autowired
    private SecurityProperties securityProperties;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(securityProperties.getPasswordEncoderLength());
    }
}
