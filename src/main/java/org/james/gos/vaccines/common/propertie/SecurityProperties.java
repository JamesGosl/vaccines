package org.james.gos.vaccines.common.propertie;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 安全设置属性集
 *
 * @author James Gosl
 * @since 2023/08/14 20:19
 */
@Data
@ConfigurationProperties(prefix = "vaccines.security")
public class SecurityProperties {

    /**
     * PasswordEncoder 加密复杂度，越高开销越大
     */
    private Integer passwordEncoderLength = 4;
}
