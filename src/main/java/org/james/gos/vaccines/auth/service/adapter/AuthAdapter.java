package org.james.gos.vaccines.auth.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import org.james.gos.vaccines.auth.doman.entity.Auth;
import org.james.gos.vaccines.auth.doman.vo.response.AuthResp;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限适配器
 *
 * @author James Gosl
 * @since 2023/08/15 15:44
 */
public class AuthAdapter {

    public static List<AuthResp> buildAuthRespList(List<Auth> auths) {
        return auths.stream().map(AuthAdapter::buildAuthResp)
                        .sorted(Comparator.comparing(AuthResp::getId, Comparator.naturalOrder()))
                        .collect(Collectors.toList());
    }

    public static AuthResp buildAuthResp(Auth auth) {
        AuthResp authResp = new AuthResp();
        BeanUtil.copyProperties(auth, authResp);
        return authResp;
    }
}
