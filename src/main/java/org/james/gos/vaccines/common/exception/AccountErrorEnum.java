package org.james.gos.vaccines.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * TODO
 *
 * @author James Gosl
 * @since 2023/08/16 17:25
 */
@Getter
@AllArgsConstructor
public enum AccountErrorEnum implements ErrorEnum {
    NOT_LOGIN(100, "未登录"),
    NOT_AUTH(101, "无权限"),
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
