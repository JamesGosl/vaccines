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
     * 权限信息
     */
    public static final String AUTH_STRING_JSON = "auth:id_%d";

    /**
     * 权限所有信息
     */
    public static final String AUTH_STRING_JSON_ALL = "auth:all";


    /**
     * 常量替换
     */
    public static String getKey(String key, Object... objects) {
        return BASE_KEY + String.format(key, objects);
    }
}
