package org.james.gos.vaccines.common.event;

import org.james.gos.vaccines.account.doman.dto.AccountDTO;
import org.james.gos.vaccines.common.util.JsonUtils;

/**
 * 账户更新事件
 *
 * @author James Gosl
 * @since 2023/08/17 17:51
 */
public class RedisAccountApplicationEvent extends RedisApplicationEventBase<AccountDTO> {
    private static final long serialVersionUID = 2904743399955570809L;

    public RedisAccountApplicationEvent(byte[] bytes) {
        super(bytes);
    }

    @Override
    protected AccountDTO doGetValue(byte[] bytes) {
        return JsonUtils.toObj(bytes, AccountDTO.class);
    }
}
