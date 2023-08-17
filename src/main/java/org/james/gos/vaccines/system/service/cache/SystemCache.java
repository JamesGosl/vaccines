package org.james.gos.vaccines.system.service.cache;

import lombok.extern.slf4j.Slf4j;
import org.james.gos.vaccines.common.constant.CacheKey;
import org.james.gos.vaccines.common.constant.SystemKey;
import org.james.gos.vaccines.common.doman.enums.AuthEnum;
import org.james.gos.vaccines.common.event.ClearCacheApplicationEvent;
import org.james.gos.vaccines.common.utils.CacheUtils;
import org.james.gos.vaccines.common.utils.FileUtils;
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
public class SystemCache implements ApplicationListener<ClearCacheApplicationEvent> {


    @Cacheable(cacheNames = CacheKey.SYSTEM, key = "'info' + #auth.getAuth()")
    public String info(AuthEnum auth) {
        log.debug("缓存未击中-{}", auth.getAuth());
        return FileUtils.fileToString(SystemKey.getKey(SystemKey.INFO_PATH, auth.getAuth()));
    }

    @Override
    public void onApplicationEvent(ClearCacheApplicationEvent event) {
        Object source = event.getSource();
        log.debug("清除缓存 -> {}", source.toString());

        CacheUtils.del(CacheKey.SYSTEM);
    }
}
