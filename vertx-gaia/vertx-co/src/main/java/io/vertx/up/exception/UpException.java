package io.vertx.up.exception;

import io.horizon.eon.VString;
import io.horizon.exception.AbstractException;
import io.vertx.up.log.Errors;

/**
 * Top Exception for error code mapping ( Runtime )
 */
public abstract class UpException extends AbstractException {
    private final String message;
    private final Class<?> target;

    public UpException(final Class<?> clazz, final Object... args) {
        super(VString.EMPTY);
        this.target = clazz;
        this.message = Errors.normalize(clazz, this.getCode(), args);
    }

    public abstract int getCode();

    @Override
    public String getMessage() {
        return this.message;
    }

    public Class<?> getTarget() {
        return this.target;
    }
}
