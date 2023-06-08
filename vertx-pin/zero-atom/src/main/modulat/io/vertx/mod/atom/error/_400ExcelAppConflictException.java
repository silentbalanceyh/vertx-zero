package io.vertx.mod.atom.error;

import io.horizon.exception.WebException;

public class _400ExcelAppConflictException extends WebException {

    public _400ExcelAppConflictException(final Class<?> clazz,
                                         final String expected,
                                         final String current) {
        super(clazz, expected, current);
    }

    @Override
    public int getCode() {
        return -80511;
    }
}
