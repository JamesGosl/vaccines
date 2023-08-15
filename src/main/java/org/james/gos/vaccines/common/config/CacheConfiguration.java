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

import java.util.concurrent.TimeUnit;

/**
 * 缓存配置类
 *
 * @author James Gosl
 * @since 2023/08/15 16:18
 */
@EnableCaching
@SpringBootConfiguration
public class CacheConfiguration extends CachingConfigurerSupport {

    @Bean("caffeineCacheManager")
    @Primary
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        // 方案一(常用)：定制化缓存Cache
        // 本地缓存 5分钟过期 有的数据建议不要在本地缓存存放
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .initialCapacity(100)
                .maximumSize(200));
        return cacheManager;
    }
}
