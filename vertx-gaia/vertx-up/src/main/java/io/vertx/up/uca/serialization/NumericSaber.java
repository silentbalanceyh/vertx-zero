package io.vertx.up.uca.serialization;

import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.function.Function;

/**
 * Int, Long, Short
 */
public abstract class NumericSaber extends BaseSaber {

    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        return Fn.getNull(() ->
                        Fn.getSemi(isValid(paramType), getLogger(),
                                () -> {
                                    verifyInput(!Ut.isInteger(literal), paramType, literal);
                                    return getFun().apply(literal);
                                }, () -> null),
                paramType, literal);
    }

    protected abstract boolean isValid(final Class<?> paramType);

    protected abstract <T> Function<String, T> getFun();
}
