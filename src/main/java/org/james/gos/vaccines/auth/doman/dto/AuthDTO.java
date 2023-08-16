package org.james.gos.vaccines.auth.doman.dto;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import org.james.gos.vaccines.auth.doman.entity.Auth;

/**
 * AuthDTO
 *
 * @author James Gosl
 * @since 2023/08/16 13:13
 */
@Data
public class AuthDTO {

    /** id */
    private Long id;
    /** 权限字段 */
    private Integer auth;
    /** 描述字段 */
    private String description;

    public static AuthDTO build(Auth auth) {
        AuthDTO authDTO = new AuthDTO();
        BeanUtil.copyProperties(auth, authDTO);
        return authDTO;
    }
}
