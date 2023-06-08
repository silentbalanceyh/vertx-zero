package io.horizon.exception;

import io.horizon.eon.VString;
import io.horizon.eon.error.ErrorMessage;
import io.horizon.util.HUt;

/**
 * 内部异常，不和资源文件绑定
 *
 * @author lang : 2023/4/27
 */
public abstract class InternalException extends AbstractException {
    private final Class<?> caller;
    private final String message;

    public InternalException(final Class<?> caller) {
        super();
        this.caller = caller;
        this.message = VString.EMPTY;
    }

    /*
     * 内部异常不绑定资源文件，所以不执行 HaS.fromError 的调用
     * 此处直接做消息级别初始化，以防止调用混乱的情况
     * [ ERR{} ] ( {} ) Internal Error : {}
     */
    public InternalException(final Class<?> caller, final String message, final Throwable cause) {
        super(message, cause);
        this.caller = caller;
        this.message = HUt.fromMessage(ErrorMessage.EXCEPTION_INTERNAL,
            String.valueOf(this.getCode()), caller.getSimpleName(), message);
    }

    public InternalException(final Class<?> caller, final String message) {
        super(message);
        this.caller = caller;
        this.message = HUt.fromMessage(ErrorMessage.EXCEPTION_INTERNAL,
            String.valueOf(this.getCode()), caller.getSimpleName(), message);
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public Class<?> caller() {
        return this.caller;
    }
}
