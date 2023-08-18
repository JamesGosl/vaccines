package org.james.gos.vaccines.common.constant;

/**
 * Redis 通道Key
 *
 * @author James Gosl
 * @since 2023/08/18 13:09
 */
public class RedisChannelKey {
    private static final String CHANNEL_PATTERN = "vaccines.del.%s";

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 账户
     */
    public static final String ACCOUNT = "account";

    /**
     * 用户
     */
    public static final String USER = "user";

    /**
     * 疫苗
     */
    public static final String VACCINES = "vaccines";

    /**
     * 关系
     */
    public static final String FRIEND = "friend";

    /**
     * 本地缓存
     */
    public static final String CLEAR = "clear";

    public static String getKey(String key) {
        return String.format(CHANNEL_PATTERN, key);
    }
}
