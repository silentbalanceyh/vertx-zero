package io.vertx.tp.error;

import io.horizon.exception.BootingException;

public class PluginOptionException extends BootingException {

    public PluginOptionException(final Class<?> clazz,
                                 final String name) {
        super(clazz, name);
    }

    @Override
    public int getCode() {
        return -40015;
    }
}
