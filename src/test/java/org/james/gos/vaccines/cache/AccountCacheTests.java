package org.james.gos.vaccines.cache;

import lombok.extern.slf4j.Slf4j;
import org.james.gos.vaccines.VaccinesApplication;
import org.james.gos.vaccines.account.doman.entity.Account;
import org.james.gos.vaccines.account.mapper.AccountMapper;
import org.james.gos.vaccines.account.service.cache.AccountCache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tk.mybatis.mapper.entity.Example;

/**
 * AccountAccount
 *
 * @author James Gosl
 * @since 2023/08/18 16:06
 */
@SpringBootTest(classes = VaccinesApplication.class)
@Slf4j
public class AccountCacheTests {
    @Autowired
    private AccountCache accountCache;
    @Autowired
    private AccountMapper accountMapper;

    @Test
    public void getAccount() {
        // 通配符完成

        long start = System.currentTimeMillis();
        accountCache.getAccount(1L);
        log.debug("执行时间-{}ms", System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        accountCache.getAccount("admin");
        log.debug("执行时间-{}ms", System.currentTimeMillis() - start);
    }

    @Test
    public void getAccountAll() {
        // 数据库查询
        long start = System.currentTimeMillis();
        accountMapper.selectAll();
        log.debug("执行时间-{}ms", System.currentTimeMillis() - start);

        // Redis 数据载入
        start = System.currentTimeMillis();
        accountCache.getAccountAll();
        log.debug("执行时间-{}ms", System.currentTimeMillis() - start);

        // Redis 缓存
        start = System.currentTimeMillis();
        accountCache.getAccountAll();
        log.debug("执行时间-{}ms", System.currentTimeMillis() - start);

        // Caffeine 本地缓存
        start = System.currentTimeMillis();
        accountCache.getAccountAll();
        log.debug("执行时间-{}ms", System.currentTimeMillis() - start);
    }

    @Test
    public void getAccountLike() {
        final String username = "a";

        // 数据库查询
        long start = System.currentTimeMillis();
        Example example = new Example(Account.class);
        example.createCriteria().andLike("username", "%" + username + "%");
        accountMapper.selectByExample(example);
        log.debug("执行时间-{}ms", System.currentTimeMillis() - start);

        // Redis 数据载入
        start = System.currentTimeMillis();
        accountCache.getAccountLike(username);
        log.debug("执行时间-{}ms", System.currentTimeMillis() - start);

        // Redis 缓存
        start = System.currentTimeMillis();
        accountCache.getAccountLike(username);
        log.debug("执行时间-{}ms", System.currentTimeMillis() - start);

        // Caffeine 本地缓存
        start = System.currentTimeMillis();
        accountCache.getAccountLike(username);
        log.debug("执行时间-{}ms", System.currentTimeMillis() - start);
    }
}