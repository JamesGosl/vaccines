package org.james.gos.vaccines.common.constant;

/**
 * Redis key 常量区
 *
 * @author James Gosl
 * @since 2023/08/15 15:52
 */
public class RedisKey {
    private static final String BASE_KEY = "vaccines:";

    /**
     * 令牌
     */
    public static final String TOKEN = "token:%d";

    /**
     * 账户
     */
    public static final String ACCOUNT = "account:%d";


    /**
     * 常量替换
     */
    public static String getKey(String key, Object... objects) {
        return BASE_KEY + String.format(key, objects);
    }
}
