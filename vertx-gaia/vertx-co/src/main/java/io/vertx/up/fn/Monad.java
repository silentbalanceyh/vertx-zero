package io.vertx.up.fn;

import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author lang : 2023/4/28
 */
class Monad {
    private Monad() {

    }

    static <T> void safeT(final Supplier<T> supplier, final Consumer<T> consumer) {
        final T input = supplier.get();
        if (Objects.nonNull(input)) {
            if (input instanceof String) {
                if (Ut.isNotNil((String) input)) {
                    consumer.accept(input);
                }
            } else {
                consumer.accept(input);
            }
        }
    }
}
