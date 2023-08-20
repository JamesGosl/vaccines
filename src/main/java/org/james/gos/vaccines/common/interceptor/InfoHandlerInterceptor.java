package org.james.gos.vaccines.common.interceptor;

import cn.hutool.extra.spring.SpringUtil;
import org.james.gos.vaccines.common.doman.vo.request.RequestInfo;
import org.james.gos.vaccines.common.exception.AccountErrorEnum;
import org.james.gos.vaccines.common.exception.AccountRuntimeException;
import org.james.gos.vaccines.common.util.JwtUtils;
import org.james.gos.vaccines.common.util.RequestHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Info HandlerInterceptor
 *
 * @author James Gosl
 * @since 2023/08/16 16:44
 */
public class InfoHandlerInterceptor implements HandlerInterceptor {
    private static JwtUtils jwtUtils;

    static {
        InfoHandlerInterceptor.jwtUtils = SpringUtil.getBean(JwtUtils.class);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            Long uid = jwtUtils.getAidOrNull(request.getParameter("token"));
            if(uid != null) {
                RequestHolder.set(new RequestInfo().setId(uid));
                return true;
            }
        } catch (Exception e) {
        }

        throw new AccountRuntimeException(AccountErrorEnum.NOT_LOGIN.getErrorMsg());
    }
}
