package org.james.gos.vaccines.msg.doman.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.james.gos.vaccines.common.annotation.FieldFill;
import org.james.gos.vaccines.common.annotation.TableField;
import org.james.gos.vaccines.common.annotation.TableLogic;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 消息表
 *
 * @author James Gosl
 * @since 2023/08/14 22:53
 */
@Table(name = "msg")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Msg implements Serializable {
    private static final long serialVersionUID = 5203256921221426730L;

    /** id */
    @Id
    private Long id;

    /** 好友id */
    private Long friendId;
    /** 描述 */
    private String msg;

    /** 逻辑删除（0未删除，1已删除） */
    @TableLogic
    private Integer deleted;
    /** 创建时间 */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    /** 更新时间 */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
