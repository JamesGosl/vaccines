package org.james.gos.vaccines.common.event;

/**
 * 令牌更新事件
 *
 * @author James Gosl
 * @since 2023/08/17 17:51
 */
public class RedisTokenApplicationEvent extends RedisApplicationEvent<Long> {
    private static final long serialVersionUID = 2904743399955570809L;

    public RedisTokenApplicationEvent(byte[] bytes) {
        super(bytes);
    }

    @Override
    protected Long doGetValue(byte[] bytes) {
        return null;
    }
}
