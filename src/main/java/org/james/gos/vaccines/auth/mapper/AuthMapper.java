package org.james.gos.vaccines.auth.mapper;

import org.james.gos.vaccines.auth.doman.entity.Auth;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * 权限表 数据层
 *
 * @author James Gosl
 * @since 2023/08/14 23:04
 */
@Repository
public interface AuthMapper extends Mapper<Auth> {
}
