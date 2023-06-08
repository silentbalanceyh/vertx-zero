package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

public class EventSourceException extends BootingException {

    public EventSourceException(final Class<?> clazz,
                                final String endpointCls) {
        super(clazz, endpointCls);
    }

    @Override
    public int getCode() {
        return -40005;
    }
}
