package io.vertx.tp.ke.refine;

import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

class KeIs {

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
                    .filter(Ut::notNil)
                    .count();
            return counter == fields.length;
        }
    }
}
