package io.vertx.up.exception;

import io.horizon.exception.ZeroException;
import io.vertx.up.log.Errors;

/**
 * Top Exception for error code mapping.
 */
public abstract class DemonException extends ZeroException {

    private final String message;

    public DemonException(final Class<?> clazz, final Object... args) {
        super(null);
        message = Errors.normalize(clazz, getCode(), args);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
