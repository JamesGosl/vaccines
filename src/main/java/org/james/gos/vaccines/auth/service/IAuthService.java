package org.james.gos.vaccines.auth.service;

import org.james.gos.vaccines.auth.doman.vo.response.AuthResp;

import java.util.List;

/**
 * 权限表 逻辑层
 *
 * @author James Gosl
 * @since 2023/08/15 15:34
 */
public interface IAuthService {

    /**
     * 获取所有权限信息
     *
     * @return 二级缓存查找、数据库查找
     */
    List<AuthResp> getAuthAll();

    /**
     * 根据ID 获取权限信息
     *
     * @param id id
     * @return 权限信息
     */
    AuthResp getAuth(Long id);

    /**
     * 根据auth 获取权限信息
     *
     * @param auth id
     * @return 权限信息
     */
    AuthResp getAuth(Integer auth);
}
