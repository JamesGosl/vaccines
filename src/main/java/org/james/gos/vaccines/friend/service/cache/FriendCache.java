package org.james.gos.vaccines.friend.service.cache;

import lombok.extern.slf4j.Slf4j;
import org.james.gos.vaccines.common.constant.CacheKey;
import org.james.gos.vaccines.common.constant.RedisKey;
import org.james.gos.vaccines.common.doman.enums.RedisChannelEnum;
import org.james.gos.vaccines.common.doman.enums.YesOrNoEnum;
import org.james.gos.vaccines.common.event.RedisApplicationEventBase;
import org.james.gos.vaccines.common.event.RedisClearApplicationEvent;
import org.james.gos.vaccines.common.event.RedisFriendApplicationEvent;
import org.james.gos.vaccines.common.util.CacheUtils;
import org.james.gos.vaccines.common.util.RedisUtils;
import org.james.gos.vaccines.friend.doman.dto.FriendDTO;
import org.james.gos.vaccines.friend.doman.entity.Friend;
import org.james.gos.vaccines.friend.mapper.FriendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * FriendCache
 *
 * @author James Gosl
 * @since 2023/08/17 15:00
 */
@Component
@Slf4j
public class FriendCache implements ApplicationListener<RedisApplicationEventBase<?>> {
    @Autowired
    private FriendMapper friendMapper;

    /**
     * 获取Friend
     *
     * @param id id 主键
     */
    @Cacheable(cacheNames = CacheKey.FRIEND, key = "'friend-id-' + #id")
    public FriendDTO getFriend(Long id) {
        FriendDTO friendDTO = RedisUtils.keysOne(RedisKey.getFriend(id), FriendDTO.class);

        if (Objects.isNull(friendDTO)) {
            friendDTO = FriendDTO.build(friendMapper.selectByPrimaryKey(id));
            if (Objects.nonNull(friendDTO)) {
                RedisUtils.set(RedisKey.getFriend(friendDTO.getId(), friendDTO.getAccountId()), friendDTO);
            }
        }
        return friendDTO;
    }

    /**
     * 获取Friends
     *
     * @param aid 账户Id
     */
    @Cacheable(cacheNames = CacheKey.FRIEND, key = "'friend-aid-' + #aid")
    public List<FriendDTO> getFriendList(Long aid) {
        Set<String> keys = RedisUtils.keys(RedisKey.getFriendAid(aid));
        Example example = new Example(Friend.class);
        example.createCriteria().andEqualTo("accountId", aid);

        // 数据库
        if (Objects.isNull(keys) || keys.size() != friendMapper.selectCountByExample(example)) {
            List<FriendDTO> list = FriendDTO.build(friendMapper.selectByExample(example));
            if (Objects.nonNull(list) && !list.isEmpty()) {
                // TODO 这个操作同步还是异步
                RedisUtils.setList(list.stream().collect(Collectors.toMap(
                        key -> RedisKey.getVaccines(key.getId(), key.getAccountId()),
                        val -> val
                )));
                return list;
            }
        }
        // 缓存
        else {
            return RedisUtils.getList(keys, FriendDTO.class);
        }

        return Collections.emptyList();
    }

    /**
     * 插入Friend
     *
     * @param friend 关系
     */
    public YesOrNoEnum inFriend(Friend friend) {
        YesOrNoEnum yesOrNo = YesOrNoEnum.of(friendMapper.insertSelective(friend));
        if(Objects.equals(YesOrNoEnum.YES, yesOrNo)) {
            FriendDTO friendDTO = FriendDTO.build(friend);
            // 加入缓存
            RedisUtils.set(RedisKey.getFriend(friend.getId(), friend.getAccountId()), friendDTO);
            // 发布事件
            RedisUtils.publish(RedisChannelEnum.FRIEND, friendDTO);
        }
        return yesOrNo;
    }

    /**
     * 更新Friend DTO
     *
     * @param friend 关系
     */
    @CacheEvict(cacheNames = CacheKey.FRIEND, key = "'friend-id-' + #friend.id")
    public YesOrNoEnum upFriend(Friend friend) {
        YesOrNoEnum yesOrNo = YesOrNoEnum.of(friendMapper.updateByPrimaryKeySelective(friend));
        if(Objects.equals(YesOrNoEnum.YES, yesOrNo)) {
            FriendDTO friendDTO = FriendDTO.build(friend);
            // 更新缓存
            if (RedisUtils.set(RedisKey.getFriend(friend.getId(), friend.getAccountId()), friendDTO)) {
                // 发布事件
                RedisUtils.publish(RedisChannelEnum.FRIEND, friendDTO);
            } else {
                // TODO 补偿策略
                log.debug("更新好友信息缓存失败");
            }
        }
        return yesOrNo;
    }

    @Override
    public void onApplicationEvent(RedisApplicationEventBase event) {
        if(event instanceof RedisClearApplicationEvent) {
            CacheUtils.del(CacheKey.FRIEND);
        }
        else if(event instanceof RedisFriendApplicationEvent) {
            FriendDTO friend = ((RedisFriendApplicationEvent) event).getValue();
            // 清除本地缓存
            CacheUtils.del(CacheKey.FRIEND, "friend-id-" + friend.getId());
            CacheUtils.del(CacheKey.FRIEND, "friend-aid-" + friend.getAccountId());
        }
    }
}
