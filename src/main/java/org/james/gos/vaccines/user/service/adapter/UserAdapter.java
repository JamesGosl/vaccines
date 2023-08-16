package org.james.gos.vaccines.user.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import org.james.gos.vaccines.user.doman.dto.UserDTO;
import org.james.gos.vaccines.user.doman.entity.User;
import org.james.gos.vaccines.user.doman.vo.request.UserReq;
import org.james.gos.vaccines.user.doman.vo.response.UserResp;

/**
 * UserAdapter
 *
 * @author James Gosl
 * @since 2023/08/16 17:47
 */
public class UserAdapter {

    public static UserResp build(UserDTO userDTO) {
        UserResp userResp = new UserResp();
        BeanUtil.copyProperties(userDTO, userResp);
        return userResp;
    }

    public static User build(Long id) {
        return new User().setAccountId(id);
    }

    public static User build(UserReq userReq) {
        User user = new User();
        BeanUtil.copyProperties(userReq, user);
        return user;
    }

    public static User build(Long aid, UserReq userReq) {
        User user = new User();
        BeanUtil.copyProperties(userReq, user);
        return user.setAccountId(aid);
    }
}
