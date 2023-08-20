package org.james.gos.vaccines.common.util;

import org.james.gos.vaccines.common.doman.vo.request.RequestInfo;

/**
 * 请求上下文
 *
 * @author James Gosl
 * @since 2023/08/15 21:32
 */
public class RequestHolder {
    private static final ThreadLocal<RequestInfo> threadLocal = new ThreadLocal<>();

    public static void set(RequestInfo requestInfo) {
        threadLocal.set(requestInfo);
    }

    public static RequestInfo get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
