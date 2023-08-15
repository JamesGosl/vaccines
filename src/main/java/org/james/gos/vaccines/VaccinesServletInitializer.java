package org.james.gos.vaccines;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Servlet 容器中进行初始化
 *
 * @author James Gosl
 * @since 2023/08/14 23:11
 */
public class VaccinesServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(VaccinesApplication.class);
    }
}
