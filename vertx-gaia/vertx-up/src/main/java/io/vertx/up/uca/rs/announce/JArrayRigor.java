package io.vertx.up.uca.rs.announce;

import io.vertx.up.atom.Rule;
import io.vertx.up.exception.WebException;

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
