package org.james.gos.vaccines.auth.doman.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 权限信息返回
 *
 * @author James Gosl
 * @since 2023/08/15 15:42
 */
@Data
@ApiModel("权限信息")
public class AuthResp {

    @ApiModelProperty(value = "权限ID")
    private Long id;
    @ApiModelProperty(value = "权限 0为管理员，1为医生，2为接种者")
    private Integer auth;
    @ApiModelProperty(value = "权限描述")
    private String description;
}
