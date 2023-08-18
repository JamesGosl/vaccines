package org.james.gos.vaccines.common.listener;

import org.james.gos.vaccines.common.doman.enums.RedisChannelEnum;
import org.james.gos.vaccines.common.event.publisher.RedisApplicationEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Redis 消息监听器
 *
 * @author James Gosl
 * @since 2023/08/18 13:19
 */
@Component
public class RedisMessageListener implements MessageListener {
    public static final String SUBSCRIBE_LISTENER_PATTERN = "*.del.*";

    @Autowired
    private List<RedisApplicationEventPublisher> redisApplicationEvents;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RedisChannelEnum redisChannel = RedisChannelEnum.of(message.getChannel());

        if(redisChannel == null)
            return;

        // 策略模式分发事件
        for (RedisApplicationEventPublisher redisApplicationEventPublisher : redisApplicationEvents) {
            if(redisApplicationEventPublisher.support(redisChannel)) {
                redisApplicationEventPublisher.publishEvent(message.getBody());
            }
        }
    }
}
