package io.vertx.up.uca.serialization;

import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

public class CommonSaber extends BaseSaber {
    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        return Fn.getNull(() ->
                        Fn.getSemi(!SaberTypes.isSupport(paramType), getLogger(),
                                () -> Ut.deserialize(literal, paramType),
                                () -> null),
                paramType, literal);
    }

    @Override
    public <T> Object from(final T input) {
        return Fn.getNull(() -> {
            Object reference = null;
            if (!SaberTypes.isSupport(input.getClass())) {
                final String literal = Ut.serialize(input);
                reference = new JsonObject(literal);
            }
            return reference;
        }, input);
    }
}
