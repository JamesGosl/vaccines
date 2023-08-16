package org.james.gos.vaccines.user.doman.dto;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import org.james.gos.vaccines.user.doman.entity.User;

/**
 * UserDTO
 *
 * @author James Gosl
 * @since 2023/08/16 17:46
 */
@Data
public class UserDTO {
    /** id */
    private Long id;

    /** 申请人账户id */
    private Long accountId;
    /** 申请人账户id */
    private String name;
    /** 申请人账户id */
    private String phone;
    /** 申请人账户id */
    private String address;

    public static UserDTO build(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtil.copyProperties(user, userDTO);
        return userDTO;
    }
}
