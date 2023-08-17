package org.james.gos.vaccines.system.service;

import org.james.gos.vaccines.system.domain.vo.response.LoginResp;

/**
 * 系统 逻辑层
 *
 * @author James Gosl
 * @since 2023/08/16 15:48
 */
public interface ISystemService {

    /**
     * 返回系统初始化文件 前端的
     *
     * @param aid 账户id
     * @return 初始化文件
     */
    String info(Long aid);

    /**
     * 清除系统缓存
     *
     * @param aid 账户Id
     */
    void clear(Long aid);

    /**
     * 登录
     *
     * @param username 账户
     * @param password 密码
     */
    LoginResp login(String username, String password);

    /**
     * 退出
     *
     * @param aid 账户id
     */
    void logout(Long aid);


    /**
     * 效验Token 是否有效
     *
     * @param token Token
     */
    boolean verify(String token);

    /**
     * 获取用户Id
     *
     * @return 用户Id
     */
    Long getAid(String token);
}
