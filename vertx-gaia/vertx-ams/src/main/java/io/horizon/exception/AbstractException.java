package io.horizon.exception;

/**
 * Extend from vert.x exception
 */
public abstract class AbstractException extends RuntimeException {
    public AbstractException() {
        super();
    }

    public AbstractException(final String message) {
        super(message);
    }

    public AbstractException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AbstractException(final Throwable cause) {
        super(cause);
    }

    protected abstract int getCode();

    protected Class<?> caller() {
        return Void.class;
    }
}
