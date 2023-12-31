package org.james.gos.vaccines.common.resolver;

import org.james.gos.vaccines.common.annotation.Aid;
import org.james.gos.vaccines.common.util.RequestHolder;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Aid 注解 解析器
 *
 * @author James Gosl
 * @since 2023/08/17 22:13
 */
public class AidHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Aid.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        return RequestHolder.get().getId();
    }
}
