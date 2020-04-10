package io.vertx.up.exception.zero;

import io.vertx.up.exception.UpException;

public class JooqConfigurationException extends UpException {

    public JooqConfigurationException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40065;
    }
}
