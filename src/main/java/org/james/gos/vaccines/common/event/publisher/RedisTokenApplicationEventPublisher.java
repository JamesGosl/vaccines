package org.james.gos.vaccines.common.event.publisher;

import org.james.gos.vaccines.common.doman.enums.RedisChannelEnum;
import org.james.gos.vaccines.common.event.RedisTokenApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * 令牌更新事件
 *
 * @author James Gosl
 * @since 2023/08/18 14:17
 */
@Component
public class RedisTokenApplicationEventPublisher extends RedisApplicationEventPublisher {

    @Override
    public boolean support(RedisChannelEnum redisChannel) {
        return RedisChannelEnum.TOKEN.equals(redisChannel);
    }

    @Override
    public void publishEvent(byte[] bytes) {
        publishEvent(new RedisTokenApplicationEvent(bytes));
    }
}
