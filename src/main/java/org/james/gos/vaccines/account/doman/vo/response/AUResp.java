package org.james.gos.vaccines.account.doman.vo.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 账户用户关联信息
 *
 * @author James Gosl
 * @since 2023/08/17 10:23
 */
@Data
@ApiModel("账户用户关联信息")
public class AUResp {

    @ApiModelProperty("账户ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @ApiModelProperty("账户")
    private String username;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("地址")
    private String address;
}
