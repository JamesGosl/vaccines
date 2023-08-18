package org.james.gos.vaccines.common.event;

/**
 * 清除缓存事件
 *
 * @author James Gosl
 * @since 2023/08/17 14:54
 */
public class RedisClearApplicationEvent extends RedisApplicationEvent<Void> {
    private static final long serialVersionUID = 8261766728133283054L;

    public RedisClearApplicationEvent(byte[] bytes) {
        super(bytes);
    }

    @Override
    protected Void doGetValue(byte[] bytes) {
        return null;
    }
}
