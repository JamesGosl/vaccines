package org.james.gos.vaccines.account.service.cache;

import lombok.extern.slf4j.Slf4j;
import org.james.gos.vaccines.account.doman.dto.AccountDTO;
import org.james.gos.vaccines.account.doman.entity.Account;
import org.james.gos.vaccines.common.constant.CacheKey;
import org.james.gos.vaccines.common.constant.RedisKey;
import org.james.gos.vaccines.common.utils.RedisUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * 账户信息 缓存
 *
 * @author James Gosl
 * @since 2023/08/15 20:34
 */
@Component
@Slf4j
public class AccountCache {

    /**
     * 设置账户到缓存中
     * @param id 键
     * @param account 账户
     */
    @CacheEvict(cacheNames = CacheKey.ACCOUNT, key = "'account-' + #id")
    public void setAccount(Long id, AccountDTO account) {
        if (!RedisUtils.set(RedisKey.getKey(RedisKey.ACCOUNT, id), account)) {
            throw new RuntimeException("设置账户缓存错误->" + id);
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
        log.debug("未击中缓存-{}", id);
        return RedisUtils.get(RedisKey.getKey(RedisKey.ACCOUNT, id), AccountDTO.class);
    }

    /**
     * 删除账户信息
     *
     * @param id 账户id
     */
    @CacheEvict(cacheNames = CacheKey.ACCOUNT, key = "'account-' + #id")
    public void delAccount(Long id) {
        if (!RedisUtils.del(RedisKey.getKey(RedisKey.ACCOUNT, id))) {
            throw new RuntimeException("删除账户缓存错误->" + id);
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
            throw new RuntimeException("设置令牌缓存错误->" + id);
        }
    }

    /**
     * 获取Token
     *
     * @param id 键
     */
    @Cacheable(cacheNames = CacheKey.ACCOUNT, key = "'token-' + #id")
    public String getToken(Long id) {
        log.debug("未击中缓存-{}", id);
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
            throw new RuntimeException("删除令牌缓存错误->" + id);
        }
    }
}
