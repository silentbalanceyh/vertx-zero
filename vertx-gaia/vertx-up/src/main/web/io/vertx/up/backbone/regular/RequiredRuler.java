package io.vertx.up.backbone.regular;

import io.horizon.exception.WebException;
import io.vertx.up.atom.Rule;
import io.vertx.up.util.Ut;

/**
 * {
 * "type":"required",
 * "message":"xxx"
 * "config": "None"
 * }
 */
class RequiredRuler extends BaseRuler {

    @Override
    public WebException verify(final String field,
                               final Object value,
                               final Rule rule) {
        WebException error = null;
        if (null == value || Ut.isNil(value.toString())) {
            // Single Field
            error = this.failure(field, value, rule);
        }
        return error;
    }
}
