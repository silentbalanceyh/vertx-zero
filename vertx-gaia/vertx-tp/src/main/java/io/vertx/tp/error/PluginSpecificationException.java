package io.vertx.tp.error;

import io.vertx.up.exception.UpException;

public class PluginSpecificationException extends UpException {

    public PluginSpecificationException(final Class<?> clazz,
                                        final String key) {
        super(clazz, key);
    }

    @Override
    public int getCode() {
        return -40016;
    }
}
