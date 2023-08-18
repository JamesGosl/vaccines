package org.james.gos.vaccines.system.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import org.james.gos.vaccines.account.doman.dto.AccountDTO;
import org.james.gos.vaccines.system.domain.vo.response.LoginResp;

/**
 * SystemAdapter
 *
 * @author James Gosl
 * @since 2023/08/17 17:33
 */
public class SystemAdapter {

    public static LoginResp buildLoginResp(AccountDTO account, String token) {
        LoginResp loginResp = new LoginResp();
        BeanUtil.copyProperties(account, loginResp);
        return loginResp.setToken(token);
    }
}
