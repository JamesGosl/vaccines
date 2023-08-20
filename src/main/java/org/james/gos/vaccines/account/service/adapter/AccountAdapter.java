package org.james.gos.vaccines.account.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import org.james.gos.vaccines.account.doman.dto.AccountDTO;
import org.james.gos.vaccines.account.doman.entity.Account;
import org.james.gos.vaccines.account.doman.vo.request.AccountReq;
import org.james.gos.vaccines.account.doman.vo.response.AUResp;
import org.james.gos.vaccines.account.doman.vo.response.AccountResp;
import org.james.gos.vaccines.common.doman.enums.AuthEnum;
import org.james.gos.vaccines.common.exception.AccountRuntimeException;
import org.james.gos.vaccines.common.util.DateUtils;
import org.james.gos.vaccines.user.doman.dto.UserDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Account Adapter
 *
 * @author James Gosl
 * @since 2023/08/15 20:48
 */
@Data
public class AccountAdapter {

    public static AccountResp buildAccountResp(AccountDTO accountDTO) {
        AccountResp accountResp = new AccountResp();
        BeanUtil.copyProperties(accountDTO, accountResp);
        return accountResp.setCreateTime(DateUtils.format(accountDTO.getCreateTime()))
                .setUpdateTime(DateUtils.format(accountDTO.getUpdateTime()));
    }

    public static List<AccountResp> buildAccountResp(List<AccountDTO> accountDTO) {
        return accountDTO.stream().map(AccountAdapter::buildAccountResp).collect(Collectors.toList());
    }

    public static Account buildAccount(AccountReq accountReq) {
        if (Objects.isNull(AuthEnum.of(accountReq.getAuth()))) {
            throw new AccountRuntimeException("权限不正常");
        }

        Account account = new Account();
        BeanUtil.copyProperties(accountReq, account);
        return account;
    }

    public static AUResp buildAU(AccountDTO account, UserDTO user) {
        AUResp auResp = new AUResp();
        auResp.setId(account.getId());
        auResp.setUsername(account.getUsername());
        auResp.setName(user.getName());
        auResp.setPhone(user.getPhone());
        auResp.setAddress(user.getAddress());
        return auResp;
    }
}
