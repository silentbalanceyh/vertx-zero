package io.vertx.up.uca.serialization;

import io.vertx.up.atom.secure.Vis;
import io.vertx.up.fn.Fn;

public class VisSaber extends BaseSaber {

    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        return Fn.getNull(() -> Vis.smart(literal), paramType, literal);
    }
}
