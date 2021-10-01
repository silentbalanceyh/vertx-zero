package io.vertx.tp.error;

import io.vertx.up.exception.UpException;

public class WallTypeWrongException extends UpException {

    public WallTypeWrongException(final Class<?> clazz,
                                  final String key,
                                  final Class<?> target) {
        super(clazz, key, target);
    }

    @Override
    public int getCode() {
        return -40075;
    }
}
