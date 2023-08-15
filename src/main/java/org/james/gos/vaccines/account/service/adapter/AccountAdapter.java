package org.james.gos.vaccines.account.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import org.james.gos.vaccines.account.doman.dto.AccountDTO;
import org.james.gos.vaccines.account.doman.entity.Account;
import org.james.gos.vaccines.account.doman.vo.request.AccountReq;
import org.james.gos.vaccines.account.doman.vo.response.AccountResp;
import org.james.gos.vaccines.auth.doman.vo.response.AuthResp;
import org.james.gos.vaccines.common.doman.enums.AuthEnum;

/**
 * Account Adapter
 *
 * @author James Gosl
 * @since 2023/08/15 20:48
 */
@Data
public class AccountAdapter {

    public static AccountResp buildAccountResp(Account account, AuthResp auth, String token) {
        AccountResp accountResp = new AccountResp();
        BeanUtil.copyProperties(account, accountResp);
        BeanUtil.copyProperties(auth, accountResp);
        return accountResp.setToken(token);
    }

    public static AccountResp buildAccountResp(AccountDTO accountDTO) {
        AccountResp accountResp = new AccountResp();
        BeanUtil.copyProperties(accountDTO, accountResp);
        return accountResp;
    }

    public static Account buildAccount(AccountReq accountReq) {
        Account account = new Account();
        BeanUtil.copyProperties(accountReq, account);
        return account;
    }

    public static Account buildAccount(AccountReq accountReq, String password) {
        Account account = new Account();
        BeanUtil.copyProperties(accountReq, account);
        return account.setPassword(password);
    }
}
