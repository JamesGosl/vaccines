package org.james.gos.vaccines.account.doman.entity;

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
 * 账户表
 *
 * @author James Gosl
 * @since 2023/08/14 22:48
 */
@Table(name = "account")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Account implements Serializable {
    private static final long serialVersionUID = -4968304195419699614L;

    /** id */
    @Id
    private Long id;

    /** 权限ID */
    private Long authId;
    /** 用户名 */
    private String username;
    /** BCrypt 加密后的密码 */
    private String password;

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
