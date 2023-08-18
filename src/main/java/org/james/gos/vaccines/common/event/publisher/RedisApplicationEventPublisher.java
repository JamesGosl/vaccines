package org.james.gos.vaccines.common.event.publisher;

import cn.hutool.extra.spring.SpringUtil;
import org.james.gos.vaccines.common.doman.enums.RedisChannelEnum;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.context.WebApplicationContext;

/**
 * BaseApplicationEvent
 *
 * @author James Gosl
 * @since 2023/08/18 13:59
 */
public abstract class RedisApplicationEventPublisher implements ApplicationEventPublisher, ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RedisApplicationEventPublisher.applicationContext = applicationContext;
    }

    public abstract boolean support(RedisChannelEnum redisChannel);

    @Override
    public void publishEvent(Object event) {
    }

    public abstract void publishEvent(byte[] bytes);

    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }
}
