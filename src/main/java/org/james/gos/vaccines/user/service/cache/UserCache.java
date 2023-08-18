package org.james.gos.vaccines.user.service.cache;

import lombok.extern.slf4j.Slf4j;
import org.james.gos.vaccines.common.constant.CacheKey;
import org.james.gos.vaccines.common.constant.RedisKey;
import org.james.gos.vaccines.common.doman.enums.RedisChannelEnum;
import org.james.gos.vaccines.common.doman.enums.YesOrNoEnum;
import org.james.gos.vaccines.common.event.RedisApplicationEvent;
import org.james.gos.vaccines.common.event.RedisClearApplicationEvent;
import org.james.gos.vaccines.common.event.RedisUserApplicationEvent;
import org.james.gos.vaccines.common.utils.CacheUtils;
import org.james.gos.vaccines.common.utils.RedisUtils;
import org.james.gos.vaccines.user.doman.dto.UserDTO;
import org.james.gos.vaccines.user.doman.entity.User;
import org.james.gos.vaccines.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Objects;

/**
 * UserCache
 *
 * @author James Gosl
 * @since 2023/08/16 17:46
 */
@Component
@Slf4j
public class UserCache implements ApplicationListener<RedisApplicationEvent<?>> {
    @Autowired
    private UserMapper userMapper;

    /**
     * 获取用户信息
     *
     * @param aid 账户id
     */
    @Cacheable(cacheNames = CacheKey.USER, key = "'user-aid-' + #aid")
    public UserDTO getUser(Long aid) {
        UserDTO user = RedisUtils.get(RedisKey.getKey(RedisKey.USER, aid), UserDTO.class);

        if(Objects.isNull(user)) {
            Example example = new Example(User.class);
            example.createCriteria().andEqualTo("accountId", aid);
            user = UserDTO.build(userMapper.selectOneByExample(example));

            if(Objects.nonNull(user))
                RedisUtils.set(RedisKey.getKey(RedisKey.USER, user.getAccountId()), user);
        }
        return user;
    }

    /**
     * 增加用户信息
     *
     * @param user 用户信息
     */
    public YesOrNoEnum inUser(User user) {
        YesOrNoEnum yesOrNo = YesOrNoEnum.of(userMapper.insertSelective(user));
        if(YesOrNoEnum.YES.equals(yesOrNo)) {
            // 加入缓存
            RedisUtils.set(RedisKey.getKey(RedisKey.USER, user.getAccountId()), UserDTO.build(user));
            // 发布事件
            RedisUtils.publish(RedisChannelEnum.USER, UserDTO.build(user));
        }
        return yesOrNo;
    }

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     */
    @CacheEvict(cacheNames = CacheKey.USER, key = "'user-aid-' + #user.accountId")
    public YesOrNoEnum upUser(User user) {
        YesOrNoEnum yesOrNo = YesOrNoEnum.of(userMapper.updateByPrimaryKeySelective(user));
        if (YesOrNoEnum.YES.equals(yesOrNo)) {
            UserDTO userDTO = UserDTO.build(user);
            // 更新缓存
            if (RedisUtils.set(RedisKey.getKey(RedisKey.USER, user.getAccountId()), userDTO)) {
                // 发布事件
                RedisUtils.publish(RedisChannelEnum.USER, userDTO);
            } else {
                // TODO 补偿策略
                log.debug("更新用户信息缓存失败");
            }
        }
        return yesOrNo;
    }

    /**
     * 删除用户信息
     *
     * @param aid 账户id
     */
    @CacheEvict(cacheNames = CacheKey.USER, key = "'user-aid-' + #aid")
    public YesOrNoEnum delUser(Long aid) {
        UserDTO user = getUser(aid);
        // 校验是否有数据
        if(Objects.isNull(user))
            return YesOrNoEnum.NO;

        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("accountId", aid);
        YesOrNoEnum yesOrNo = YesOrNoEnum.of(userMapper.deleteByExample(example));
        if(YesOrNoEnum.YES.equals(yesOrNo)) {
            // 删除缓存
            if (RedisUtils.del(RedisKey.getKey(RedisKey.USER, aid))) {
                // 发布事件
                RedisUtils.publish(RedisChannelEnum.USER, user);
            } else {
                // TODO 补偿策略
                log.debug("删除用户信息缓存失败");
            }
        }
        return yesOrNo;
    }

    @Override
    public void onApplicationEvent(RedisApplicationEvent event) {
        if(event instanceof RedisClearApplicationEvent) {
            CacheUtils.del(CacheKey.USER);
        }
        else if(event instanceof RedisUserApplicationEvent) {
            UserDTO user = ((RedisUserApplicationEvent) event).getValue();
            CacheUtils.del(CacheKey.ACCOUNT, "user-aid-" + user.getAccountId());
        }
    }
}
