package io.vertx.up.uca.serialization;

import io.vertx.up.eon.bridge.Strings;
import io.vertx.up.fn.Fn;

/**
 * String
 */
public class StringSaber extends BaseSaber {

    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        return Fn.orNull(() ->
                Fn.orSemi(String.class == paramType, this.getLogger(),
                    () -> literal, () -> Strings.EMPTY),
            paramType, literal);
    }
}
