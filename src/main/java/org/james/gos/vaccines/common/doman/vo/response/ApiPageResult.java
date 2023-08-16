package org.james.gos.vaccines.common.doman.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.james.gos.vaccines.common.exception.ErrorEnum;

import java.util.List;

/**
 * 分页响应体
 *
 * @author James Gosl
 * @since 2023/08/16 19:41
 */
@Data
@ApiModel("分页响应体")
public class ApiPageResult<T> {
    @ApiModelProperty("成功标识true or false")
    private Boolean success;
    @ApiModelProperty("错误码")
    private Integer errCode;
    @ApiModelProperty("错误消息")
    private String errMsg;
    @ApiModelProperty("响应码")
    private Integer code = 0;
    @ApiModelProperty("数量")
    private Long count;
    @ApiModelProperty("载荷")
    private T data;

    public static <T> ApiPageResult<T> success() {
        ApiPageResult<T> result = new ApiPageResult<T>();
        result.setData(null);
        result.setSuccess(Boolean.TRUE);
        return result;
    }

    public static <T> ApiPageResult<T> success(Long count, T data) {
        ApiPageResult<T> result = new ApiPageResult<T>();
        result.setCount(count);
        result.setData(data);
        result.setSuccess(Boolean.TRUE);
        return result;
    }

    public static <T> ApiPageResult<T> fail(Integer code, String msg) {
        ApiPageResult<T> result = new ApiPageResult<T>();
        result.setSuccess(Boolean.FALSE);
        result.setErrCode(code);
        result.setErrMsg(msg);
        return result;
    }

    public static <T> ApiPageResult<T> fail(ErrorEnum errorEnum) {
        ApiPageResult<T> result = new ApiPageResult<T>();
        result.setSuccess(Boolean.FALSE);
        result.setErrCode(errorEnum.getErrorCode());
        result.setErrMsg(errorEnum.getErrorMsg());
        return result;
    }

    public boolean isSuccess() {
        return this.success;
    }
}
