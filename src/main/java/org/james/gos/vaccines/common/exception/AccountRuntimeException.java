package org.james.gos.vaccines.common.exception;

/**
 * 账户相关异常类
 *
 * @author James Gosl
 * @since 2023/08/15 20:22
 */
public class AccountRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 826632462353374500L;

    public AccountRuntimeException() {
        super("权限不足");
    }

    public AccountRuntimeException(String message) {
        super(message);
    }

    public AccountRuntimeException(ErrorEnum errorEnum) {
        super(errorEnum.getErrorMsg());
    }
}
