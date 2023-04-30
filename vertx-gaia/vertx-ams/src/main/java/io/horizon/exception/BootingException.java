package io.horizon.exception;

import io.horizon.eon.VString;
import io.horizon.eon.error.ErrorMessage;
import io.horizon.util.HaS;

/**
 * 和资源文件绑定的启动异常类，通常在容器启动中抛出该信息
 * 1. 模型校验
 * 2. 容器启动
 */
public abstract class BootingException extends AbstractException {
    private final String message;
    private final Class<?> target;

    public BootingException(final Class<?> clazz, final Object... args) {
        super(VString.EMPTY);
        this.target = clazz;
        this.message = HaS.fromError(ErrorMessage.EXCEPTION_BOOTING, clazz, this.getCode(), args); // Errors.format(clazz, this.getCode(), args);
    }

    @Override
    public abstract int getCode();

    @Override
    public String getMessage() {
        return this.message;
    }

    public Class<?> getTarget() {
        return this.target;
    }
}
