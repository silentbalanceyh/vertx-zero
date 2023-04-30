package io.vertx.up.exception.booting;

import io.horizon.exception.BootingException;

public class DynamicConfigTypeException extends BootingException {

    public DynamicConfigTypeException(final Class<?> clazz,
                                      final String key,
                                      final Class<?> current) {
        super(clazz, key, current);
    }

    @Override
    public int getCode() {
        return -10007;
    }
}
