package org.james.gos.vaccines.account.doman.dto;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.james.gos.vaccines.account.doman.entity.Account;
import org.james.gos.vaccines.auth.doman.vo.response.AuthResp;
import org.james.gos.vaccines.common.annotation.FieldFill;
import org.james.gos.vaccines.common.annotation.TableField;
import org.james.gos.vaccines.common.annotation.TableLogic;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * AccountDTO
 *
 * @author James Gosl
 * @since 2023/08/15 21:44
 */
@Data
@Accessors(chain = true)
public class AccountDTO {
    /** id */
    private Long id;

    /** 权限ID */
    private Long authId;
    /** 用户名 */
    private String username;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
    /** 权限 */
    private Integer auth;
    /** 权限描述 */
    private String description;
    /** 登录令牌 */
    private String token;

    public static AccountDTO build(Account account, AuthResp authResp, String token) {
        AccountDTO accountDTO = new AccountDTO();
        BeanUtil.copyProperties(account, accountDTO);
        BeanUtil.copyProperties(authResp, accountDTO);
        return accountDTO.setToken(token);
    }
}
