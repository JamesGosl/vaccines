package org.james.gos.vaccines.system.service.cache;

import lombok.extern.slf4j.Slf4j;
import org.james.gos.vaccines.common.constant.CacheKey;
import org.james.gos.vaccines.common.constant.RedisKey;
import org.james.gos.vaccines.common.constant.SystemKey;
import org.james.gos.vaccines.common.doman.enums.AuthEnum;
import org.james.gos.vaccines.common.event.AccountUpdateApplicationEvent;
import org.james.gos.vaccines.common.event.ClearCacheApplicationEvent;
import org.james.gos.vaccines.common.utils.CacheUtils;
import org.james.gos.vaccines.common.utils.FileUtils;
import org.james.gos.vaccines.common.utils.RedisUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Component;

/**
 * SystemCache
 *
 * @author James Gosl
 * @since 2023/08/16 15:51
 */
@Component
@Slf4j
public class SystemCache implements ApplicationListener<ApplicationEvent> {

    @Cacheable(cacheNames = CacheKey.SYSTEM, key = "'info' + #auth.getAuth()")
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
        RedisUtils.del(RedisKey.getKey(RedisKey.TOKEN, aid));
    }

    public void clear() {
        CacheUtils.del(CacheKey.SYSTEM);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof ClearCacheApplicationEvent) {
            log.debug("清除缓存 -> {}", event.getSource());
            clear();
        } else if(event instanceof AccountUpdateApplicationEvent) {
            clear();
            clearToken((Long) event.getSource());
        }
    }
}
