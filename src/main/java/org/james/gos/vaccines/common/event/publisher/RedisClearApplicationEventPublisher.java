package org.james.gos.vaccines.common.event.publisher;

import org.james.gos.vaccines.common.doman.enums.RedisChannelEnum;
import org.james.gos.vaccines.common.event.RedisClearApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * RedisClearApplicationEventPublisher
 *
 * @author James Gosl
 * @since 2023/08/18 14:15
 */
@Component
public class RedisClearApplicationEventPublisher extends RedisApplicationEventPublisher {

    @Override
    public boolean support(RedisChannelEnum redisChannel) {
        return RedisChannelEnum.CLEAR.equals(redisChannel);
    }

    @Override
    public void publishEvent(byte[] bytes) {
        publishEvent(new RedisClearApplicationEvent(bytes));
    }
}
