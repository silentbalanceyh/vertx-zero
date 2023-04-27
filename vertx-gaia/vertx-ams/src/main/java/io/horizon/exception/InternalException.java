package io.horizon.exception;

import io.horizon.eon.error.ErrorMessage;
import io.horizon.util.HaS;

/**
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
        return HaS.fromMessage(ErrorMessage.E_1X_MESSAGE, String.valueOf(this.getCode()), super.getMessage());
    }

    @Override
    public Class<?> caller() {
        return this.caller;
    }
}
