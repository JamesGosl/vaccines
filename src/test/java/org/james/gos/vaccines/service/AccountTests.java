package org.james.gos.vaccines.service;

import lombok.extern.slf4j.Slf4j;
import org.james.gos.vaccines.VaccinesApplication;
import org.james.gos.vaccines.account.doman.entity.Account;
import org.james.gos.vaccines.account.doman.vo.request.AccountReq;
import org.james.gos.vaccines.account.doman.vo.request.LoginReq;
import org.james.gos.vaccines.account.doman.vo.response.AccountResp;
import org.james.gos.vaccines.account.service.IAccountService;
import org.james.gos.vaccines.common.constant.RedisKey;
import org.james.gos.vaccines.common.doman.enums.AuthEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * AccountTests
 *
 * @author James Gosl
 * @since 2023/08/15 19:49
 */
@SpringBootTest(classes = VaccinesApplication.class)
@Slf4j
public class AccountTests {

    @Autowired
    private IAccountService accountService;

    @Test
    public void selectByUsername() {
        Account account = accountService.selectByUsername("admin");
        assert account != null;
    }

    @Test
    public void login() {
        LoginReq loginReq = new LoginReq();
        loginReq.setUsername("admin");
        loginReq.setPassword("admin");
        log.debug(accountService.login(loginReq).toString());
    }

    @Test
    public void redisKey() {
        log.debug(RedisKey.getKey(RedisKey.TOKEN, 1L));
    }

    @Test
    public void insertAndUpdate() {
        LoginReq loginReq = new LoginReq();
        loginReq.setUsername("admin");
        loginReq.setPassword("admin");
        AccountResp login = accountService.login(loginReq);

        AccountReq accountReq = new AccountReq();
        accountReq.setAuth(AuthEnum.ADMIN.getAuth());
        accountReq.setUsername("root");
        accountReq.setPassword("root");
        accountService.insertAndUpdate(login.getId(), accountReq);
    }
}
