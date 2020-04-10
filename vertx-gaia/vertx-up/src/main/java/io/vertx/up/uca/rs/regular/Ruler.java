package io.vertx.up.uca.rs.regular;

import io.vertx.up.atom.Rule;
import io.vertx.up.exception.WebException;

import java.util.Collection;

public interface Ruler {
    /**
     * Verify each field for @BodyParam
     *
     * @param field
     * @param value
     * @param rule
     * @return
     */
    WebException verify(final String field,
                        final Object value,
                        final Rule rule);

    static Ruler get(final String type) {
        return Pool.RULERS.get(type);
    }

    static WebException verify(final Collection<Rule> rules,
                               final String field,
                               final Object value) {
        WebException error = null;
        for (final Rule rule : rules) {
            final Ruler ruler = get(rule.getType());
            if (null != ruler) {
                error = ruler.verify(field, value, rule);
            }
            // Error found
            if (null != error) {
                break;
            }
        }
        return error;
    }
}
