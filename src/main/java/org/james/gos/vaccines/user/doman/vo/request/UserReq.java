package org.james.gos.vaccines.user.doman.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 用户信息请求类
 *
 * @author James Gosl
 * @since 2023/08/16 17:54
 */
@Data
@ApiModel("用户信息 请求")
public class UserReq {

    @ApiModelProperty("用户Id")
    @NotNull
    private Long id;

    @ApiModelProperty("用户名")
    private String name;
    @ApiModelProperty("电话")
    private String phone;
    @ApiModelProperty("地址")
    private String address;
}
