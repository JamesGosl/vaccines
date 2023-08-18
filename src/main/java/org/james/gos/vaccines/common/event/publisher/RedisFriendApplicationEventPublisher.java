package org.james.gos.vaccines.common.event.publisher;

import org.james.gos.vaccines.common.doman.enums.RedisChannelEnum;
import org.james.gos.vaccines.common.event.RedisFriendApplicationEvent;
import org.james.gos.vaccines.common.event.RedisVaccinesApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * RedisUserApplicationEventPublisher
 *
 * @author James Gosl
 * @since 2023/08/18 18:50
 */
@Component
public class RedisFriendApplicationEventPublisher extends RedisApplicationEventPublisher {

    @Override
    public boolean support(RedisChannelEnum redisChannel) {
        return RedisChannelEnum.FRIEND.equals(redisChannel);
    }

    @Override
    public void publishEvent(byte[] bytes) {
        publishEvent(new RedisFriendApplicationEvent(bytes));
    }
}
