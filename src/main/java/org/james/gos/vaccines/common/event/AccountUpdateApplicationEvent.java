package org.james.gos.vaccines.common.event;

import org.springframework.context.ApplicationEvent;

/**
 * 账户更新事件
 *
 * @author James Gosl
 * @since 2023/08/17 17:51
 */
public class AccountUpdateApplicationEvent extends ApplicationEvent {
    private static final long serialVersionUID = 2904743399955570809L;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public AccountUpdateApplicationEvent(Object source) {
        super(source);
    }
}
