package org.james.gos.vaccines.user.service.impl;

import org.james.gos.vaccines.account.doman.dto.AccountDTO;
import org.james.gos.vaccines.account.service.IAccountService;
import org.james.gos.vaccines.common.doman.enums.AuthEnum;
import org.james.gos.vaccines.common.doman.enums.YesOrNoEnum;
import org.james.gos.vaccines.common.exception.AccountRuntimeException;
import org.james.gos.vaccines.common.exception.InsertRuntimeException;
import org.james.gos.vaccines.user.doman.dto.UserDTO;
import org.james.gos.vaccines.user.doman.vo.request.UserReq;
import org.james.gos.vaccines.user.doman.vo.response.UserResp;
import org.james.gos.vaccines.user.mapper.UserMapper;
import org.james.gos.vaccines.user.service.IUserService;
import org.james.gos.vaccines.user.service.adapter.UserAdapter;
import org.james.gos.vaccines.user.service.cache.UserCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * UserService
 *
 * @author James Gosl
 * @since 2023/08/16 17:45
 */
@Service
public class UserService implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserCache userCache;
    @Autowired
    private IAccountService accountService;

    @Override
    public UserDTO getUser(Long aid) {
        return userCache.getUser(aid);
    }

    @Override
    public void updateUser(Long aid, UserReq userReq) {
        AccountDTO account = accountService.getAccount(aid);
        if (account.getId().equals(aid) || AuthEnum.of(account.getAuth()).equals(AuthEnum.ADMIN)) {
            if (YesOrNoEnum.NO.equals(userCache.upUser(UserAdapter.build(aid, userReq)))) {
                throw new RuntimeException("更新用户信息失败-" + aid);
            }
        } else {
            throw new AccountRuntimeException("权限不足");
        }
    }

    @Override
    public void insertUser(Long aid) {
        if (YesOrNoEnum.NO.equals(userCache.inUser(UserAdapter.build(aid)))) {
            throw new RuntimeException("插入用户信息失败-" + aid);
        }
    }

    @Override
    public void deletedUser(Long aid) {
        if (YesOrNoEnum.NO.equals(userCache.delUser(aid))) {
            throw new RuntimeException("删除用户信息失败-" + aid);
        }
    }
}
