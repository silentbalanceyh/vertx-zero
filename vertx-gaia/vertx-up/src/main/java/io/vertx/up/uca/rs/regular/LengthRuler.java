package io.vertx.up.uca.rs.regular;

import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.Rule;
import io.horizon.exception.WebException;

class LengthRuler extends BaseRuler {

    @Override
    public WebException verify(final String field,
                               final Object value,
                               final Rule rule) {
        WebException error = null;
        // Extract length
        final JsonObject config = rule.getConfig();
        if (config.containsKey("max")) {
            final Ruler ruler = Ruler.get("maxlength");
            error = ruler.verify(field, value, rule);
        }
        if (null == error && config.containsKey("min")) {
            final Ruler ruler = Ruler.get("minlength");
            error = ruler.verify(field, value, rule);
        }
        return error;
    }
}
