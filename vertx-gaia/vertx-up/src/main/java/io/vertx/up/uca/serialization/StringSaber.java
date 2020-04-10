package io.vertx.up.uca.serialization;

import io.vertx.up.eon.Strings;
import io.vertx.up.fn.Fn;

/**
 * String
 */
public class StringSaber extends BaseSaber {

    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        return Fn.getNull(() ->
                        Fn.getSemi(String.class == paramType, getLogger(),
                                () -> literal, () -> Strings.EMPTY),
                paramType, literal);
    }
}
