package org.james.gos.vaccines.common.doman.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.james.gos.vaccines.common.constant.RedisChannelKey;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Redis 通道
 *
 * @author James Gosl
 * @since 2023/08/18 13:27
 */
@AllArgsConstructor
@Getter
public enum RedisChannelEnum {
    ACCOUNT(RedisChannelKey.getKey(RedisChannelKey.ACCOUNT)),
    USER(RedisChannelKey.getKey(RedisChannelKey.USER)),
    VACCINES(RedisChannelKey.getKey(RedisChannelKey.VACCINES)),
    FRIEND(RedisChannelKey.getKey(RedisChannelKey.FRIEND)),

    TOKEN(RedisChannelKey.getKey(RedisChannelKey.TOKEN)),
    CLEAR(RedisChannelKey.getKey(RedisChannelKey.CLEAR)),
    ;

    private final String key;

    private static final Map<String, RedisChannelEnum> cache;

    static {
        cache = Arrays.stream(RedisChannelEnum.values()).collect(Collectors.toMap(RedisChannelEnum::getKey, Function.identity()));
    }

    public static RedisChannelEnum of(byte[] channel) {
        return cache.get(new String(channel));
    }
}
