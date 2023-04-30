package io.vertx.up.uca.rs.regular;

import io.vertx.up.atom.Rule;
import io.horizon.exception.WebException;

import java.util.Collection;

class SingleFileRuler extends BaseRuler {

    @Override
    public WebException verify(final String field,
                               final Object value,
                               final Rule rule) {
        WebException error = null;
        if (Collection.class.isAssignableFrom(value.getClass())) {
            error = failure(field, value, rule);
        }
        return error;
    }
}
