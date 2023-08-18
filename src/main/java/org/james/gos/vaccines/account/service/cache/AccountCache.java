package org.james.gos.vaccines.account.service.cache;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import lombok.extern.slf4j.Slf4j;
import org.james.gos.vaccines.account.doman.dto.AccountDTO;
import org.james.gos.vaccines.account.doman.entity.Account;
import org.james.gos.vaccines.account.mapper.AccountMapper;
import org.james.gos.vaccines.common.constant.CacheKey;
import org.james.gos.vaccines.common.constant.RedisKey;
import org.james.gos.vaccines.common.doman.enums.RedisChannelEnum;
import org.james.gos.vaccines.common.doman.enums.YesOrNoEnum;
import org.james.gos.vaccines.common.doman.vo.request.PageBaseReq;
import org.james.gos.vaccines.common.doman.vo.response.PageBaseResp;
import org.james.gos.vaccines.common.event.RedisAccountApplicationEvent;
import org.james.gos.vaccines.common.event.RedisApplicationEvent;
import org.james.gos.vaccines.common.event.RedisClearApplicationEvent;
import org.james.gos.vaccines.common.utils.CacheUtils;
import org.james.gos.vaccines.common.utils.RedisUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 账户信息 缓存
 *
 * @author James Gosl
 * @since 2023/08/15 20:34
 */
@Component
@Slf4j
public class AccountCache implements ApplicationListener<RedisApplicationEvent<?>> {

    @Resource
    private AccountMapper accountMapper;

    /**
     * 获取所有账户信息
     */
    @Cacheable(cacheNames = CacheKey.ACCOUNT, key =  "'account-list'")
    public List<AccountDTO> getAccountAll() {
        // 查询二级缓存数量是否与数据库一致
        return getAccountExample(RedisUtils.keys(RedisKey.getAccount()), new Example(Account.class));
    }

    /**
     * 获取账户信息
     *
     * @param username 账户名
     * @return 账户信息
     */
    @Cacheable(cacheNames = CacheKey.ACCOUNT, key = "'account-like-' + #username")
    public List<AccountDTO> getAccountLike(String username) {
        // 模糊查询二级缓存跟数据库的数量是否一致，如果一致则返回二级缓存，不一致返回数据库，随后将数据库数据放入二级缓存
        Example example = new Example(Account.class);
        example.createCriteria().andLike("username", "%" + username + "%");

        return getAccountExample(RedisUtils.keys(RedisKey.getAccountLike(username)), example);
    }

    private List<AccountDTO> getAccountExample(Set<String> keys, Example example) {
        // 判断二级缓存数量是否与数据库一致

        // 数据库查找
        if(Objects.isNull(keys) || keys.size() != accountMapper.selectCountByExample(example)) {
            List<AccountDTO> list = AccountDTO.build(accountMapper.selectByExample(example));
            if (Objects.nonNull(list) && !list.isEmpty()) {
                // TODO 这个操作同步还是异步
                RedisUtils.setList(list.stream().collect(Collectors.toMap(
                        key -> RedisKey.getAccount(key.getId(), key.getUsername()),
                        val -> val
                )));
                return list;
            }
        }
        // 返回Redis 缓存
        else {
            if (!keys.isEmpty()) {
                return RedisUtils.getList(keys, AccountDTO.class);
            }
        }
        return Collections.emptyList();
    }

    /**
     * 获取分页账户信息
     */
//    @Cacheable(cacheNames = CacheKey.ACCOUNT, key =  "'accountPage-' + #request.page + '-' + #request.limit")
    @Deprecated
    public PageBaseResp<AccountDTO> getAccountPage(PageBaseReq request) {
        // TODO PageHelper 无法与自研MyBatis 插件配合
        Page<Account> pageUser = PageMethod.startPage(request.getPage(), request.getLimit());
        accountMapper.selectAll();

        if (pageUser.getTotal() > 0) {
            return PageBaseResp.init(pageUser.getTotal(), AccountDTO.build(pageUser.getResult()));
        }
        return PageBaseResp.empty();
    }

    /**
     * 获取账户信息
     *
     * @param id 账户id
     * @return 账户信息
     */
    @Cacheable(cacheNames = CacheKey.ACCOUNT, key = "'account-id-' + #id")
    public AccountDTO getAccount(Long id) {
        // 模糊查询匹配二级缓存
        AccountDTO accountDTO = RedisUtils.keysOne(RedisKey.getAccount(id), AccountDTO.class);
        if (accountDTO == null) {
            accountDTO = AccountDTO.build(accountMapper.selectByPrimaryKey(id));

            if (Objects.nonNull(accountDTO))
                RedisUtils.set(RedisKey.getAccount(id, accountDTO.getUsername()), accountDTO);
        }
        return accountDTO;
    }

