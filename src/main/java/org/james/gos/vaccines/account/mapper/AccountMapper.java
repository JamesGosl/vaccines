package org.james.gos.vaccines.account.mapper;

import org.james.gos.vaccines.account.doman.entity.Account;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * 账户表 数据层
 *
 * @author James Gosl
 * @since 2023/08/14 23:03
 */
@Repository
public interface AccountMapper extends Mapper<Account> {
}
