package org.james.gos.vaccines.friend.service.cache;

import lombok.extern.slf4j.Slf4j;
import org.james.gos.vaccines.common.constant.CacheKey;
import org.james.gos.vaccines.common.event.ClearCacheApplicationEvent;
import org.james.gos.vaccines.common.utils.CacheUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * FriendCache
 *
 * @author James Gosl
 * @since 2023/08/17 15:00
 */
@Component
@Slf4j
public class FriendCache implements ApplicationListener<ClearCacheApplicationEvent> {

    @Override
    public void onApplicationEvent(ClearCacheApplicationEvent event) {
        Object source = event.getSource();
        log.debug("清除缓存 -> {}", source.toString());

        CacheUtils.del(CacheKey.FRIEND);
    }
}
