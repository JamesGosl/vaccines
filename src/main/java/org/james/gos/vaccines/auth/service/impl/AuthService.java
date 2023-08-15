package org.james.gos.vaccines.auth.service.impl;

import org.james.gos.vaccines.auth.doman.vo.response.AuthResp;
import org.james.gos.vaccines.auth.mapper.AuthMapper;
import org.james.gos.vaccines.auth.service.IAuthService;
import org.james.gos.vaccines.auth.service.adapter.AuthAdapter;
import org.james.gos.vaccines.auth.service.cache.AuthCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * AuthService
 *
 * @author James Gosl
 * @since 2023/08/15 15:35
 */
@Service
public class AuthService implements IAuthService {

    @Resource
    private AuthMapper authMapper;

    @Autowired
    private AuthCache authCache;

    @Override
    public List<AuthResp> getAuthAll() {
        return AuthAdapter.buildAuthRespList(authCache.getAuthAll());
    }

    @Override
    public AuthResp getAuth(Long id) {
        return AuthAdapter.buildAuthResp(authCache.getAuth(id));
    }
}
