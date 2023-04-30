package io.vertx.up.uca.rs.announce;

import io.vertx.up.atom.Rule;
import io.horizon.exception.WebException;

import java.util.List;
import java.util.Map;

public interface Rigor {

    static Rigor get(final Class<?> clazz) {
        return Pool.RIGORS.get(clazz);
    }

    WebException verify(final Map<String, List<Rule>> rulers,
                        final Object value);
}
