package org.james.gos.vaccines.system.service;

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
     * @param id 用户id
     * @return 初始化文件
     */
    String info(Long id);
}
