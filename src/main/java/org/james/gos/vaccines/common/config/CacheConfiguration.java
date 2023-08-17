package org.james.gos.vaccines.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 缓存配置类
 *
 * @author James Gosl
 * @since 2023/08/15 16:18
 */
@EnableCaching(proxyTargetClass = true)
@SpringBootConfiguration
public class CacheConfiguration extends CachingConfigurerSupport {

    @Bean("caffeineCacheManager")
    @Primary
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        // 本地缓存
        cacheManager.setCaffeine(Caffeine.newBuilder()
                // 写入后过期时间
                .expireAfterWrite(Duration.ofMinutes(5))
                // 初始化条目
                .initialCapacity(200)
                // 最大条目
                .maximumSize(1000));
        return cacheManager;
    }
}
