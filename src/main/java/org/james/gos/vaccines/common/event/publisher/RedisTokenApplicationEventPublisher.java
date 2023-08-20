package org.james.gos.vaccines.common.event.publisher;

import org.james.gos.vaccines.common.doman.enums.RedisChannelEnum;
import org.james.gos.vaccines.common.event.RedisTokenApplicationEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 令牌更新事件
 *
 * @author James Gosl
 * @since 2023/08/18 14:17
 */
@Component
public class RedisTokenApplicationEventPublisher extends RedisApplicationEventPublisherBase {

    @Override
    public boolean support(RedisChannelEnum redisChannel) {
        return Objects.equals(RedisChannelEnum.TOKEN, redisChannel);
    }

    @Override
    public void publishEvent(byte[] bytes) {
        publishEvent(new RedisTokenApplicationEvent(bytes));
    }
}
