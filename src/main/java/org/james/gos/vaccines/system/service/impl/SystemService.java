package org.james.gos.vaccines.system.service.impl;

import org.james.gos.vaccines.account.doman.dto.AccountDTO;
import org.james.gos.vaccines.account.service.IAccountService;
import org.james.gos.vaccines.auth.service.IAuthService;
import org.james.gos.vaccines.common.doman.enums.AuthEnum;
import org.james.gos.vaccines.common.event.ClearCacheApplicationEvent;
import org.james.gos.vaccines.friend.service.IFriendService;
import org.james.gos.vaccines.system.service.ISystemService;
import org.james.gos.vaccines.system.service.cache.SystemCache;
import org.james.gos.vaccines.user.service.IUserService;
import org.james.gos.vaccines.vaccines.service.IVaccinesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

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
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public String info(Long aid) {
        AccountDTO account = accountService.getAccount(aid);
        AuthEnum auth = AuthEnum.of(account.getAuth());
        return systemCache.info(auth);
    }

    @Override
    public void clear(Long aid) {
        AccountDTO account = accountService.getAccount(aid);
        AuthEnum auth = AuthEnum.of(account.getAuth());
        switch (auth) {
            case ADMIN:
            case DOCTOR:
            case USER:
                applicationEventPublisher.publishEvent(new ClearCacheApplicationEvent(auth.name() + "-" + aid));
        }
    }
}
