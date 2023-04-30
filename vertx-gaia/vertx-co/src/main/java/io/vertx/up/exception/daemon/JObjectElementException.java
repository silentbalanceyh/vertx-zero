package io.vertx.up.exception.daemon;

import io.horizon.exception.DaemonException;

public class JObjectElementException extends DaemonException {

    public JObjectElementException(final Class<?> clazz,
                                   final int index,
                                   final Object value) {
        super(clazz, index, value);
    }

    @Override
    public int getCode() {
        return -10001;
    }
}
