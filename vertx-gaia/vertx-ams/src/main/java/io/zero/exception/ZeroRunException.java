package io.zero.exception;

/**
 * Extend from vert.x exception
 */
public abstract class ZeroRunException extends RuntimeException {
    public ZeroRunException(final String message) {
        super(message);
    }

    public ZeroRunException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ZeroRunException(final Throwable cause) {
        super(cause);
    }
}
