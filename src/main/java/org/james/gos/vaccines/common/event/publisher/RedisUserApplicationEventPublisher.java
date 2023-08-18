package org.james.gos.vaccines.common.event.publisher;

import org.james.gos.vaccines.common.doman.enums.RedisChannelEnum;
import org.james.gos.vaccines.common.event.RedisUserApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * RedisUserApplicationEventPublisher
 *
 * @author James Gosl
 * @since 2023/08/18 18:50
 */
@Component
public class RedisUserApplicationEventPublisher extends RedisApplicationEventPublisher {

    @Override
    public boolean support(RedisChannelEnum redisChannel) {
        return RedisChannelEnum.USER.equals(redisChannel);
    }

    @Override
    public void publishEvent(byte[] bytes) {
        publishEvent(new RedisUserApplicationEvent(bytes));
    }
}