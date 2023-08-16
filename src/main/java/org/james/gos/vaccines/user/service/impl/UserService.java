package org.james.gos.vaccines.user.service.impl;

import org.james.gos.vaccines.account.doman.dto.AccountDTO;
import org.james.gos.vaccines.account.service.IAccountService;
import org.james.gos.vaccines.common.doman.enums.AuthEnum;
import org.james.gos.vaccines.common.doman.enums.YesOrNoEnum;
import org.james.gos.vaccines.common.exception.AccountRuntimeException;
import org.james.gos.vaccines.common.exception.InsertRuntimeException;
import org.james.gos.vaccines.user.doman.vo.request.UserReq;
import org.james.gos.vaccines.user.doman.vo.response.UserResp;
import org.james.gos.vaccines.user.mapper.UserMapper;
import org.james.gos.vaccines.user.service.IUserService;
import org.james.gos.vaccines.user.service.adapter.UserAdapter;
import org.james.gos.vaccines.user.service.cache.UserCache;
import org.james.gos.vaccines.vaccines.service.apadter.VaccinesAdapter;
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
    public UserResp getUser(Long aid) {
        return UserAdapter.build(userCache.getUser(aid));
    }

    @Override
    public void updateUser(Long aid, UserReq userReq) {
        AccountDTO account = accountService.getAccount(aid);
        if (account.getId().equals(aid) || AuthEnum.of(account.getAuth()).equals(AuthEnum.ADMIN)) {
            userCache.updateUser(UserAdapter.build(aid, userReq));
        } else {
            throw new AccountRuntimeException("权限不足");
        }
    }

    @Override
    public void insertUser(Long aid) {
        if (userMapper.insertSelective(UserAdapter.build(aid)) == YesOrNoEnum.NO.getStatus()) {
            throw new InsertRuntimeException("插入用户信息失败-" + aid);
        }
    }

    @Override
    public void deletedUser(Long aid) {
        userCache.deletedUser(aid);
    }
}
