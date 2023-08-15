package org.james.gos.vaccines.common.doman.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 响应ID
 *
 * @author James Gosl
 * @since 2023/08/15 17:04
 */
@Data
@Accessors(chain = true)
@ApiModel("响应ID")
public class IdResp {

    @ApiModelProperty("id")
    private long id;

    public static IdResp id(Long id) {
        return new IdResp().setId(id);
    }
}
