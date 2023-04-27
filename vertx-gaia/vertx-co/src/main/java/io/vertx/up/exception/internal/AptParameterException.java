package io.vertx.up.exception.internal;

import io.horizon.exception.InternalException;
import io.vertx.up.eon.em.ErrorZero;

public class AptParameterException extends InternalException {

    public AptParameterException(final Class<?> caller) {
        super(caller, ErrorZero._15000.M());
    }

    @Override
    public int getCode() {
        return ErrorZero._15000.V();
    }
}
