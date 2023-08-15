package org.james.gos.vaccines.msg.mapper;

import org.james.gos.vaccines.msg.doman.entity.Msg;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * 消息表 数据层
 *
 * @author James Gosl
 * @since 2023/08/14 23:05
 */
@Repository
public interface MsgMapper extends Mapper<Msg> {
}
