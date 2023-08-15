package org.james.gos.vaccines.common.utils;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * 操作本地缓存工具类
 *
 * @author James Gosl
 * @since 2023/08/15 17:14
 */
public class CacheUtils {
    private static CacheManager cacheManager;

    static {
        CacheUtils.cacheManager = SpringUtil.getBean(CacheManager.class);
    }

    public static <T> T get(String name, String key) {
        Object obj = getKey(name, key);
        return obj == null ? null : (T) obj;
    }

    public static Object getKey(String name, String key) {
        Cache cache = getCache(name);
        if (cache != null) {
            Cache.ValueWrapper value = cache.get(key);
            if(value != null) {
                return value.get();
            }
        }
        return null;
    }

    public static Cache getCache(String name) {
        return cacheManager.getCache(name);
    }
}
