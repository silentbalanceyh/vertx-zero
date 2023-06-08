package io.vertx.up.plugin.cache.util;

import io.horizon.uca.log.Annal;
import io.vertx.core.Future;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 * Cache-Aside
 */
public class CacheAside {
    private static final Annal LOGGER = Annal.get(CacheAside.class);

    public static <T> T after(final Supplier<T> executor, final Consumer<T> consumer) {
        /*
         * 1. Get T reference
         */
        final T reference = executor.get();
        if (Objects.nonNull(reference)) {
            /*
             * 2. Call after function
             */
            consumer.accept(reference);
        }
        return reference;
    }

    public static <T> Future<T> afterAsync(final Supplier<Future<T>> executor, final Consumer<T> consumer) {
        /*
         * 1. Get T reference in async way
         */
        return executor.get().compose(reference -> {
            if (Objects.nonNull(reference)) {
                /*
                 * 2. Call after function
                 */
                consumer.accept(reference);
            }
            return Future.succeededFuture(reference);
        });
    }

    /*
     * T -> T
     */
    public static <T> T before(final Supplier<T> cacheSupplier,
                               final Supplier<T> actualSupplier,
                               final Consumer<T> callback) {
        return beforeSync(cacheSupplier, actualSupplier, Objects::isNull, callback);
    }

    public static <T> T check(final Supplier<T> cacheSupplier,
                              final Supplier<T> actualSupplier) {
        return beforeSync(cacheSupplier, actualSupplier,
            (existing) -> Objects.nonNull(existing) && Boolean.FALSE == existing, null);
    }


    public static <T> Future<T> beforeAsync(final Supplier<Future<T>> cacheSupplier,
                                            final Supplier<Future<T>> actualSupplier,
                                            final Consumer<T> callback) {
        /*
         * 1. Get T reference from cache
         */
        return cacheSupplier.get().compose(reference -> {
            if (Objects.isNull(reference)) {
                /*
                 * 2. When T reference is null
                 */
                LOGGER.debug("[ Cache ] ( Async ) Actual operation will execute because handler to hit cache.");
                return actualSupplier.get().compose(actualRef -> {
                    if (Objects.nonNull(callback) && Objects.nonNull(actualRef)) {
                        /*
                         * 3. When callback is not null
                         */
                        callback.accept(actualRef);
                    }
                    return Future.succeededFuture(actualRef);
                });
            } else {
                return Future.succeededFuture(reference);
            }
        });
    }

    public static Future<Boolean> checkAsync(final Supplier<Future<Boolean>> cacheSupplier,
                                             final Supplier<Future<Boolean>> actualSupplier) {
        return cacheSupplier.get().compose(reference -> {
            if (reference) {
                /*
                 * Existing Here
                 */
                return Future.succeededFuture(Boolean.TRUE);
            } else {
                LOGGER.debug("[ Cache ] ( Async ) Actual operation will execute because handler to hit cache.");
                return actualSupplier.get();
            }
        });
    }


    // ------------------ Private Processing -------------------------
    private static <T> T beforeSync(final Supplier<T> supplier,
                                    final Supplier<T> actual,
                                    final Predicate<T> predicate,
                                    final Consumer<T> callback) {
        /*
         * 1. Get T reference from cache
         */
        T reference = supplier.get();
        if (predicate.test(reference)) {
            /*
             * 2. When T reference is null
             */
            LOGGER.debug("[ Cache ] ( Sync ) Actual operation will execute because handler to hit cache.");
            reference = actual.get();
            if (Objects.nonNull(callback) && Objects.nonNull(reference)) {
                /*
                 * 3. When callback is not null
                 */
                callback.accept(reference);
            }
        }
        return reference;
    }
}
