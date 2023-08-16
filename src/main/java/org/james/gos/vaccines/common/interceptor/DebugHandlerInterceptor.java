package org.james.gos.vaccines.common.interceptor;

import org.james.gos.vaccines.common.doman.vo.request.RequestInfo;
import org.james.gos.vaccines.common.utils.RequestHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 调试拦截器
 *
 * @author James Gosl
 * @since 2023/08/16 15:34
 */
public class DebugHandlerInterceptor implements HandlerInterceptor {

    private static final Long ADMIN = 1L;
    private static final Long DOCTOR = 2L;
    private static final Long USER = 3L;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RequestHolder.set(new RequestInfo().setId(ADMIN));

        return true;
    }
}
