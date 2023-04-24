package io.vertx.up.uca.rs.regular;

import io.vertx.up.atom.Rule;
import io.vertx.up.eon.bridge.Values;
import io.vertx.up.exception.WebException;

/**
 * {
 * "type":"minlength",
 * "message":"xxx",
 * "config":{
 * "value":8
 * }
 * }
 */
class MinLengthRuler extends BaseRuler {

    @Override
    public WebException verify(final String field,
                               final Object value,
                               final Rule rule) {
        WebException error = null;
        final int length = null == value ? Values.ZERO : value.toString().length();
        final int min = rule.getConfig().getInteger("min");
        if (length < min) {
            error = failure(field, value, rule);
        }
        return error;
    }
}
