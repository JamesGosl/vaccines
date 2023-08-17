package org.james.gos.vaccines.account.doman.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * 用户名请求
 *
 * @author James Gosl
 * @since 2023/08/17 10:44
 */
@Data
@ApiModel("用户名请求")
public class UsernameReq {

    @ApiModelProperty("用户名")
    @NotNull
    private String username;
}
