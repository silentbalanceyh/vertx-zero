package io.vertx.up.exception.boot;

import io.horizon.exception.BootingException;

public class EventCodexMultiException extends BootingException {

    public EventCodexMultiException(final Class<?> clazz,
                                    final Class<?> target) {
        super(clazz, target);
    }

    @Override
    public int getCode() {
        return -40036;
    }
}
