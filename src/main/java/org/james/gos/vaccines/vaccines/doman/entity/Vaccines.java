package org.james.gos.vaccines.vaccines.doman.entity;

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
 * 疫苗表
 *
 * @author James Gosl
 * @since 2023/08/14 23:00
 */
@Table(name = "vaccines")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Vaccines implements Serializable {
    private static final long serialVersionUID = -4903749803130623521L;

    /** id */
    @Id
    private Long id;

    /** 账户id */
    private Long accountId;
    /** 接种信息 */
    private String content;

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
