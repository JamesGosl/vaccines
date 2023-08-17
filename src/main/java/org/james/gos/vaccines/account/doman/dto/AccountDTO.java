package org.james.gos.vaccines.account.doman.dto;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.james.gos.vaccines.account.doman.entity.Account;
import org.james.gos.vaccines.common.doman.enums.AuthEnum;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * AccountDTO
 *
 * @author James Gosl
 * @since 2023/08/15 21:44
 */
@Data
@Accessors(chain = true)
public class AccountDTO {
    /** id */
    private Long id;
    /** 用户名 */
    private String username;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
    /** 权限 */
    private Integer auth;
    /** 权限描述 */
    private String description;

    public static AccountDTO build(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        BeanUtil.copyProperties(account, accountDTO);
        return accountDTO.setDescription(AuthEnum.of(account.getAuth()).getDesc());
    }

    public static List<AccountDTO> build(List<Account> account) {
        if (Objects.isNull(account)) {
            return null;
        }
        return account.stream().map(AccountDTO::build).collect(Collectors.toList());
    }
}
