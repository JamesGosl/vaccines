package org.james.gos.vaccines.common.event;

import org.james.gos.vaccines.common.util.JsonUtils;
import org.james.gos.vaccines.user.doman.dto.UserDTO;

/**
 * 用户更新事件
 *
 * @author James Gosl
 * @since 2023/08/18 18:48
 */
public class RedisUserApplicationEvent extends RedisApplicationEventBase<UserDTO> {
    private static final long serialVersionUID = 4374436903523930552L;

    public RedisUserApplicationEvent(byte[] bytes) {
        super(bytes);
    }

    @Override
    protected UserDTO doGetValue(byte[] bytes) {
        return JsonUtils.toObj(bytes, UserDTO.class);
    }
}
