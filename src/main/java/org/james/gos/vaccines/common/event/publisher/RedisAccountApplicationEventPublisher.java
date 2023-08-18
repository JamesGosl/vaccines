package org.james.gos.vaccines.common.event.publisher;

import org.james.gos.vaccines.common.doman.enums.RedisChannelEnum;
import org.james.gos.vaccines.common.event.RedisAccountApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * RedisAccountApplicationEventPublisher
 *
 * @author James Gosl
 * @since 2023/08/18 14:13
 */
@Component
public class RedisAccountApplicationEventPublisher extends RedisApplicationEventPublisher {

    @Override
    public boolean support(RedisChannelEnum redisChannel) {
        return RedisChannelEnum.ACCOUNT.equals(redisChannel);
    }

    public void publishEvent(byte[] bytes) {
        publishEvent(new RedisAccountApplicationEvent(bytes));
    }
}
