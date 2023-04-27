package io.vertx.up.exception.internal;

import io.horizon.exception.InternalException;
import io.vertx.up.eon.em.ErrorZero;
import io.vertx.up.util.Ut;

/**
 * vertx:
 * lime: error, inject, db, consul
 */
public class LimeMissingException extends InternalException {

    public LimeMissingException(final Class<?> caller, final String filename) {
        super(caller, Ut.fromMessage(ErrorZero._15003.M(), filename));
    }

    @Override
    public int getCode() {
        return ErrorZero._15003.V();
    }
}
