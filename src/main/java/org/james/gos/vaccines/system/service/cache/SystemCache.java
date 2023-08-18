package org.james.gos.vaccines.system.service.cache;

import lombok.extern.slf4j.Slf4j;
import org.james.gos.vaccines.common.constant.CacheKey;
import org.james.gos.vaccines.common.constant.RedisKey;
import org.james.gos.vaccines.common.constant.SystemKey;
import org.james.gos.vaccines.common.doman.enums.AuthEnum;
import org.james.gos.vaccines.common.doman.enums.RedisChannelEnum;
import org.james.gos.vaccines.common.event.RedisApplicationEvent;
import org.james.gos.vaccines.common.event.RedisClearApplicationEvent;
import org.james.gos.vaccines.common.event.RedisTokenApplicationEvent;
import org.james.gos.vaccines.common.utils.CacheUtils;
import org.james.gos.vaccines.common.utils.FileUtils;
import org.james.gos.vaccines.common.utils.RedisUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * SystemCache
 *
 * @author James Gosl
 * @since 2023/08/16 15:51
 */
@Component
@Slf4j
public class SystemCache implements ApplicationListener<RedisApplicationEvent<?>> {

    @Cacheable(cacheNames = CacheKey.SYSTEM, key = "'info' + #auth.auth")
    public String info(AuthEnum auth) {
        log.debug("缓存未击中-{}", auth.getAuth());
        return FileUtils.fileToString(SystemKey.getKey(SystemKey.INFO_PATH, auth.getAuth()));
    }

    @Cacheable(cacheNames = CacheKey.SYSTEM, key = "'token-' + #aid")
    public String getToken(Long aid) {
        return RedisUtils.get(RedisKey.getKey(RedisKey.TOKEN, aid), String.class);
    }

    @CacheEvict(cacheNames = CacheKey.SYSTEM, key = "'token-' + #aid")
    public void setToken(Long aid, String token) {
        RedisUtils.set(RedisKey.getKey(RedisKey.TOKEN, aid), token);
    }

    @CacheEvict(cacheNames = CacheKey.SYSTEM, key = "'token-' + #aid")
    public void clearToken(Long aid) {
        if (RedisUtils.del(RedisKey.getKey(RedisKey.TOKEN, aid))) {
            // Redis 发布/订阅 保证数据一致性
            RedisUtils.publish(RedisChannelEnum.TOKEN, aid);
        } else {
            // 失败策略
            log.debug("删除Token 失败");
        }
    }

    @Override
    public void onApplicationEvent(RedisApplicationEvent event) {
        // 清除所有本地缓存事件
        if(event instanceof RedisClearApplicationEvent) {
            CacheUtils.del(CacheKey.SYSTEM);
        }

        // 清除Token 事件
        else if(event instanceof RedisTokenApplicationEvent) {
            RedisTokenApplicationEvent redisTokenApplicationEvent = (RedisTokenApplicationEvent) event;
            CacheUtils.del(CacheKey.SYSTEM, "token-" + redisTokenApplicationEvent.getValue());
        }
    }
}
