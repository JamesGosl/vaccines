package org.james.gos.vaccines.auth.doman.entity;

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
 * 权限表
 *
 * @author James Gosl
 * @since 2023/08/14 22:39
 */
@Table(name = "auth")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Auth implements Serializable {
    private static final long serialVersionUID = -2416802408311391930L;
    
    /** id */
    @Id
    private Long id;

    /** 权限字段 */
    private Integer auth;
    /** 描述字段 */
    private String description;

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
