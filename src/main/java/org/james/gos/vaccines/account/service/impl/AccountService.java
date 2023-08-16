package org.james.gos.vaccines.account.service.impl;

import org.james.gos.vaccines.account.doman.dto.AccountDTO;
import org.james.gos.vaccines.account.doman.dto.AccountPageDTO;
import org.james.gos.vaccines.account.doman.entity.Account;
import org.james.gos.vaccines.account.doman.vo.request.AccountReq;
import org.james.gos.vaccines.account.doman.vo.request.LoginReq;
import org.james.gos.vaccines.account.doman.vo.response.AUVResp;
import org.james.gos.vaccines.account.doman.vo.response.AccountResp;
import org.james.gos.vaccines.account.mapper.AccountMapper;
import org.james.gos.vaccines.account.service.IAccountService;
import org.james.gos.vaccines.account.service.adapter.AccountAdapter;
import org.james.gos.vaccines.account.service.cache.AccountCache;
import org.james.gos.vaccines.auth.service.IAuthService;
import org.james.gos.vaccines.common.doman.enums.AuthEnum;
import org.james.gos.vaccines.common.doman.enums.YesOrNoEnum;
import org.james.gos.vaccines.common.doman.vo.request.PageBaseReq;
import org.james.gos.vaccines.common.exception.AccountRuntimeException;
import org.james.gos.vaccines.common.utils.JwtUtils;
import org.james.gos.vaccines.user.doman.vo.response.UserResp;
import org.james.gos.vaccines.user.service.IUserService;
import org.james.gos.vaccines.vaccines.doman.dto.VaccinesDTO;
import org.james.gos.vaccines.vaccines.service.IVaccinesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private IUserService userService;
    @Autowired
    private IVaccinesService vaccinesService;

    @Override
    public AccountResp login(LoginReq loginReq) {
        // 查询数据库
        Account account = selectByUsername(loginReq.getUsername());

        // 效验密码
        if (account != null && passwordEncoder.matches(loginReq.getUsername(), account.getPassword())) {
            // 构建DTO
            AccountDTO accountDTO = AccountDTO.build(account, authService.getAuth(account.getAuthId()));

            // 构建Token
            Long key = account.getId();
            String token = jwtUtils.createToken(key);

            // 加入缓存
            accountCache.setAccount(key, accountDTO);
            accountCache.setToken(key, token);
            // 构建响应
            return AccountAdapter.buildAccountResp(accountDTO, token);
        }
        throw new AccountRuntimeException("账户或密码错误");
    }

    @Override
    public void logout(Long id) {
        // 清除Token
        accountCache.delToken(id);
    }

    @Override
    public Account selectByUsername(String username) {
        Example example = new Example(Account.class);
        example.createCriteria().andEqualTo("username", username);
        return accountMapper.selectOneByExample(example);
    }

    @Override
    public Account selectById(Long id) {
        return accountMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Account> selectAll() {
        return accountMapper.selectAll();
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
    @Transactional
    public void insert(Account account) {
        account
                // 分布式ID 生成方案 通用Mapper 来完成
//                .setId((long) (Math.random() * 100000))
                // 密码加密
                .setPassword(passwordEncoder.encode(account.getPassword()));

        try {
            if (accountMapper.insertSelective(account) == YesOrNoEnum.NO.getStatus()) {
                throw new RuntimeException("账户新增失败->" + account.getUsername());
            }
        } catch (DuplicateKeyException e) {
            throw new AccountRuntimeException("账户已经存在->" + account.getUsername());
        }

        // 增加用户信息
        userService.insertUser(account.getId());
        // 增加疫苗信息
        vaccinesService.insertVaccines(account.getId());
    }

    @Override
    public void update(Account account) {
        Long id = account.getId();

        // TODO 是否需要修改密码，获取数据，判断密码是否需要修改，减少网络I/O
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        // 更新失败则有问题
        try {
            if (accountMapper.updateByPrimaryKeySelective(account) == YesOrNoEnum.YES.getStatus()) {
                accountCache.delToken(id);
                accountCache.delAccount(id);
            } else {
                throw new RuntimeException("账户更新失败->" + id);
            }
        } catch (DuplicateKeyException e) {
            throw new AccountRuntimeException("账户已经存在->" + account.getUsername());
        }
    }

    @Override
    @Transactional
    public void deleted(Long aid, Long id) {
        // 判断权限
        validate(id);

        // 删除，去缓存中删除
        accountCache.delAccount(id);
        accountCache.delToken(id);
        if (accountMapper.deleteByPrimaryKey(id) == YesOrNoEnum.NO.getStatus()) {
            throw new RuntimeException("账户删除失败->" + id);
        }

        // 删除用户信息
        userService.deletedUser(id);
    }

    @Override
    public AccountDTO getAccount(Long id) {
        return accountCache.getAccount(id);
    }

    @Override
    public List<AccountDTO> getAccountAll(Long id) {
        // 判断权限
        validate(id);
        // 缓存中查找
        return accountCache.getAccountAll();
    }

    @Override
    public AccountPageDTO getAccountAll(Long aid, PageBaseReq pageBaseReq) {
        // 判断权限
        validate(aid);
        // 缓存中查找
        return accountCache.getAccountAll(pageBaseReq);
    }

    @Override
    public AccountDTO validate(Long aid) {
        AccountDTO account = getAccount(aid);
        if(AuthEnum.of(account.getAuth()) == AuthEnum.ADMIN) {
            return account;
        }
        throw new RuntimeException("权限不足");
    }

    @Override
    public List<AUVResp> auv(Long id) {
        // 获取账户
        AccountDTO account = getAccount(id);
        // 判断权限
        AuthEnum auth = AuthEnum.of(account.getAuth());
        switch (auth) {
            case ADMIN:
            case DOCTOR:
                List<AccountDTO> accountAll = getAccountAll(id);
                return accountAll.stream().map(this::auv).collect(Collectors.toList());
            case USER:
                return Collections.singletonList(this.auv(account));
        }

        return null;
    }

    @Override
    public AUVResp auv(AccountDTO accountDTO) {
        Long aid = accountDTO.getId();
        // 用户信息
        UserResp user = userService.getUser(aid);
        // 疫苗信息
        VaccinesDTO vaccines = vaccinesService.getVaccinesByAid(aid);
        // 聚合数据
        return AccountAdapter.buildAUV(accountDTO, user, vaccines);
    }
}
