package io.vertx.up.backbone.regular;

import io.horizon.eon.VValue;
import io.horizon.exception.WebException;
import io.vertx.up.atom.Rule;

/**
 * {
 * "type":"minlength",
 * "message":"xxx",
 * "config":{
 * "value":8
 * }
 * }
 */
class MaxLengthRuler extends BaseRuler {

    @Override
    public WebException verify(final String field,
                               final Object value,
                               final Rule rule) {
        WebException error = null;
        final int length = null == value ? VValue.ZERO : value.toString().length();
        final int max = rule.getConfig().getInteger("max");
        if (length > max) {
            error = this.failure(field, value, rule);
        }
        return error;
    }
}
