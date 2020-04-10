package io.vertx.up.exception.zero;

import io.vertx.up.exception.UpException;

public class DynamicConfigTypeException extends UpException {

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
