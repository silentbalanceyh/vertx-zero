package io.vertx.up.exception.booting;

import io.horizon.exception.BootingException;

public class JooqConfigurationException extends BootingException {

    public JooqConfigurationException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40065;
    }
}
