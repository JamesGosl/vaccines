package org.james.gos.vaccines.common.event.publisher;

import org.james.gos.vaccines.common.doman.enums.RedisChannelEnum;
import org.james.gos.vaccines.common.event.RedisFriendApplicationEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * RedisUserApplicationEventPublisher
 *
 * @author James Gosl
 * @since 2023/08/18 18:50
 */
@Component
public class RedisFriendApplicationEventPublisher extends RedisApplicationEventPublisherBase {

    @Override
    public boolean support(RedisChannelEnum redisChannel) {
        return Objects.equals(RedisChannelEnum.FRIEND, redisChannel);
    }

    @Override
    public void publishEvent(byte[] bytes) {
        publishEvent(new RedisFriendApplicationEvent(bytes));
    }
}
