package org.james.gos.vaccines.user.service.cache;

import lombok.extern.slf4j.Slf4j;
import org.james.gos.vaccines.common.constant.CacheKey;
import org.james.gos.vaccines.common.event.ClearCacheApplicationEvent;
import org.james.gos.vaccines.common.utils.CacheUtils;
import org.james.gos.vaccines.user.doman.dto.UserDTO;
import org.james.gos.vaccines.user.doman.entity.User;
import org.james.gos.vaccines.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

/**
 * UserCache
 *
 * @author James Gosl
 * @since 2023/08/16 17:46
 */
@Component
@Slf4j
public class UserCache implements ApplicationListener<ClearCacheApplicationEvent> {
    @Autowired
    private UserMapper userMapper;


    /**
     * 获取用户信息
     *
     * @param aid 账户id
     */
    @Cacheable(cacheNames = CacheKey.USER, key = "'user-' + #aid")
    public UserDTO getUser(Long aid) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("accountId", aid);
        return UserDTO.build(userMapper.selectOneByExample(example));
    }

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     */
    @CacheEvict(cacheNames = CacheKey.USER, key = "'user-' + #user.accountId")
    public void updateUser(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 删除用户信息
     *
     * @param aid 账户id
     */
    @Cacheable(cacheNames = CacheKey.USER, key = "'user-' + #aid")
    public void deletedUser(Long aid) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("accountId", aid);
        userMapper.deleteByExample(example);
    }

    @Override
    public void onApplicationEvent(ClearCacheApplicationEvent event) {
        Object source = event.getSource();
        log.debug("清除缓存 -> {}", source.toString());

        CacheUtils.del(CacheKey.USER);
    }
}
