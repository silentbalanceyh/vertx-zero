package io.vertx.up.backbone.regular;

import io.horizon.exception.WebException;
import io.vertx.up.atom.Rule;

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
