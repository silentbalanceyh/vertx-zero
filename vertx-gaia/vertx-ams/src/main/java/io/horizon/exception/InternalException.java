package io.horizon.exception;

import io.horizon.eon.error.ErrorMessage;
import io.horizon.util.HaS;

/**
 * 内部异常，不和资源文件绑定
 *
 * @author lang : 2023/4/27
 */
public abstract class InternalException extends AbstractException {
    private transient final Class<?> caller;

    public InternalException(final Class<?> caller) {
        super();
        this.caller = caller;
    }

    public InternalException(final Class<?> caller, final String message, final Throwable cause) {
        super(message, cause);
        this.caller = caller;
    }

    public InternalException(final Class<?> caller, final String s) {
        super(s);
        this.caller = caller;
    }

    @Override
    public String getMessage() {
        /*
         * 内部异常不绑定资源文件，所以不执行 HaS.fromError 的调用
         * 此处直接做消息级别初始化，以防止调用混乱的情况
         */
        return HaS.fromMessage(ErrorMessage.EXCEPTION_INTERNAL, String.valueOf(this.getCode()), super.getMessage());
    }

    @Override
    public Class<?> caller() {
        return this.caller;
    }
}
