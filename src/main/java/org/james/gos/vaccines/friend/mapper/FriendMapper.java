package org.james.gos.vaccines.friend.mapper;

import org.james.gos.vaccines.friend.doman.entity.Friend;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * 好友表 数据层
 *
 * @author James Gosl
 * @since 2023/08/14 23:05
 */
@Repository
public interface FriendMapper extends Mapper<Friend> {
}
