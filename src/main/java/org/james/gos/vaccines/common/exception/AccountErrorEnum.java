package org.james.gos.vaccines.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AccountErrorEnum
 *
 * @author James Gosl
 * @since 2023/08/16 17:25
 */
@Getter
@AllArgsConstructor
public enum AccountErrorEnum implements ErrorEnum {
    NOT_LOGIN(100, "未登录"),
    NOT_AUTH(101, "无权限"),
    EXISTS(102, "账户已经存在"),
    LOGIN(103, "账户或密码不对"),
    NOT_OPTIONS_THIS(104, "不能操作自己");
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
