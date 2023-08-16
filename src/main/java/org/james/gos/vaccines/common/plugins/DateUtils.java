package org.james.gos.vaccines.common.plugins;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author James Gosl
 * @since 2023/08/16 19:26
 */
public class DateUtils {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:ms");

    public static String format(Date date) {
        return simpleDateFormat.format(date);
    }
}
