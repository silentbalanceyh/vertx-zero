package io.vertx.tp.error;

import io.vertx.up.exception.UpException;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class InternalConflictException extends UpException {

    public InternalConflictException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40070;
    }
}
