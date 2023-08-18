package org.james.gos.vaccines.common.utils;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.james.gos.vaccines.common.doman.enums.RedisChannelEnum;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
     * 发布事件
     *
     * @param channel 通道
     * @param value 事件
     */
    public static void publish(RedisChannelEnum channel, Object value) {
        stringRedisTemplate.convertAndSend(channel.getKey(), objToStr(value));
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

    public static Boolean setList(Map<String, Object> source) {
        try {
            stringRedisTemplate.executePipelined((RedisCallback<?>) connection -> {
                try {
                    // 开启管道
                    connection.openPipeline();
                    source.forEach((key, val) ->
                            connection.set(RedisSerializer.string().serialize(key),
                                    RedisSerializer.string().serialize(objToStr(val))));
                } finally {
                    // 关闭管道
                    connection.close();
                }
                return null;
            });
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 键
     * @return true 成功，false 失败
     */
    public static Boolean del(String key) {
        return stringRedisTemplate.delete(key);
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
     * 模糊查询
     *
     * @param pattern 键
     * @return 值
     */
    public static Set<String> keys(String pattern) {
        return pattern == null ? null : stringRedisTemplate.keys(pattern);
    }

    /**
     * 批量取出值
     *
     * @param keys 键
     * @return 值
     */
    public static List<String> getList(Set<String> keys) {
        return keys == null || keys.size() < 1 ? null : stringRedisTemplate.opsForValue().multiGet(keys);
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

    public static <T> T keysOne(String key, Class<T> klass) {
        Set<String> vals = keys(key);
        if (Objects.isNull(vals) || vals.isEmpty())
            return null;

        return get(vals.iterator().next(), klass);
    }

    public static <T> List<T> getList(Set<String> keys, Class<T> klass) {
        List<String> list = getList(keys);
        return list == null ? null : list.stream().map(str -> toBeanOrNull(str, klass)).collect(Collectors.toList());
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
