package org.james.gos.vaccines.account.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import org.james.gos.vaccines.account.doman.dto.AccountDTO;
import org.james.gos.vaccines.account.doman.dto.AccountPageDTO;
import org.james.gos.vaccines.account.doman.entity.Account;
import org.james.gos.vaccines.account.doman.vo.request.AccountReq;
import org.james.gos.vaccines.account.doman.vo.response.AUVResp;
import org.james.gos.vaccines.account.doman.vo.response.AccountPageResp;
import org.james.gos.vaccines.account.doman.vo.response.AccountResp;
import org.james.gos.vaccines.auth.doman.vo.response.AuthResp;
import org.james.gos.vaccines.common.doman.enums.YesOrNoEnum;
import org.james.gos.vaccines.common.plugins.DateUtils;
import org.james.gos.vaccines.user.doman.vo.response.UserResp;
import org.james.gos.vaccines.vaccines.doman.dto.VaccinesDTO;

import java.util.List;
import java.util.stream.Collectors;

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

    public static AccountPageResp buildAccountPageResp(AccountPageDTO accountPageDTO) {


        return null;
    }

    public static AccountResp buildAccountResp(AccountDTO accountDTO) {
        AccountResp accountResp = new AccountResp();
        BeanUtil.copyProperties(accountDTO, accountResp);
        return accountResp.setCreateTime(DateUtils.format(accountDTO.getCreateTime()))
                .setUpdateTime(DateUtils.format(accountDTO.getUpdateTime()));
    }

    public static List<AccountResp> buildAccountResp(List<AccountDTO> accountDTO) {
        return accountDTO.stream().map(AccountAdapter::buildAccountResp).collect(Collectors.toList());
    }

    public static AccountResp buildAccountResp(AccountDTO accountDTO, String token) {
        AccountResp accountResp = new AccountResp();
        BeanUtil.copyProperties(accountDTO, accountResp);
        return accountResp.setToken(token);
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

    public static AUVResp buildAUV(AccountDTO accountDTO, UserResp user, VaccinesDTO vaccines) {
        AUVResp auvResp = new AUVResp();
        if(vaccines != null) {
            auvResp.setId(vaccines.getId());
            if (vaccines.getContent() != null) {
                auvResp.setState(YesOrNoEnum.YES.getStatus());
            } else {
                auvResp.setState(YesOrNoEnum.NO.getStatus());
            }
        }
        auvResp.setUsername(accountDTO.getUsername());
        auvResp.setName(user.getName());
        return auvResp;
    }
}
