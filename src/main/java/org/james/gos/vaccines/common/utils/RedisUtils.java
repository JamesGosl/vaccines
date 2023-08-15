package org.james.gos.vaccines.common.utils;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 全局Redis 访问点
 *
 * @author James Gosl
 * @since 2023/08/15 15:58
 */
@Slf4j
public class RedisUtils {
    private static StringRedisTemplate stringRedisTemplate;

    static {
        RedisUtils.stringRedisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
    }

    /**
     * 普通存入Redis
     *
     * @param key   键
     * @param value 值
     * @return true 成功，false 失败
     */
    public static Boolean set(String key, Object value) {
        try {
            stringRedisTemplate.opsForValue().set(key, objToStr(value));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }


    /**
     * 普通取出值
     *
     * @param key 键
     * @return 值
     */
    public static String get(String key) {
        return key == null ? null : stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 取出指定类型的值
     *
     * @param key 键
     * @param klass 类型
     * @param <T> 泛型
     * @return 类型值
     */
    public static <T> T get(String key, Class<T> klass) {
        String s = get(key);
        return toBeanOrNull(s, klass);
    }

    static <T> T toBeanOrNull(String json, Class<T> tClass) {
        return json == null ? null : JsonUtils.toObj(json, tClass);
    }

    /**
     * 工具类内 做此操作的点
     *
     * @param value 对象
     * @return String
     */
    private static String objToStr(Object value) {
        return JsonUtils.toStr(value);
    }
}
