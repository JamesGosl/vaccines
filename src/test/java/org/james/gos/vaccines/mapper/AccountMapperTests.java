package org.james.gos.vaccines.mapper;

import lombok.extern.slf4j.Slf4j;
import org.james.gos.vaccines.VaccinesApplication;
import org.james.gos.vaccines.account.doman.entity.Account;
import org.james.gos.vaccines.account.mapper.AccountMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * AccountMapperTests
 *
 * @author James Gosl
 * @since 2023/08/18 17:31
 */
@SpringBootTest(classes = VaccinesApplication.class)
@Slf4j
public class AccountMapperTests {
    @Resource
    private AccountMapper accountMapper;

    @Test
    public void selectCount() {
        log.debug("{}", accountMapper.selectCount(new Account()));
        log.debug("{}", accountMapper.selectCountByExample(new Example(Account.class)));
    }
}
