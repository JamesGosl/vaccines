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

    // 为什么要单独测试，这就是不使用权限校验框架的坏处，在服务端单独校验
    // 使用了权限校验框架，就可以在接口层直接效验

    @Test
    public void insertAndUpdateAdmin() {
        LoginReq loginReq = new LoginReq();
        loginReq.setUsername("admin");
        loginReq.setPassword("admin");
        AccountResp login = accountService.login(loginReq);

        // 增加用户
        try {
            AccountReq accountReq = new AccountReq();
            accountReq.setAuth(AuthEnum.ADMIN.getAuth());
            accountReq.setUsername("user");
            accountReq.setPassword("user");
            accountService.insertAndUpdate(login.getId(), accountReq);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        // 修改用户
        try {
            AccountReq accountReq = new AccountReq();
            accountReq.setId(72893L);
            accountReq.setAuth(AuthEnum.USER.getAuth());
            accountReq.setUsername("root");
            accountReq.setPassword("root");
            accountService.insertAndUpdate(login.getId(), accountReq);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        // 修改用户名为已存在的
        try {
            AccountReq accountReq = new AccountReq();
            accountReq.setId(1691654507110469632L);
            accountReq.setAuth(AuthEnum.USER.getAuth());
            accountReq.setUsername("root");
            accountReq.setPassword("root");
            accountService.insertAndUpdate(login.getId(), accountReq);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Test
    public void insertAndUpdateUser() {
        LoginReq loginReq = new LoginReq();
        loginReq.setUsername("root");
        loginReq.setPassword("root");
        AccountResp login = accountService.login(loginReq);

        // 增加用户
        try {
            AccountReq accountReq = new AccountReq();
            accountReq.setAuth(AuthEnum.ADMIN.getAuth());
            accountReq.setUsername("root");
            accountReq.setPassword("root");
            accountService.insertAndUpdate(login.getId(), accountReq);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        // 修改自己
        try {
            AccountReq accountReq = new AccountReq();
            accountReq.setAuth(AuthEnum.ADMIN.getAuth());
            accountReq.setUsername("root");
            accountReq.setPassword("root");
            accountService.insertAndUpdate(login.getId(), accountReq);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        // 修改其他
        try {
            AccountReq accountReq = new AccountReq();
            accountReq.setId(1L);
            accountReq.setAuth(AuthEnum.ADMIN.getAuth());
            accountReq.setUsername("root");
            accountReq.setPassword("root");
            accountService.insertAndUpdate(login.getId(), accountReq);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Test
    public void deletedAdmin() {
        LoginReq loginReq = new LoginReq();
        loginReq.setUsername("admin");
        loginReq.setPassword("admin");
        AccountResp login = accountService.login(loginReq);

        try {
            accountService.deleted(login.getId(), 1691771048724402177L);
        } catch (Exception e) {
            e.printStackTrace();
//            log.error(e.getMessage());
        }
    }

    @Test
    public void deletedUser() {
        LoginReq loginReq = new LoginReq();
        loginReq.setUsername("root");
        loginReq.setPassword("root");
        AccountResp login = accountService.login(loginReq);

        try {
            accountService.deleted(login.getId(), 72893L);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Test
    public void getAccountAllAdmin() {
        LoginReq loginReq = new LoginReq();
        loginReq.setUsername("admin");
        loginReq.setPassword("admin");
        AccountResp login = accountService.login(loginReq);

        try {
            for (int i = 0; i < 10; i++) {
                accountService.getAccountAll(login.getId());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Test
    public void getAccountAllUser() {
        LoginReq loginReq = new LoginReq();
        loginReq.setUsername("root");
        loginReq.setPassword("root");
        AccountResp login = accountService.login(loginReq);

        try {
            accountService.getAccountAll(login.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
