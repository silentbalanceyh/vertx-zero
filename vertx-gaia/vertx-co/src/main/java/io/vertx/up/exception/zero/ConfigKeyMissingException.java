package io.vertx.up.exception.zero;

import io.vertx.up.exception.UpException;

public class ConfigKeyMissingException extends UpException {

    public ConfigKeyMissingException(final Class<?> clazz,
                                     final String key) {
        super(clazz, key);
    }

    @Override
    public int getCode() {
        return -40020;
    }
}
