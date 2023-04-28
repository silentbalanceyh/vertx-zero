package io.vertx.up.exception;

import io.horizon.exception.ProgramException;
import io.horizon.uca.log.Errors;

/**
 * Top Exception for error code mapping.
 */
public abstract class DemonException extends ProgramException {

    private final String message;

    public DemonException(final Class<?> clazz, final Object... args) {
        super(null);
        this.message = Errors.normalize(clazz, this.getCode(), args);
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
