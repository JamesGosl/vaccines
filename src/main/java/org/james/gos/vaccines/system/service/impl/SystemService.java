package org.james.gos.vaccines.system.service.impl;

import org.james.gos.vaccines.account.doman.dto.AccountDTO;
import org.james.gos.vaccines.account.service.IAccountService;
import org.james.gos.vaccines.common.doman.enums.AuthEnum;
import org.james.gos.vaccines.common.doman.enums.RedisChannelEnum;
import org.james.gos.vaccines.common.utils.JwtUtils;
import org.james.gos.vaccines.common.utils.RedisUtils;
import org.james.gos.vaccines.system.domain.vo.response.LoginResp;
import org.james.gos.vaccines.system.service.ISystemService;
import org.james.gos.vaccines.system.service.adapter.SystemAdapter;
import org.james.gos.vaccines.system.service.cache.SystemCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * SystemService
 *
 * @author James Gosl
 * @since 2023/08/16 15:50
 */
@Service
public class SystemService implements ISystemService {
    @Autowired
    private IAccountService accountService;
    @Autowired
    private SystemCache systemCache;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public LoginResp login(String username, String password) {
        AccountDTO account = accountService.login(username, password);
        Long aid = account.getId();
        String token = jwtUtils.createToken(aid);
        systemCache.setToken(aid, token);
        return SystemAdapter.buildLoginResp(account, token);
    }

    @Override
    public void logout(Long aid) {
        systemCache.clearToken(aid);
    }

    @Override
    public String info(Long aid) {
        AccountDTO account = accountService.getAccount(aid);
        AuthEnum auth = AuthEnum.of(account.getAuth());
        return systemCache.info(auth);
    }

    @Override
    public boolean verify(String token) {
        if(Objects.isNull(token) || token.isEmpty())
            return false;

        Long aid = jwtUtils.getAidOrNull(token);
        if (Objects.isNull(aid)) {
            return false;
        }
        // 有可能token失效了，需要校验是不是和最新token一致
        return Objects.equals(token, systemCache.getToken(aid));
    }

    @Override
    public Long getAid(String token) {
        return verify(token) ? jwtUtils.getAidOrNull(token) : null;
    }

    @Override
    public void clear(Long aid) {
        AccountDTO account = accountService.getAccount(aid);
        AuthEnum auth = AuthEnum.of(account.getAuth());
        switch (auth) {
            case ADMIN:
            case DOCTOR:
            case USER:
                // Redis 发布/订阅 保证数据一致性
                RedisUtils.publish(RedisChannelEnum.CLEAR, null);
        }
    }
}
