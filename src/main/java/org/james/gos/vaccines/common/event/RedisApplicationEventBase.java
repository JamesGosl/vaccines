package org.james.gos.vaccines.common.event;

import org.springframework.context.ApplicationEvent;

/**
 * Redis 事件
 *
 * @author James Gosl
 * @since 2023/08/18 14:35
 */
public abstract class RedisApplicationEventBase<T> extends ApplicationEvent {
    private static final long serialVersionUID = 3609245932393284861L;

    public RedisApplicationEventBase(byte[] bytes) {
        super(bytes);
    }

    public T getValue() {
        return doGetValue((byte[]) getSource());
    }

    protected abstract T doGetValue(byte[] bytes);
}
