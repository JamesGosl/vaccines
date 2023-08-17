package org.james.gos.vaccines.friend.doman.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 好友关系 枚举
 *
 * @author James Gosl
 * @since 2023/08/17 11:46
 */
@Getter
@AllArgsConstructor
public enum FriendEnum {

    CONNECT("建立关系"),
    ACCEPT("等待处理"),
    SUCCESS("处理完成"),
    ERROR("建立失败"),
    ;

    private final String desc;
}
