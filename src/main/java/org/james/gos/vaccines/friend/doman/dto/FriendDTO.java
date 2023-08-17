package org.james.gos.vaccines.friend.doman.dto;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.james.gos.vaccines.friend.doman.entity.Friend;
import org.james.gos.vaccines.friend.service.adapter.FriendAdapter;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * FriendDTO
 *
 * @author James Gosl
 * @since 2023/08/17 12:51
 */
@Data
@Accessors(chain = true)
public class FriendDTO {

    /** id */
    private Long id;
    /** 申请人账户id */
    private Long accountId;
    /** 被申请人账户id */
    private Long friendId;
    /** 描述字段 */
    private String description;
    /** 申请状态（0没处理、1已处理） */
    private Integer status;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

    public static FriendDTO build(Friend friend) {
        if(Objects.isNull(friend))
            return null;

        FriendDTO friendDTO = new FriendDTO();
        BeanUtil.copyProperties(friend, friendDTO);
        return friendDTO;
    }

    public static List<FriendDTO> build(List<Friend> friendList) {
        if (friendList == null || friendList.size() < 1) {
            return null;
        }
        return friendList.stream().map(FriendDTO::build).collect(Collectors.toList());
    }
}
