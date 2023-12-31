package org.james.gos.vaccines.user.service;

import org.james.gos.vaccines.user.doman.dto.UserDTO;
import org.james.gos.vaccines.user.doman.vo.request.UserReq;

/**
 * 用户表 逻辑层
 *
 * @author James Gosl
 * @since 2023/08/15 19:24
 */
public interface UserService {

    /**
     * 获取当前用户信息
     *
     * @param aid 账户信息
     */
    UserDTO getUser(Long aid);

    /**
     * 更新用户信息
     *
     * @param userReq  用户信息
     */
    void updateUser(Long aid, UserReq userReq);

    /**
     * 增加用户信息
     *
     * @param aid 账户id
     */
    void insertUser(Long aid);

    /**
     * 删除用户信息
     *
     * @param aid 账户id
     */
    void deletedUser(Long aid);
}
