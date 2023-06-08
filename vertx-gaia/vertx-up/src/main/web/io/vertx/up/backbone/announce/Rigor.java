package io.vertx.up.backbone.announce;

import io.horizon.exception.WebException;
import io.vertx.up.atom.Rule;

import java.util.List;
import java.util.Map;

public interface Rigor {

    static Rigor get(final Class<?> clazz) {
        return CACHE.RIGORS.get(clazz);
    }

    WebException verify(final Map<String, List<Rule>> rulers,
                        final Object value);
}
