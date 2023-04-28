package io.vertx.up.uca.serialization;

import io.horizon.eon.VString;
import io.vertx.up.fn.Fn;

/**
 * String
 */
public class StringSaber extends BaseSaber {

    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        return Fn.runOr(() ->
                Fn.runOr(String.class == paramType, this.getLogger(),
                    () -> literal, () -> VString.EMPTY),
            paramType, literal);
    }
}
