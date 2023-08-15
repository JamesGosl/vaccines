package org.james.gos.vaccines.user.mapper;

import org.james.gos.vaccines.user.doman.entity.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * 用户表 数据层
 *
 * @author James Gosl
 * @since 2023/08/14 23:06
 */
@Repository
public interface UserMapper extends Mapper<User> {
}
