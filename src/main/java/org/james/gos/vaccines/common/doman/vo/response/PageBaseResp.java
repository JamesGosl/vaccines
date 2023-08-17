package org.james.gos.vaccines.common.doman.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.james.gos.vaccines.common.exception.ErrorEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 分页响应体
 *
 * @author James Gosl
 * @since 2023/08/16 19:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("分页响应体")
public class PageBaseResp<T> {
    @ApiModelProperty("数量")
    private Long count;
    @ApiModelProperty("载荷")
    private List<T> data;

    public static <T> PageBaseResp<T> empty() {
        PageBaseResp<T> pageBaseResp = new PageBaseResp<>();
        pageBaseResp.setCount(0L);
        pageBaseResp.setData(new ArrayList<>());
        return pageBaseResp;
    }

    public static <T> PageBaseResp<T> init(Long count, List<T> list) {
        return new PageBaseResp<>(count, list);
    }
}
