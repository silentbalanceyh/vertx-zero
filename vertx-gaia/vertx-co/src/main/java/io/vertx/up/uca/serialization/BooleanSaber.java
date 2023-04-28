package io.vertx.up.uca.serialization;

import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

/**
 * Boolean
 */
public class BooleanSaber extends BaseSaber {

    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        return Fn.runOr(boolean.class == paramType || Boolean.class == paramType, this.getLogger(),
            () -> {

                this.verifyInput(!Ut.isBoolean(literal), paramType, literal);
                return Boolean.parseBoolean(literal);
            }, () -> Boolean.FALSE);
    }
}
