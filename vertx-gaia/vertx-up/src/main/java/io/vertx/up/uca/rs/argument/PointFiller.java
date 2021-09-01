package io.vertx.up.uca.rs.argument;

import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.eon.Strings;
import io.vertx.up.exception.web._415PointDefineException;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.rs.Filler;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class PointFiller implements Filler {
    @Override
    public Object apply(final String name, final Class<?> paramType, final RoutingContext datum) {
        /*
         * First check the `paramType`, it must be following
         * 1. JsonArray
         * 2. List<T>
         */
        final boolean valid = Objects.nonNull(paramType)        // 1) Type is not null
            && (paramType.isAssignableFrom(List.class)          // 2) Type is implements List interface
            || JsonArray.class == paramType                     // 3) Type is JsonArray data structure
        );
        Fn.out(!valid, _415PointDefineException.class, this.getClass(), paramType);

        final String literal = datum.request().getParam(name);
        if (Objects.isNull(literal)) {
            // No input for this parameters
            return null;
        } else {
            final String normalized;
            if (literal.contains(Strings.QUOTE_DOUBLE)) {
                normalized = literal;
            } else {
                normalized = this.normalize(literal);
            }
            // Convert to correct type
            return this.resolve(paramType, normalized);
        }
    }

    private String normalize(final String literal) {
        final StringBuilder buffer = new StringBuilder();
        final String[] split = literal.split(Strings.COMMA);
        final List<String> elements = new ArrayList<>();
        Arrays.stream(split).forEach(each -> {
            if (Objects.nonNull(each)) {
                if (each.trim().startsWith(Strings.LEFT_SQUARE)) {
                    elements.add(Strings.QUOTE_DOUBLE +
                        each.trim().substring(1)
                        + Strings.QUOTE_DOUBLE);
                } else if (each.trim().endsWith(Strings.RIGHT_SQUARE)) {
                    elements.add(Strings.QUOTE_DOUBLE +
                        each.trim().substring(0, each.trim().length() - 2)
                        + Strings.QUOTE_DOUBLE);
                } else {
                    elements.add(Strings.QUOTE_DOUBLE +
                        each.trim()
                        + Strings.QUOTE_DOUBLE);
                }
            }
        });
        buffer.append(Strings.LEFT_SQUARE);
        buffer.append(Ut.fromJoin(elements));
        buffer.append(Strings.RIGHT_SQUARE);
        return buffer.toString();
    }

    private Object resolve(final Class<?> paramType, final String input) {
        final JsonArray value = Ut.toJArray(input);
        final Object reference;
        if (JsonArray.class == paramType) {
            reference = value;
        } else {
            reference = value.getList();
        }
        return reference;
    }
}
