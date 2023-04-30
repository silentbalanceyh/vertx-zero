package io.vertx.tp.error;

import io.horizon.exception.BootingException;

public class PluginUpException extends BootingException {

    public PluginUpException(final Class<?> clazz,
                             final String name,
                             final String message) {
        super(clazz, name, message);
    }

    @Override
    public int getCode() {
        return -40019;
    }
}
