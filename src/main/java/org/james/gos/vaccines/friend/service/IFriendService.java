package org.james.gos.vaccines.friend.service;

import org.james.gos.vaccines.friend.doman.dto.FriendDTO;
import org.james.gos.vaccines.friend.doman.vo.response.FAUResp;

import java.util.List;

/**
 * 好友表 逻辑层
 *
 * @author James Gosl
 * @since 2023/08/15 19:23
 */
public interface IFriendService {


    /**
     * 建立好友关系
     *
     * @param aid 申请人
     * @param id 被申请人
     */
    void insert(Long aid, Long id);

    /**
     * 查看是否存在
     *
     * @param aid 账户id
     * @param id 被申请Id
     */
    Boolean verify(Long aid, Long id);

    /**
     * 获取好友关系
     *
     * @param id 关系id
     */
    FriendDTO getFriend(Long id);

    /**
     * 获取好友关系
     *
     * @param aid 账户id
     */
    List<FriendDTO> getFriends(Long aid);

    /**
     * 确定好友关系
     *
     * @param aid 申请人
     * @param id 关系ID
     */
    void update(Long aid, Long id);


    /**
     * 获取好友信息
     *
     * @param aid 账户id
     */
    List<FAUResp> list(Long aid);
}
