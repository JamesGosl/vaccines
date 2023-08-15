package org.james.gos.vaccines.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用异常码
 *
 * @author James Gosl
 * @since 2023/08/15 17:43
 */
@AllArgsConstructor
@Getter
public enum CommonErrorEnum implements ErrorEnum {
    SYSTEM_ERROR(-1, "系统出小差了，请稍后再试哦~~"),
    PARAM_VALID(-2, "参数校验失败，你在试试别的~~"),
    ACCOUNT(-3, "账户有问题，你想干啥~~"),
    ;

    private final Integer code;
    private final String msg;

    @Override
    public Integer getErrorCode() {
        return this.code;
    }

    @Override
    public String getErrorMsg() {
        return this.msg;
    }
}
