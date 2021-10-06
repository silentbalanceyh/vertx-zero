package io.vertx.tp.error;

import io.vertx.up.exception.UpException;

public class WallProviderConflictException extends UpException {

    public WallProviderConflictException(final Class<?> clazz,
                                         final Class<?> target) {
        super(clazz, target);
    }

    @Override
    public int getCode() {
        return -40077;
    }
}
