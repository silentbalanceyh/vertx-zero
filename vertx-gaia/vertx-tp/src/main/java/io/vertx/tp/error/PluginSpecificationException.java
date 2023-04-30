package io.vertx.tp.error;

import io.horizon.exception.BootingException;

public class PluginSpecificationException extends BootingException {

    public PluginSpecificationException(final Class<?> clazz,
                                        final String key) {
        super(clazz, key);
    }

    @Override
    public int getCode() {
        return -40016;
    }
}
