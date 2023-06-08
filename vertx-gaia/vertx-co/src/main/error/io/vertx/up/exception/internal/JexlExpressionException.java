package io.vertx.up.exception.internal;

import io.horizon.exception.InternalException;
import io.vertx.up.eon.em.ErrorZero;
import io.vertx.up.util.Ut;

public class JexlExpressionException extends InternalException {

    public JexlExpressionException(final Class<?> caller,
                                   final String expression,
                                   final Throwable ex) {
        super(caller, Ut.fromMessage(ErrorZero._15002.M(), expression, ex.getMessage()));
    }

    @Override
    public int getCode() {
        return ErrorZero._15002.V();
    }
}
