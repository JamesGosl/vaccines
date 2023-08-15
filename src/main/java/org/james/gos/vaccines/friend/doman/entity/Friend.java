package org.james.gos.vaccines.friend.doman.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.james.gos.vaccines.common.annotation.FieldFill;
import org.james.gos.vaccines.common.annotation.TableField;
import org.james.gos.vaccines.common.annotation.TableLogic;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 好友表
 *
 * @author James Gosl
 * @since 2023/08/14 22:51
 */
@Table(name = "friend")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Friend implements Serializable {
    private static final long serialVersionUID = -5326311791783819558L;

    /** id */
    @Id
    private Long id;

    /** 申请人账户id */
    private Long accountId;
    /** 被申请人账户id */
    private Long friendId;
    /** 申请状态（0没处理、1同意、2拒绝） */
    private Integer status;

    /** 逻辑删除（0未删除，1已删除） */
    @TableLogic
    @Column(name = "is_deleted")
    private Integer deleted;
    /** 创建时间 */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    /** 更新时间 */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
