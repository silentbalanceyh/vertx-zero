package io.horizon.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author lang : 2023/4/27
 */
class HIs {
    static boolean isNil(final String str) {
        return Objects.isNull(str) || str.trim().isEmpty();
    }

    static boolean isNil(final JsonObject inputJ) {
        return Objects.isNull(inputJ) || inputJ.isEmpty();
    }

    static boolean isNil(final JsonArray inputA) {
        return Objects.isNull(inputA) || inputA.isEmpty();
    }

    static boolean isNull(final Object... args) {
        return 0 == args.length || Arrays.stream(args).allMatch(Objects::isNull);
    }

    static boolean isNotNull(final Object... args) {
        return 0 == args.length || Arrays.stream(args).noneMatch(Objects::isNull);
    }
}
