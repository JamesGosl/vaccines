package org.james.gos.vaccines.system.domain.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 登录响应
 *
 * @author James Gosl
 * @since 2023/08/17 16:56
 */
@Data
@Accessors(chain = true)
@ApiModel("登录响应")
public class LoginResp {
    @ApiModelProperty("账户id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty(value = "权限 0为管理员，1为医生，2为接种者")
    private Integer auth;
    @ApiModelProperty(value = "权限描述")
    private String description;
    @ApiModelProperty("令牌")
    private String token;

    @ApiModelProperty("创建时间")
    @JsonFormat
    private Date createTime;
    @ApiModelProperty("更新时间")
    @JsonFormat
    private Date updateTime;
}

