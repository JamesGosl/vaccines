package org.james.gos.vaccines.account.doman.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 登录请求
 *
 * @author James Gosl
 * @since 2023/08/15 19:27
 */
@Data
@ApiModel("登录请求")
public class LoginReq {

    @ApiModelProperty("用户名")
    @NotNull
    private String username;

    @ApiModelProperty("密码")
    @NotNull
    private String password;
}
