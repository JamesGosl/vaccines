package org.james.gos.vaccines.common.doman.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 入参ID
 *
 * @author James Gosl
 * @since 2023/08/15 17:02
 */
@Data
@ApiModel("入参ID")
public class IdReq {

    @ApiModelProperty("id")
    @NotNull
    private Long id;
}
