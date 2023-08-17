package org.james.gos.vaccines.account.service.cache;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import lombok.extern.slf4j.Slf4j;
import org.james.gos.vaccines.account.doman.dto.AccountDTO;
import org.james.gos.vaccines.account.doman.dto.AccountPageDTO;
import org.james.gos.vaccines.account.doman.entity.Account;
import org.james.gos.vaccines.account.mapper.AccountMapper;
import org.james.gos.vaccines.account.service.IAccountService;
import org.james.gos.vaccines.common.constant.CacheKey;
import org.james.gos.vaccines.common.constant.RedisKey;
import org.james.gos.vaccines.common.doman.vo.request.PageBaseReq;
import org.james.gos.vaccines.common.event.ClearCacheApplicationEvent;
import org.james.gos.vaccines.common.utils.CacheUtils;
import org.james.gos.vaccines.common.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * 账户信息 缓存
 *
 * @author James Gosl
 * @since 2023/08/15 20:34
 */
@Component
@Slf4j
public class AccountCache implements ApplicationListener<ClearCacheApplicationEvent> {

    @Resource
    private AccountMapper accountMapper;

    /**
     * 获取所有账户信息
     */
    @Cacheable(cacheNames = CacheKey.ACCOUNT, key =  "'accountList'")
    public List<AccountDTO> getAccountAll() {
        List<Account> accounts = accountMapper.selectAll();

        // 构建DTO
        return AccountDTO.build(accounts);
    }

    /**
     * 获取账户信息
     *
     * @param id 账户id
     * @return 账户信息
     */
    @Cacheable(cacheNames = CacheKey.ACCOUNT, key = "'account-' + #id")
    public AccountDTO getAccount(Long id) {
        return AccountDTO.build(accountMapper.selectByPrimaryKey(id));
    }

    /**
     * 获取账户信息
     *
     * @param username 账户名
     * @return 账户信息
     */
    @Cacheable(cacheNames = CacheKey.ACCOUNT, key = "'account-' + #username")
    public Account getAccount(String username) {
        Example example = new Example(Account.class);
        example.createCriteria().andEqualTo("username", username);
        return accountMapper.selectOneByExample(example);
    }

    /**
     * 获取账户信息
     *
     * @param username 账户名
     * @return 账户信息
     */
    @Cacheable(cacheNames = CacheKey.ACCOUNT, key = "'account-like-' + #username")
    public List<Account> getAccountLike(String username) {
        Example example = new Example(Account.class);
        example.createCriteria()
                .andLike("username", "%" + username + "%");
        return accountMapper.selectByExample(example);
    }

    /**
     * 删除账户信息
     *
     * @param id 账户id
     */
    @CacheEvict(cacheNames = CacheKey.ACCOUNT, key = "'account-' + #id")
    public void delAccount(Long id) {
        // 清除本地缓存
        clear();
        // 清除Redis 缓存
        if (!RedisUtils.del(RedisKey.getKey(RedisKey.ACCOUNT, id))) {
            log.error("删除账户缓存错误-{}", id);
        }
    }

    public void clear() {
        // 清除本地缓存
        CacheUtils.del(CacheKey.ACCOUNT);
    }

    @Override
    public void onApplicationEvent(ClearCacheApplicationEvent event) {
        Object source = event.getSource();
        log.debug("清除缓存 -> {}", source.toString());

        clear();
    }
}
