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
    @Autowired
    private IAccountService accountService;

    /**
     * 获取所有账户信息
     */
    @Cacheable(cacheNames = CacheKey.ACCOUNT, key =  "'accountList'")
    public List<AccountDTO> getAccountAll() {
        // 只使用本地缓存 不使用Redis 不然太复杂了
        List<Account> accounts = accountService.selectAll();

        // 构建DTO
        return AccountDTO.build(accounts);
    }

    /**
     * 获取所有账户信息
     */
    @Cacheable(cacheNames = CacheKey.ACCOUNT, key =  "'accountListPage-' + #pageBaseReq.page + '-' + #pageBaseReq.limit")
    public AccountPageDTO getAccountAll(PageBaseReq pageBaseReq) {
        // 只使用本地缓存 不使用Redis 不然太复杂了

        Page<Account> page = PageMethod.startPage(pageBaseReq.getPage(), pageBaseReq.getLimit());
        List<Account> accounts = accountService.selectAll();

        // 构建DTO
        return AccountPageDTO.build(page, AccountDTO.build(accounts));
    }

    /**
     * 设置账户到缓存中
     * @param id 键
     * @param account 账户
     */
    @CacheEvict(cacheNames = CacheKey.ACCOUNT, key = "'account-' + #id")
    public void setAccount(Long id, AccountDTO account) {
        if (!RedisUtils.set(RedisKey.getKey(RedisKey.ACCOUNT, id), account)) {
            log.error("设置账户缓存错误-{}", id);
        }
    }

    /**
     * 获取账户信息
     *
     * @param id 账户id
     * @return 账户信息
     */
    @Cacheable(cacheNames = CacheKey.ACCOUNT, key = "'account-' + #id")
    public AccountDTO getAccount(Long id) {
        // 集合中查找
        List<AccountDTO> accounts = CacheUtils.get(CacheKey.ACCOUNT, "accountList");
        AccountDTO accountDTO;
        if(accounts != null) {
            accountDTO = accounts.stream().filter(account -> account.getId().equals(id)).findFirst().orElse(null);
            if (accountDTO != null)
                return accountDTO;
        }
        log.debug("本地缓存未击中-{}", id);

        // Redis 中查找
        accountDTO = RedisUtils.get(RedisKey.getKey(RedisKey.ACCOUNT, id), AccountDTO.class);

        // 数据库查找
        if (accountDTO == null) {
            log.debug("Redis 缓存未击中-{}", id);
            Account account = accountService.selectById(id);
            // 设置缓存
            setAccount(id, AccountDTO.build(account));
        }
        return accountDTO;
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

    /**
     * 设置Token 到缓存中
     *
     * @param id 键
     * @param token 信息
     */
    @CacheEvict(cacheNames = CacheKey.ACCOUNT, key = "'token-' + #id")
    public void setToken(Long id, String token) {
        if (!RedisUtils.set(RedisKey.getKey(RedisKey.TOKEN, id), token)) {
            log.error("设置令牌缓存错误-{}", id);
        }
    }

    /**
     * 获取Token
     *
     * @param id 键
     */
    @Cacheable(cacheNames = CacheKey.ACCOUNT, key = "'token-' + #id")
    public String getToken(Long id) {
        // TODO 强依赖的数据是否要采用本地缓存

        log.debug("本地缓存未击中-{}", id);
        return RedisUtils.get(RedisKey.getKey(RedisKey.TOKEN, id), String.class);
    }

    /**
     * 删除Token
     *
     * @param id 键
     */
    @CacheEvict(cacheNames = CacheKey.ACCOUNT, key = "'token-' + #id")
    public void delToken(Long id) {
        if (!RedisUtils.del(RedisKey.getKey(RedisKey.TOKEN, id))) {
            log.error("删除令牌缓存错误-{}", id);
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
