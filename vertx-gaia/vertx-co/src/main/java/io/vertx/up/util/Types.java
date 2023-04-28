package io.vertx.up.util;

import io.horizon.util.HaS;
import io.vertx.core.json.JsonObject;
import io.horizon.uca.log.Annal;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("all")
final class Types {
    private static final Annal LOGGER = Annal.get(Types.class);

    private Types() {
    }

    static <T> boolean isEqual(final JsonObject record, final String field, final T expected) {
        if (HaS.isNil(record)) {
            /*
             * If record is null or empty, return `false`
             */
            return false;
        } else {
            /*
             * Object reference
             */
            final Object value = record.getValue(field);
            return HaS.isSame(value, expected);
        }
    }

    static boolean isIn(final JsonObject input, final String... fields) {
        if (Ut.isNil(input)) {
            return false;
        } else {
            final Set<String> fieldSet = Arrays.stream(fields).collect(Collectors.toSet());
            final long counter = input.fieldNames().stream()
                .filter(Objects::nonNull)
                .filter(fieldSet::contains)
                .map(input::getValue)
                .filter(item -> item instanceof String)
                .map(item -> (String) item)
                .filter(Ut::isNotNil)
                .count();
            return counter == fields.length;
        }
    }

    static boolean isArray(final Object value) {
        if (null == value) {
            return false;
        }
        return (value instanceof Collection ||
            value.getClass().isArray());
    }
}
