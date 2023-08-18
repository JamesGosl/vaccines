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
    public static final String ACCOUNT = "account:%s-%s";

    /**
     * 用户
     */
    public static final String USER = "user:%d";

    /**
     * 疫苗
     */
    public static final String VACCINES = "vaccines:%s-%s";

    /**
     * 关系
     */
    public static final String FRIEND = "account:%s-%s";


    /**
     * 常量替换
     */
    public static String getKey(String key, Object... objects) {
        return BASE_KEY + String.format(key, objects);
    }

    public static String getAccount() {
        return getAccount("*", "*");
    }

    public static String getAccount(Long aid) {
        return getAccount(aid, "*");
    }

    public static String getAccount(String username) {
        return getAccount("*", username);
    }

    public static String getAccountLike(String username) {
        return getAccount("*", "*" + username + "*");
    }

    public static String getAccount(Long aid, String username) {
        return getAccount(aid.toString(), username);
    }

    public static String getAccount(String aid, String username) {
        return BASE_KEY + String.format(ACCOUNT, aid, username);
    }

    public static String getVaccines(Long id) {
        return getVaccines(id.toString(), "*");
    }

    public static String getVaccinesAid(Long aid) {
        return getVaccines("*", aid.toString());
    }

    public static String getVaccines(Long id, Long aid) {
        return getVaccines(id.toString(), aid.toString());
    }

    public static String getVaccines(String id, String aid) {
        return BASE_KEY + String.format(VACCINES, id, aid);
    }

    public static String getFriend(Long id) {
        return getFriend(id.toString(), "*");
    }

    public static String getFriendAid(Long aid) {
        return getFriend("*", aid.toString());
    }

    public static String getFriend(Long id, Long aid) {
        return getFriend(id.toString(), aid.toString());
    }

    public static String getFriend(String id, String aid) {
        return BASE_KEY + String.format(FRIEND, id, aid);
    }
}
