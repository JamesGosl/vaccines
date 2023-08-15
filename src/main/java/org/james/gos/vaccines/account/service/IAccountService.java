package org.james.gos.vaccines.account.service;

import org.james.gos.vaccines.account.doman.dto.AccountDTO;
import org.james.gos.vaccines.account.doman.entity.Account;
import org.james.gos.vaccines.account.doman.vo.request.AccountReq;
import org.james.gos.vaccines.account.doman.vo.request.LoginReq;
import org.james.gos.vaccines.account.doman.vo.response.AccountResp;

/**
 * 账户表 逻辑层
 *
 * @author James Gosl
 * @since 2023/08/15 19:23
 */
public interface IAccountService {

    /**
     * 账户登录
     *
     * @param loginReq 登录信息
     * @return 令牌信息
     */
    AccountResp login(LoginReq loginReq);

    /**
     * 根据用户名查询账户
     *
     * @param username 用户名
     * @return 账户信息
     */
    Account selectByUsername(String username);

    /**
     * 增加更改账户
     *
     * @param accountReq 账户信息
     */
    void insertAndUpdate(Long aid, AccountReq accountReq);

    /**
     * 获得账号信息
     *
     * @param id 账号id
     * @return 账号信息
     */
    AccountDTO getAccount(Long id);

    /**
     * 增加账户
     * @param account 账户信息
     */
    void insert(Account account);

    /**
     * 更新账户
     * @param account 账户信息
     */
    void update(Account account);
}
