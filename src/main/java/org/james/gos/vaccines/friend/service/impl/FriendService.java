package org.james.gos.vaccines.friend.service.impl;

import org.james.gos.vaccines.account.doman.dto.AccountDTO;
import org.james.gos.vaccines.account.service.IAccountService;
import org.james.gos.vaccines.account.service.impl.AccountService;
import org.james.gos.vaccines.common.doman.enums.YesOrNoEnum;
import org.james.gos.vaccines.friend.doman.dto.FriendDTO;
import org.james.gos.vaccines.friend.doman.entity.Friend;
import org.james.gos.vaccines.friend.doman.enums.FriendEnum;
import org.james.gos.vaccines.friend.doman.vo.response.FAUResp;
import org.james.gos.vaccines.friend.mapper.FriendMapper;
import org.james.gos.vaccines.friend.service.IFriendService;
import org.james.gos.vaccines.friend.service.adapter.FriendAdapter;
import org.james.gos.vaccines.user.doman.vo.response.UserResp;
import org.james.gos.vaccines.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * FriendService
 *
 * @author James Gosl
 * @since 2023/08/17 11:29
 */
@Service
public class FriendService implements IFriendService {

    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserService userService;

    @Override
    @Transactional
    public void insert(Long aid, Long id) {
        if(Objects.equals(aid, id))
            throw new RuntimeException("不能跟自己加好友");

        // 是否已经存在
        if(!verify(aid, id))
            throw new RuntimeException("已提交处理");

        // 建立关系、双向关系
        insert(FriendAdapter.build(aid, id).setStatus(YesOrNoEnum.YES.getStatus()).setDescription(FriendEnum.CONNECT.getDesc()));
        insert(FriendAdapter.build(id, aid).setStatus(YesOrNoEnum.NO.getStatus()).setDescription(FriendEnum.ACCEPT.getDesc()));
    }


    @Override
    @Transactional
    public void update(Long aid, Long id) {
        // 确定关系、双向关系
        FriendDTO account = getFriend(id);
        if(Objects.isNull(account))
            throw new RuntimeException("不存在的关系");

        // 查询 对应记录
        FriendDTO friend = getFriend(FriendAdapter.buildFriend(account));
        if(Objects.isNull(friend))
            throw new RuntimeException("不存在的关系");

        update(FriendAdapter.buildSuccess(account));
        update(FriendAdapter.buildSuccess(friend));
    }

    public FriendDTO getFriend(Friend friend) {
        return FriendDTO.build(friendMapper.selectOne(friend));
    }

    @Override
    public FriendDTO getFriend(Long id) {
        return FriendDTO.build(friendMapper.selectByPrimaryKey(id));
    }

    public void insert(Friend friend) {
        if (YesOrNoEnum.of(friendMapper.insert(friend)).equals(YesOrNoEnum.NO)) {
            throw new RuntimeException("建立关系失败");
        }
    }

    public void update(Friend friend) {
        if (YesOrNoEnum.of(friendMapper.updateByPrimaryKeySelective(friend)).equals(YesOrNoEnum.NO)) {
            throw new RuntimeException("更新关系失败");
        }
    }

    @Override
    public Boolean verify(Long aid, Long id) {
        return Objects.isNull(getFriend(FriendAdapter.build(aid, id)));
    }

    @Override
    public List<FriendDTO> getFriends(Long aid) {
        Example example = new Example(Friend.class);
        example.createCriteria().andEqualTo("accountId", aid);
        return FriendDTO.build(friendMapper.selectByExample(example));
    }

    @Override
    public List<FAUResp> list(Long aid) {
        // 获取好友关系
        List<FriendDTO> friends = getFriends(aid);
        if (Objects.isNull(friends)) {
            return Collections.emptyList();
        }

        // 聚合信息
        return friends.stream().map(friendDTO -> {
            Long friendId = friendDTO.getFriendId();
            AccountDTO account = accountService.getAccount(friendId);
            UserResp user = userService.getUser(friendId);
            return FriendAdapter.build(friendDTO, account, user);
        }).collect(Collectors.toList());
    }
}
