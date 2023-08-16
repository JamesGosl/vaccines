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
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Id;
import java.awt.*;
import java.util.Date;
import java.util.List;

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
    // TODO 不应该存在别的数据 否则产生强依赖
    /** 权限 */
    private Integer auth;
    /** 权限描述 */
    private String description;

    public static AccountDTO build(Account account, @Nullable AuthResp authResp) {
        AccountDTO accountDTO = new AccountDTO();
        BeanUtil.copyProperties(account, accountDTO);
        // 如果存在相同属性的话 前者会把后者覆盖掉
//        BeanUtil.copyProperties(authResp, accountDTO);
        if(authResp == null)
            return accountDTO;
        return accountDTO.setAuthId(authResp.getId()).setAuth(authResp.getAuth()).setDescription(authResp.getDescription());
    }
}
