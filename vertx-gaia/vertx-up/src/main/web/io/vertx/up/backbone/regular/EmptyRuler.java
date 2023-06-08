package io.vertx.up.backbone.regular;

import io.horizon.exception.WebException;
import io.vertx.up.atom.Rule;

import java.util.Collection;

class EmptyRuler extends BaseRuler {
    @Override
    public WebException verify(final String field,
                               final Object value,
                               final Rule rule) {
        WebException error = null;
        if (null != value && value instanceof Collection) {
            final Collection reference = (Collection) value;
            if (reference.isEmpty()) {
                error = failure(field, value, rule);
            }
        }
        return error;
    }
}
