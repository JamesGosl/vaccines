package org.james.gos.vaccines.common.config;

import org.james.gos.vaccines.common.listener.RedisMessageListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Redis 配置中心
 *
 * @author James Gosl
 * @since 2023/08/18 13:15
 */
@Configuration
public class RedisConfiguration {

    // Redis 模板
    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnection) {
        // 创建模板
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 设置连接工厂
        redisTemplate.setConnectionFactory(redisConnection);
        // 设置key-value 序列化方式
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.json());
        // 设置key-hash 序列化方式
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        return redisTemplate;
    }

    // Redis 消息监听器容器
    @Bean
    public RedisMessageListenerContainer listenerContainer(RedisConnectionFactory redisConnection,
                                                           RedisMessageListener redisMessageListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // 设置Redis的连接工厂
        container.setConnectionFactory(redisConnection);
        // 设置监听的Topic: PatternTopic/ChannelTopic
        container.addMessageListener(redisMessageListener, new PatternTopic(RedisMessageListener.SUBSCRIBE_LISTENER_PATTERN));
        return container;
    }
}
