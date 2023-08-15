package org.james.gos.vaccines.account.doman.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 登录响应
 *
 * @author James Gosl
 * @since 2023/08/15 19:29
 */
@Data
@Accessors(chain = true)
@ApiModel("登录响应")
public class AccountResp {
    @ApiModelProperty("账户id")
    private Long id;
    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty(value = "权限 0为管理员，1为医生，2为接种者")
    private Integer auth;
    @ApiModelProperty(value = "权限描述")
    private String description;

    @ApiModelProperty("令牌")
    private String token;
}
