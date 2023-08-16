package org.james.gos.vaccines.common.exception;

/**
 * 数据库插入错误
 *
 * @author James Gosl
 * @since 2023/08/17 00:03
 */
public class InsertRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -6834341936735311048L;

    public InsertRuntimeException() {
    }

    public InsertRuntimeException(String message) {
        super(message);
    }
}
