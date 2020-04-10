package io.vertx.up.fn.wait;

import io.vertx.core.Future;

import java.util.function.Supplier;

public class Case<T> extends Tuple<Supplier<Boolean>, Supplier<Future<T>>> {

    private Case(final Supplier<Boolean> condition, final Supplier<Future<T>> result) {
        super(condition, result);
    }

    public static <T> Case<T> item(final Supplier<Boolean> condition, final Supplier<Future<T>> value) {
        return new Case<>(condition, value);
    }

    public static <T> Case.DefaultCase<T> item(final Supplier<Future<T>> value) {
        return new Case.DefaultCase<>(() -> true, value);
    }

    public static final class DefaultCase<T> extends Case<T> {
        private DefaultCase(final Supplier<Boolean> condition, final Supplier<Future<T>> result) {
            super(condition, result);
        }
    }
}
