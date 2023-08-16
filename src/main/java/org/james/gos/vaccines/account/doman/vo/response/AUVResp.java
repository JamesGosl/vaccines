package org.james.gos.vaccines.account.doman.vo.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 账号 用户 疫苗 包装响应
 *
 * @author James Gosl
 * @since 2023/08/16 21:40
 */
@Data
@ApiModel("账号 用户 疫苗 包装响应")
public class AUVResp {

    @ApiModelProperty("疫苗ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("昵称")
    private String name;
    @ApiModelProperty("是否存在 0不存在 1存在")
    private Integer state;
}
