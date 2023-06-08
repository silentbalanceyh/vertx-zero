package io.vertx.up.uca.serialization;

import io.vertx.up.commune.secure.Vis;
import io.vertx.up.fn.Fn;

public class VisSaber extends BaseSaber {

    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        return Fn.runOr(() -> Vis.smart(literal), paramType, literal);
    }
}
