package io.vertx.up.exception;

import io.vertx.core.VertxException;

/**
 * Extend from vert.x exception
 *
 * @see io.vertx.core.VertxException
 */
public abstract class ZeroRunException extends VertxException {
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
