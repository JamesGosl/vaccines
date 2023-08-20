package org.james.gos.vaccines.common.event.publisher;

import org.james.gos.vaccines.common.doman.enums.RedisChannelEnum;
import org.james.gos.vaccines.common.event.RedisClearApplicationEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * RedisClearApplicationEventPublisher
 *
 * @author James Gosl
 * @since 2023/08/18 14:15
 */
@Component
public class RedisClearApplicationEventPublisher extends RedisApplicationEventPublisherBase {

    @Override
    public boolean support(RedisChannelEnum redisChannel) {
        return Objects.equals(RedisChannelEnum.CLEAR, redisChannel);
    }

    @Override
    public void publishEvent(byte[] bytes) {
        publishEvent(new RedisClearApplicationEvent(bytes));
    }
}
