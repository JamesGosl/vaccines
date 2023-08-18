package org.james.gos.vaccines.common.event;

import org.james.gos.vaccines.common.utils.JsonUtils;
import org.james.gos.vaccines.user.doman.dto.UserDTO;
import org.james.gos.vaccines.vaccines.doman.dto.VaccinesDTO;

/**
 * 用户更新事件
 *
 * @author James Gosl
 * @since 2023/08/18 18:48
 */
public class RedisVaccinesApplicationEvent extends RedisApplicationEvent<VaccinesDTO> {
    private static final long serialVersionUID = 4374436903523930552L;

    public RedisVaccinesApplicationEvent(byte[] bytes) {
        super(bytes);
    }

    @Override
    protected VaccinesDTO doGetValue(byte[] bytes) {
        return JsonUtils.toObj(bytes, VaccinesDTO.class);
    }
}
