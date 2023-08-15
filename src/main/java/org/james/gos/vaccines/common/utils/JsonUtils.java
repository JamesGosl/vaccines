package org.james.gos.vaccines.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * 全局Json 操作点
 *
 * @author James Gosl
 * @since 2023/08/15 16:02
 */
@Slf4j
public class JsonUtils {
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    /**
     * JSON 转对象
     *
     * @param str JSON
     * @param clz 对象Class
     * @param <T> 泛型
     * @return 对象
     */
    public static <T> T toObj(String str, Class<T> clz) {
        try {
            return jsonMapper.readValue(str, clz);
        } catch (JsonProcessingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    /**
     * 对象转Json
     *
     * @param t 对象
     * @return JSON
     */
    public static String toStr(Object t) {
        try {
            return jsonMapper.writeValueAsString(t);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }
}
