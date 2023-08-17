package org.james.gos.vaccines.account.doman.dto;

import com.github.pagehelper.Page;
import lombok.Data;
import lombok.experimental.Accessors;
import org.james.gos.vaccines.account.doman.entity.Account;

import java.util.List;

/**
 * AccountDTO
 *
 * @author James Gosl
 * @since 2023/08/15 21:44
 */
@Data
@Accessors(chain = true)
public class AccountPageDTO {
    private Long total;
    private List<AccountDTO> accountDTOList;

    public static AccountPageDTO build(Page<Account> page, List<AccountDTO> collect) {
        return new AccountPageDTO().setTotal(page.getTotal()).setAccountDTOList(collect);
    }
}
