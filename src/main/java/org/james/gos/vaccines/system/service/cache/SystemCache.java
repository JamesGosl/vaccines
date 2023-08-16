package org.james.gos.vaccines.system.service.cache;

import lombok.extern.slf4j.Slf4j;
import org.james.gos.vaccines.common.constant.CacheKey;
import org.james.gos.vaccines.common.constant.SystemKey;
import org.james.gos.vaccines.common.doman.enums.AuthEnum;
import org.james.gos.vaccines.common.utils.FileUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * SystemCache
 *
 * @author James Gosl
 * @since 2023/08/16 15:51
 */
@Component
@Slf4j
public class SystemCache {

    @Cacheable(cacheNames = CacheKey.SYSTEM, key = "'info' + #auth.getAuth()")
    public String info(AuthEnum auth) {
        log.debug("缓存未击中-{}", auth.getAuth());
        return FileUtils.fileToString(SystemKey.getKey(SystemKey.INFO_PATH, auth.getAuth()));
    }
}
