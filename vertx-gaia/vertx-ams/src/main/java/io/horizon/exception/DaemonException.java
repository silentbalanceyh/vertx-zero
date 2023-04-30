package io.horizon.exception;

import io.horizon.eon.error.ErrorMessage;
import io.horizon.util.HaS;

/**
 * 和资源文件绑定的检查异常类，通常在编程过程中抛出该异常
 * 1. 配置强化校验
 * 2. 容器后台程序校验
 */
public abstract class DaemonException extends ProgramException {

    private final String message;

    public DaemonException(final Class<?> clazz, final Object... args) {
        super(null);
        this.message = HaS.fromError(ErrorMessage.EXCEPTION_DAEMON, clazz, this.getCode(), args);
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
