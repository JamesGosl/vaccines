package org.james.gos.vaccines.friend.doman.vo.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 好友、账号、用户聚合响应
 *
 * @author James Gosl
 * @since 2023/08/17 13:19
 */
@Data
@ApiModel("好友、账号、用户聚合响应")
public class FAUResp {

    @ApiModelProperty("好友Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @ApiModelProperty("操作人账户")
    private String username;
    @ApiModelProperty("操作人姓名")
    private String name;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty("操作描述")
    private String description;
    @ApiModelProperty("操作状态 0未处理 1已处理")
    private Integer status;
    @ApiModelProperty("创建时间")
    private String createTime;
    @ApiModelProperty("更新时间")
    private String updateTime;
}
