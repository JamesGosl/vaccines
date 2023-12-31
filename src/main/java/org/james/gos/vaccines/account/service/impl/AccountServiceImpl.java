package org.james.gos.vaccines.account.service.impl;

import org.james.gos.vaccines.account.doman.dto.AccountDTO;
import org.james.gos.vaccines.account.doman.entity.Account;
import org.james.gos.vaccines.account.doman.vo.request.AccountReq;
import org.james.gos.vaccines.account.doman.vo.response.AUResp;
import org.james.gos.vaccines.account.mapper.AccountMapper;
import org.james.gos.vaccines.account.service.AccountService;
import org.james.gos.vaccines.account.service.adapter.AccountAdapter;
import org.james.gos.vaccines.account.service.cache.AccountCache;
import org.james.gos.vaccines.common.doman.enums.AuthEnum;
import org.james.gos.vaccines.common.doman.enums.YesOrNoEnum;
import org.james.gos.vaccines.common.doman.vo.request.PageBaseReq;
import org.james.gos.vaccines.common.doman.vo.response.PageBaseResp;
import org.james.gos.vaccines.common.exception.AccountErrorEnum;
import org.james.gos.vaccines.common.exception.AccountRuntimeException;
import org.james.gos.vaccines.common.exception.CommonErrorEnum;
import org.james.gos.vaccines.friend.service.FriendService;
import org.james.gos.vaccines.user.service.UserService;
import org.james.gos.vaccines.vaccines.service.VaccinesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * AccountService
 *
 * @author James Gosl
 * @since 2023/08/15 19:33
 */
@Service
public class AccountServiceImpl implements AccountService {
    @Resource
    private AccountMapper accountMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountCache accountCache;
    @Autowired
    private UserService userService;
    @Autowired
    private VaccinesService vaccinesService;
    @Autowired
    private FriendService friendService;

    public AccountDTO login(String username, String password) {
        Example example = new Example(Account.class);
        example.createCriteria().andEqualTo("username", username);
        Account account = accountMapper.selectOneByExample(example);

        if (!Objects.isNull(account) && passwordEncoder.matches(password, account.getPassword())) {
            return AccountDTO.build(account);
        }
        throw new AccountRuntimeException(AccountErrorEnum.LOGIN);
    }

    @Override
    public void insertAndUpdate(Long aid, AccountReq accountReq) {
        // 获取当前请求的用户
        AccountDTO account = getAccount(aid);
        AuthEnum auth = AuthEnum.of(account.getAuth());
        Long id = accountReq.getId();

        // 每次都强制更新密码
        accountReq.setPassword(passwordEncoder.encode(accountReq.getPassword()));

        // 新增操作
        if(Objects.isNull(id) && Objects.equals(AuthEnum.ADMIN, auth)) {
            insert(AccountAdapter.buildAccount(accountReq));
        }
        // 更新自己账号操作 不允许更新权限
        else if(Objects.equals(aid, id)) {
            update(AccountAdapter.buildAccount(accountReq).setAuth(null));
        }
        // 更新别人账号操作
        else if(Objects.equals(AuthEnum.ADMIN, auth)) {
            update(AccountAdapter.buildAccount(accountReq));
        }
        // 没权限
        else {
            throw new AccountRuntimeException(AccountErrorEnum.NOT_AUTH);
        }
    }

    @Transactional
    public void insert(Account account) {
        try {
            if (Objects.equals(YesOrNoEnum.NO, accountCache.inAccount(account))) {
                throw new AccountRuntimeException(CommonErrorEnum.SYSTEM_ERROR);
            }
        } catch (DuplicateKeyException e) {
            throw new AccountRuntimeException(AccountErrorEnum.EXISTS);
        }

        // 增加用户信息
        userService.insertUser(account.getId());
        // 增加疫苗信息
        vaccinesService.insertVaccines(account.getId());
    }

    public void update(Account account) {
        // 更新失败则有问题
        try {
            if (Objects.equals(YesOrNoEnum.NO, accountCache.upAccount(account))) {
                throw new AccountRuntimeException(CommonErrorEnum.SYSTEM_ERROR);
            }
        } catch (DuplicateKeyException e) {
            throw new AccountRuntimeException(AccountErrorEnum.EXISTS);
        }
    }

    @Override
    @Transactional
    public void deleted(Long aid, Long id) {
        if(Objects.equals(aid, id))
            throw new AccountRuntimeException(AccountErrorEnum.NOT_OPTIONS_THIS);

        // 判断权限
        validate(aid);

        // 删除
        if (Objects.equals(YesOrNoEnum.NO, accountCache.delAccount(getAccount(id)))) {
            throw new AccountRuntimeException(CommonErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public AccountDTO getAccount(Long aid) {
        return accountCache.getAccount(aid);
    }

    @Override
    public List<AccountDTO> getAccountAll(Long aid) {
        // 判断权限
        validate(aid);
        // 缓存中查找
        return accountCache.getAccountAll();
    }

    @Override
    public PageBaseResp<AccountDTO> getAccountPage(Long aid, PageBaseReq request) {
        // 判断权限
        validate(aid);
        // 缓存中查找
        return accountCache.getAccountPage(request);
    }

    @Override
    public AccountDTO validate(Long aid) {
        AccountDTO account = getAccount(aid);
        if(Objects.equals(AuthEnum.ADMIN, AuthEnum.of(account.getAuth()))) {
            return account;
        }
        throw new AccountRuntimeException(AccountErrorEnum.NOT_AUTH);
    }

    @Override
    public List<AUResp> au(Long aid, String username) {
        AccountDTO accountDTO = getAccount(aid);
        List<AccountDTO> accounts = null;

        switch (AuthEnum.of(accountDTO.getAuth())) {
            // 支持模糊查询
            case DOCTOR:
                accounts = accountCache.getAccountLike(username);
                break;

            // 必须精确查询
            case USER:
                accounts = Collections.singletonList(accountCache.getAccount(username));
                break;

            default:
        }

        if (Objects.isNull(accounts)) {
            return Collections.emptyList();
        }

        // 数据聚合
        return accounts.stream()
                // 查找的必须是用户
                .filter(account -> Objects.equals(AuthEnum.USER, AuthEnum.of(account.getAuth())))
                .map(acc -> AccountAdapter.buildAU(acc, userService.getUser(acc.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public void friend(Long aid, Long id) {
        if (Objects.equals(aid, id))
            throw new AccountRuntimeException(AccountErrorEnum.NOT_OPTIONS_THIS);

        // 确定这个是接种者的ID
        AccountDTO account = accountCache.getAccount(id);
        if (Objects.equals(AuthEnum.USER, AuthEnum.of(account.getAuth()))) {// 建立关系、添加消息
            friendService.insert(aid, id);
            return;
        }
        throw new AccountRuntimeException(AccountErrorEnum.NOT_AUTH);
    }
}
