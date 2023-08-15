package org.james.gos.vaccines.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dom4j.io.SAXReader;
import org.james.gos.vaccines.common.utils.JwtUtils;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 工具类配置中心
 *
 * @author James Gosl
 * @since 2023/08/15 20:04
 */
@SpringBootConfiguration
public class UtilsConfiguration {

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }

//    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public SAXReader saxReader() {
        return new SAXReader();
    }
}
