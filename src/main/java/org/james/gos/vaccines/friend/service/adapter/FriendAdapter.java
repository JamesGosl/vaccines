package org.james.gos.vaccines.friend.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import org.james.gos.vaccines.account.doman.dto.AccountDTO;
import org.james.gos.vaccines.common.doman.enums.YesOrNoEnum;
import org.james.gos.vaccines.common.util.DateUtils;
import org.james.gos.vaccines.friend.doman.dto.FriendDTO;
import org.james.gos.vaccines.friend.doman.entity.Friend;
import org.james.gos.vaccines.friend.doman.enums.FriendEnum;
import org.james.gos.vaccines.friend.doman.vo.response.FAUResp;
import org.james.gos.vaccines.user.doman.dto.UserDTO;

/**
 * FriendAdapter
 *
 * @author James Gosl
 * @since 2023/08/17 11:33
 */
public class FriendAdapter {

    public static Friend build(Long aid, Long id) {
        Friend friend = new Friend();
        friend.setAccountId(aid);
        friend.setFriendId(id);
        return friend;
    }

    public static Friend buildFriend(FriendDTO friendDTO) {
        return new Friend().setAccountId(friendDTO.getFriendId()).setFriendId(friendDTO.getAccountId());
    }

    public static Friend buildSuccess(FriendDTO friendDTO) {
        Friend friend = new Friend();
        BeanUtil.copyProperties(friendDTO, friend);
        return friend.setStatus(YesOrNoEnum.YES.getStatus()).setDescription(FriendEnum.SUCCESS.getDesc());
    }

    public static FAUResp build(FriendDTO friendDTO, AccountDTO account, UserDTO user) {
        FAUResp fauResp = new FAUResp();
        fauResp.setId(friendDTO.getId());
        fauResp.setUsername(account.getUsername());
        fauResp.setName(user.getName());
        fauResp.setPhone(user.getPhone());
        fauResp.setAddress(user.getAddress());
        fauResp.setDescription(friendDTO.getDescription());
        fauResp.setStatus(friendDTO.getStatus());
        fauResp.setCreateTime(DateUtils.format(friendDTO.getCreateTime()));
        fauResp.setUpdateTime(DateUtils.format(friendDTO.getUpdateTime()));
        return fauResp;
    }
}
