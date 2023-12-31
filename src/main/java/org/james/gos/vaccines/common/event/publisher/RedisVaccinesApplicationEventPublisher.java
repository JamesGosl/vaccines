package org.james.gos.vaccines.common.event.publisher;

import org.james.gos.vaccines.common.doman.enums.RedisChannelEnum;
import org.james.gos.vaccines.common.event.RedisVaccinesApplicationEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * RedisUserApplicationEventPublisher
 *
 * @author James Gosl
 * @since 2023/08/18 18:50
 */
@Component
public class RedisVaccinesApplicationEventPublisher extends RedisApplicationEventPublisherBase {

    @Override
    public boolean support(RedisChannelEnum redisChannel) {
        return Objects.equals(RedisChannelEnum.VACCINES, redisChannel);
    }

    @Override
    public void publishEvent(byte[] bytes) {
        publishEvent(new RedisVaccinesApplicationEvent(bytes));
    }
}
