package io.vertx.up.exception.internal;

import io.horizon.exception.InternalException;
import io.vertx.up.eon.em.ErrorZero;
import io.vertx.up.util.Ut;

public class DateFormatException extends InternalException {

    public DateFormatException(final Class<?> caller, final String literal) {
        super(caller, Ut.fromMessage(ErrorZero._15001.M(), literal));
    }

    @Override
    public int getCode() {
        return ErrorZero._15001.V();
    }
}
