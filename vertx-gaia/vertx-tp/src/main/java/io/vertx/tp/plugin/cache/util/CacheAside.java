package io.vertx.tp.plugin.cache.util;

import io.vertx.core.Future;
import io.vertx.up.fn.RunSupplier;
import io.vertx.up.log.Annal;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * Cache-Aside
 */
public class CacheAside {
    private static final Annal LOGGER = Annal.get(CacheAside.class);

    public static <T> T after(final RunSupplier<T> executor, final Consumer<T> consumer) {
        /*
         * 1. Get T reference
         */
        final T reference = execute(executor);
        if (Objects.nonNull(reference)) {
            /*
             * 2. Call after function
             */
            consumer.accept(reference);
        }
        return reference;
    }

    public static <T> Future<T> afterAsync(final RunSupplier<Future<T>> executor, final Consumer<T> consumer) {
        /*
         * 1. Get T reference in async way
         */
        final Supplier<Future<T>> actual = () -> execute(executor);
        return actual.get().compose(reference -> {
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
    public static <T> T before(final RunSupplier<T> cacheSupplier,
                               final RunSupplier<T> actual) {
        return before(cacheSupplier, actual, null);
    }

    public static <T> T before(final RunSupplier<T> cacheSupplier,
                               final RunSupplier<T> actualSupplier,
                               final Consumer<T> callback) {
        final Supplier<T> supplier = () -> execute(cacheSupplier);
        final Supplier<T> actual = () -> execute(actualSupplier);
        /*
         * 1. Get T reference from cache
         */
        T reference = supplier.get();
        if (Objects.isNull(reference)) {
            /*
             * 2. When T reference is null
             */
            LOGGER.info("[ Cache ] ( Sync ) Actual operation will execute because failure to hit cache.");
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

    public static <T> Future<T> before(final Supplier<Future<T>> cacheSupplier,
                                       final RunSupplier<Future<T>> actualSupplier) {
        return before(cacheSupplier, actualSupplier, null);
    }

    public static <T> Future<T> before(final Supplier<Future<T>> cacheSupplier,
                                       final RunSupplier<Future<T>> actualSupplier,
                                       final Consumer<T> callback) {
        /*
         * 1. Get T reference from cache
         */
        final Supplier<Future<T>> actual = () -> execute(actualSupplier);
        return cacheSupplier.get().compose(reference -> {
            if (Objects.isNull(reference)) {
                /*
                 * 2. When T reference is null
                 */
                LOGGER.info("[ Cache ] ( Async ) Actual operation will execute because failure to hit cache.");
                return actual.get().compose(actualRef -> {
                    if (Objects.nonNull(callback) && Objects.nonNull(actualRef)) {
                        /*
                         * 3. When callback is not null
                         */
                        callback.accept(actualRef);
                    }
                    return Future.succeededFuture(actualRef);
                });
            } else return Future.succeededFuture(reference);
        });
    }

    private static <T> T execute(final RunSupplier<T> supplier) {
        T reference;
        try {
            reference = supplier.get();
        } catch (final Throwable ex) {
            ex.printStackTrace();
            reference = null;
        }
        return reference;
    }
}
