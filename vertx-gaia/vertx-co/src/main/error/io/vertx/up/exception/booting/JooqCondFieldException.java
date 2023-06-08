package io.vertx.up.exception.booting;

import io.horizon.exception.BootingException;

public class JooqCondFieldException extends BootingException {

    public JooqCondFieldException(final Class<?> clazz,
                                  final String targetField) {
        super(clazz, targetField);
    }

    @Override
    public int getCode() {
        return -40055;
    }
}
