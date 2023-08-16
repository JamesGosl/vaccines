package org.james.gos.vaccines.common.constant;

/**
 * SystemKey
 *
 * @author James Gosl
 * @since 2023/08/16 15:53
 */
public class SystemKey {

    /**初始化文件地址*/
    public static final String INFO_PATH = "/info/%d.json";

    /**
     * 常量替换
     */
    public static String getKey(String key, Object... objects) {
        return String.format(key, objects);
    }
}
