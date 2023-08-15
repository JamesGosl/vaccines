package org.james.gos.vaccines.account.doman.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 账户请求
 *
 * @author James Gosl
 * @since 2023/08/15 21:21
 */
@Data
@ApiModel("账户请求")
public class AccountReq {

    @ApiModelProperty("如果是更新的话，这个id 要有，如果是增加的话这个可以没有")
    private Long id;

    @ApiModelProperty(value = "权限 0为管理员，1为医生，2为接种者")
    @NotNull
    private Integer auth;

    @ApiModelProperty("用户名")
    @Length(min = 3, max = 8, message = "那么大我受不了，太小了也不行")
    @NotNull
    private String username;

    @ApiModelProperty("密码")
    @NotNull
    @Length(min = 3, max = 8, message = "那么大我受不了，太小了也不行")
    private String password;
}
