package org.james.gos.vaccines.common.interceptor;

import cn.hutool.extra.spring.SpringUtil;
import org.james.gos.vaccines.account.service.IAccountService;
import org.james.gos.vaccines.common.constant.RequestKey;
import org.james.gos.vaccines.common.doman.vo.request.RequestInfo;
import org.james.gos.vaccines.common.exception.AccountRuntimeException;
import org.james.gos.vaccines.common.utils.JwtUtils;
import org.james.gos.vaccines.common.utils.RequestHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 账户验证拦截器
 *
 * @author James Gosl
 * @since 2023/08/16 15:15
 */
public class AccountHandlerInterceptor implements HandlerInterceptor {
    private static JwtUtils jwtUtils;
    private static IAccountService accountService;

    static {
        AccountHandlerInterceptor.jwtUtils = SpringUtil.getBean(JwtUtils.class);
        AccountHandlerInterceptor.accountService = SpringUtil.getBean(IAccountService.class);
    }

    // 前置
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String token = request.getHeader(RequestKey.TOKEN);
            Long aid = accountService.getAid(token);
            if(Objects.nonNull(aid)) {
                RequestHolder.set(new RequestInfo().setId(aid));
                return true;
            }
        } catch (Exception e) {
        }
        throw new AccountRuntimeException("暂未登陆");
    }

    // 中置
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    // 后置
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
