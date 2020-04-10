package io.vertx.zero.exception;

import io.vertx.up.exception.UpException;

public class EventSourceException extends UpException {

    public EventSourceException(final Class<?> clazz,
                                final String endpointCls) {
        super(clazz, endpointCls);
    }

    @Override
    public int getCode() {
        return -40005;
    }
}
