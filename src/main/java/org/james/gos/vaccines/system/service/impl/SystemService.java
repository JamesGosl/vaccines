package org.james.gos.vaccines.system.service.impl;

import org.james.gos.vaccines.account.doman.dto.AccountDTO;
import org.james.gos.vaccines.account.service.IAccountService;
import org.james.gos.vaccines.common.doman.enums.AuthEnum;
import org.james.gos.vaccines.system.service.ISystemService;
import org.james.gos.vaccines.system.service.cache.SystemCache;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public String info(Long id) {
        AccountDTO account = accountService.getAccount(id);
        AuthEnum auth = AuthEnum.of(account.getAuth());
        return systemCache.info(auth);
    }
}
