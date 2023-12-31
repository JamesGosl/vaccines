package org.james.gos.vaccines.account.doman.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.james.gos.vaccines.common.annotation.FieldFill;
import org.james.gos.vaccines.common.annotation.TableField;
import org.james.gos.vaccines.common.annotation.TableLogic;
import org.james.gos.vaccines.common.plugin.IdWorker;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
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
    @KeySql(genId = IdWorker.class)
    private Long id;
    /** 用户名 */
    private String username;
    /** BCrypt 加密后的密码 */
    @JsonIgnore //拒绝密码序列化
    private String password;
    /** 权限 */
    private Integer auth;

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
