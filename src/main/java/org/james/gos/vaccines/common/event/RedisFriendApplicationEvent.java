package org.james.gos.vaccines.common.event;

import org.james.gos.vaccines.common.util.JsonUtils;
import org.james.gos.vaccines.friend.doman.dto.FriendDTO;

/**
 * 用户更新事件
 *
 * @author James Gosl
 * @since 2023/08/18 18:48
 */
public class RedisFriendApplicationEvent extends RedisApplicationEventBase<FriendDTO> {
    private static final long serialVersionUID = 4374436903523930552L;

    public RedisFriendApplicationEvent(byte[] bytes) {
        super(bytes);
    }

    @Override
    protected FriendDTO doGetValue(byte[] bytes) {
        return JsonUtils.toObj(bytes, FriendDTO.class);
    }
}