    /**
     * 获取账户信息
     *
     * @param username 账户名
     * @return 账户信息
     */
    @Cacheable(cacheNames = CacheKey.ACCOUNT, key = "'account-username-' + #username")
    public AccountDTO getAccount(String username) {
        // 模糊查询匹配二级缓存
        AccountDTO accountDTO = RedisUtils.keysOne(RedisKey.getAccount(username), AccountDTO.class);
        if (accountDTO == null) {
            Example example = new Example(Account.class);
            example.createCriteria().andEqualTo("username", username);
            accountDTO = AccountDTO.build(accountMapper.selectOneByExample(example));

            if(Objects.nonNull(accountDTO))
                RedisUtils.set(RedisKey.getAccount(accountDTO.getId(), username), accountDTO);
        }

        return accountDTO;
    }

    /**
     * 增加账户信息
     *
     * @param account 账户
     */
    @CacheEvict(cacheNames = CacheKey.ACCOUNT, key = "'account-id-' + #account.id")
    public YesOrNoEnum inAccount(Account account) {
        YesOrNoEnum yesOrNo = YesOrNoEnum.of(accountMapper.insertSelective(account));
        if (YesOrNoEnum.YES.equals(yesOrNo)) {
            AccountDTO dto = AccountDTO.build(account);
            if(Objects.nonNull(dto)) {
                // 更新Redis 缓存
                RedisUtils.set(RedisKey.getKey(RedisKey.ACCOUNT, dto.getId()), dto);
                // Redis 发布/订阅 保证数据一致性
                RedisUtils.publish(RedisChannelEnum.ACCOUNT, dto);
            }
        }
        return yesOrNo;
    }

    /**
     * 更新账户信息
     *
     * @param account 账户
     */
    @CacheEvict(cacheNames = CacheKey.ACCOUNT, key = "'account-id-' + #account.id")
    public YesOrNoEnum upAccount(Account account) {
        YesOrNoEnum yesOrNo = YesOrNoEnum.of(accountMapper.updateByPrimaryKeySelective(account));
        if (YesOrNoEnum.YES.equals(yesOrNo)) {
            AccountDTO dto = AccountDTO.build(account);
            // 更新Redis 缓存
            if (RedisUtils.set(RedisKey.getKey(RedisKey.ACCOUNT, dto.getId()), dto)) {
                // Redis 发布/订阅 保证数据一致性
                RedisUtils.publish(RedisChannelEnum.ACCOUNT, dto);
            } else {
                // TODO 补偿策略
                log.debug("更新账户信息缓存失败");
            }
        }
        return yesOrNo;
    }

    /**
     * 删除账户信息
     *
     * @param account 账户
     */
    @CacheEvict(cacheNames = CacheKey.ACCOUNT, key = "'account-id-' + #account.id")
    public YesOrNoEnum delAccount(AccountDTO account) {
        YesOrNoEnum yesOrNo = YesOrNoEnum.of(accountMapper.deleteByPrimaryKey(account.getId()));
        if (YesOrNoEnum.YES.equals(yesOrNo)) {
            // 清除Redis 缓存
            if (RedisUtils.del(RedisKey.getAccount(account.getId(), account.getUsername()))) {
                // Redis 发布/订阅 保证数据一致性
                RedisUtils.publish(RedisChannelEnum.ACCOUNT, account);
            } else {
                // TODO 补偿策略
                log.debug("删除账户信息缓存失败");
            }
        }
        return yesOrNo;
    }

    @Override
    public void onApplicationEvent(RedisApplicationEvent event) {
        // 清除所有本地缓存事件
        if(event instanceof RedisClearApplicationEvent) {
            CacheUtils.del(CacheKey.ACCOUNT);
        }

        // 清除账户事件
        else if(event instanceof RedisAccountApplicationEvent) {
            AccountDTO account = ((RedisAccountApplicationEvent) event).getValue();
            CacheUtils.del(CacheKey.ACCOUNT, "account-id-" + account.getId());
            CacheUtils.del(CacheKey.ACCOUNT, "account-username-" + account.getUsername());
            // TODO 是否应该单独删除
            CacheUtils.del(CacheKey.ACCOUNT, "account-list");
            CacheUtils.del(CacheKey.ACCOUNT, "account-like-" + account.getUsername());
        }
    }
}
