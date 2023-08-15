package org.james.gos.vaccines.account.service.impl;

import org.james.gos.vaccines.account.doman.dto.AccountDTO;
import org.james.gos.vaccines.account.doman.entity.Account;
import org.james.gos.vaccines.account.doman.vo.request.AccountReq;
import org.james.gos.vaccines.account.doman.vo.request.LoginReq;
import org.james.gos.vaccines.account.doman.vo.response.AccountResp;
import org.james.gos.vaccines.account.mapper.AccountMapper;
import org.james.gos.vaccines.account.service.IAccountService;
import org.james.gos.vaccines.account.service.adapter.AccountAdapter;
import org.james.gos.vaccines.account.service.cache.AccountCache;
import org.james.gos.vaccines.auth.doman.vo.response.AuthResp;
import org.james.gos.vaccines.auth.service.IAuthService;
import org.james.gos.vaccines.common.doman.enums.AuthEnum;
import org.james.gos.vaccines.common.doman.enums.YesOrNoEnum;
import org.james.gos.vaccines.common.exception.AccountRuntimeException;
import org.james.gos.vaccines.common.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * AccountService
 *
 * @author James Gosl
 * @since 2023/08/15 19:33
 */
@Service
public class AccountService implements IAccountService {

    @Resource
    private AccountMapper accountMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private IAuthService authService;
    @Autowired
    private AccountCache accountCache;

    @Override
    public AccountResp login(LoginReq loginReq) {
        // 查询数据库
        Account account = selectByUsername(loginReq.getUsername());

        // 效验密码
        if (account != null && passwordEncoder.matches(loginReq.getUsername(), account.getPassword())) {
            // 获取权限
            AuthResp auth = authService.getAuth(account.getAuthId());
            Long key = account.getId();
            String token = jwtUtils.createToken(key);
            // 构建DTO
            AccountDTO accountDTO = AccountDTO.build(account, auth, token);
            // 加入缓存
            accountCache.setAccount(key, accountDTO);
            // Token 加入缓存
            accountCache.setToken(key, token);
            // 构建响应
            return AccountAdapter.buildAccountResp(accountDTO);
        }
        throw new AccountRuntimeException("账户或密码错误");
    }

    @Override
    public Account selectByUsername(String username) {
        Example example = new Example(Account.class);
        example.createCriteria().andEqualTo("username", username);
        return accountMapper.selectOneByExample(example);
    }

    @Override
    public void insertAndUpdate(Long aid, AccountReq accountReq) {
        // 获取当前请求的用户
        AccountDTO account = getAccount(aid);
        AuthEnum auth = AuthEnum.of(account.getAuth());
        Long id = accountReq.getId();

        // 新增操作
        if(null == id && auth == AuthEnum.ADMIN) {
            insert(AccountAdapter.buildAccount(accountReq).setAuthId(authService.getAuth(accountReq.getAuth()).getId()));
        }
        // 更新自己账号操作
        else if(aid.equals(id)) {
            update(AccountAdapter.buildAccount(accountReq));
        }
        // 更新别人账号操作
        else if(auth == AuthEnum.ADMIN) {
            update(AccountAdapter.buildAccount(accountReq).setAuthId(authService.getAuth(accountReq.getAuth()).getId()));
        }
        // 没权限
        else {
            throw new AccountRuntimeException("你这个权限有点问题");
        }
    }

    @Override
    public void insert(Account account) {
        account
                // TODO 分布式ID 生成方案
                .setId((long) (Math.random() * 100000))
                // 密码加密
                .setPassword(passwordEncoder.encode(account.getPassword()));

        try {
            if (accountMapper.insertSelective(account) != YesOrNoEnum.YES.getStatus()) {
                throw new RuntimeException("账户新增失败->" + account.getUsername());
            }
        } catch (RuntimeException e) {
            throw new AccountRuntimeException("账户已经存在->" + account.getUsername());
        }
    }

    @Override
    public void update(Account account) {
        Long id = account.getId();

        // 密码加密
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        // 更新失败则有问题
        if (accountMapper.updateByPrimaryKeySelective(account) == YesOrNoEnum.YES.getStatus()) {
            accountCache.delToken(id);
            accountCache.delAccount(id);
        } else {
            throw new RuntimeException("账户更新失败->" + id);
        }
    }


    @Override
    public AccountDTO getAccount(Long id) {
        return accountCache.getAccount(id);
    }
}
