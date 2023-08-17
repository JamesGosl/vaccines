package org.james.gos.vaccines.common.event;

import org.springframework.context.ApplicationEvent;

/**
 * 清除缓存事件
 *
 * @author James Gosl
 * @since 2023/08/17 14:54
 */
public class ClearCacheApplicationEvent extends ApplicationEvent {
    private static final long serialVersionUID = 8261766728133283054L;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ClearCacheApplicationEvent(Object source) {
        super(source);
    }
}
