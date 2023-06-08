package io.vertx.up.backbone.announce;

import io.horizon.exception.WebException;
import io.vertx.up.atom.Rule;

import java.util.List;
import java.util.Map;

public class JArrayRigor implements Rigor {
    @Override
    public WebException verify(final Map<String, List<Rule>> rulers,
                               final Object body) {
        final WebException error = null;
        if (!rulers.isEmpty()) {

        }
        return error;
    }
}
