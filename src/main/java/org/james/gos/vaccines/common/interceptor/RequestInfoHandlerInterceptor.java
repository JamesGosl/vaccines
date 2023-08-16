package org.james.gos.vaccines.common.interceptor;

import cn.hutool.extra.spring.SpringUtil;
import org.james.gos.vaccines.common.constant.RequestKey;
import org.james.gos.vaccines.common.doman.vo.request.RequestInfo;
import org.james.gos.vaccines.common.exception.AccountRuntimeException;
import org.james.gos.vaccines.common.utils.JwtUtils;
import org.james.gos.vaccines.common.utils.RequestHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录信息拦截器
 *
 * @author James Gosl
 * @since 2023/08/16 15:16
 */
public class RequestInfoHandlerInterceptor implements HandlerInterceptor {
    private static JwtUtils jwtUtils;

    static {
        RequestInfoHandlerInterceptor.jwtUtils = SpringUtil.getBean(JwtUtils.class);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            Long uid = jwtUtils.getUidOrNull(request.getHeader(RequestKey.TOKEN));
            RequestHolder.set(new RequestInfo().setId(uid));
        } catch (Exception e) {
            throw new AccountRuntimeException("你这有毛病啊");
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestHolder.remove();
    }
}
