package io.vertx.up.fn;

import io.vertx.up.exception.heart.ArgumentException;
import io.vertx.up.log.Errors;

final class Input {

    private Input() {
    }

    static void eqLength(final Class<?> clazz, final int expected, final Object... args) {
        if (expected != args.length) {
            final String method = Errors.method(Input.class, "eqLength");
            throw new ArgumentException(clazz, method, expected, "=");
        }
    }

    static void gtLength(final Class<?> clazz, final int min, final Object... args) {
        if (min >= args.length) {
            final String method = Errors.method(Input.class, "gtLength");
            throw new ArgumentException(clazz, method, min, ">");
        }
    }
}
