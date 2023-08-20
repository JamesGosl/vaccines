package org.james.gos.vaccines.common.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.extra.spring.SpringUtil;

/**
 * IdWorkerUtils
 *
 * @author James Gosl
 * @since 2023/08/16 11:17
 */
public class IdWorkerUtils {
    private static Snowflake snowflake;

    static {
        IdWorkerUtils.snowflake = SpringUtil.getBean(Snowflake.class);
    }

    public static Long getId() {
        // TODO 有没有必要使用这种算法生成分布式ID
        return snowflake.nextId();
    }
}
