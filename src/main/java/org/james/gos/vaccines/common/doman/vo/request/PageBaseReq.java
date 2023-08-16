package org.james.gos.vaccines.common.doman.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 基础分页请求
 *
 * @author James Gosl
 * @since 2023/08/16 17:38
 */
@Data
@ApiModel("基础分页请求")
public class PageBaseReq {

    @ApiModelProperty("页面页码")
    private Integer page = 1;

    @ApiModelProperty("页面大小")
    @Max(50)
    @Min(10)
    private Integer limit = 10;
}
