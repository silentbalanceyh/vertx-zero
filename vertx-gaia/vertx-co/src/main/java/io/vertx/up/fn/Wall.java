package io.vertx.up.fn;

import io.vertx.core.VertxException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Values;
import io.vertx.up.exception.ZeroException;
import io.vertx.up.exception.ZeroRunException;
import io.vertx.up.exception.heart.ArgumentException;
import io.vertx.up.exception.heart.PoolKeyNullException;
import io.vertx.up.log.Annal;
import io.vertx.up.log.Errors;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * Defend means swapper the exception part for specific statement.
 * Uniform to manage exception code flow.
 */
final class Wall {
    private Wall() {
    }

    /**
     * Execute without any return type
     *
     * @param actuator Jvm Executor
     * @param logger   Zero logger
     */
    static void jvmVoid(final JvmActuator actuator, final Annal logger) {
        try {
            actuator.execute();
        } catch (final Throwable ex) {
            Annal.sure(logger, () -> logger.jvm(ex));
            // TODO: Debug for JVM
            ex.printStackTrace();
        }
    }

    /**
     * Execute with return T
     *
     * @param supplier Jvm Supplier
     * @param logger   Zero logger
     * @param <T>      returned supplier T
     *
     * @return T supplier or null
     */
    static <T> T jvmReturn(final JvmSupplier<T> supplier, final Annal logger) {
        T reference = null;
        try {
            reference = supplier.get();
        } catch (final Exception ex) {
            Annal.sure(logger, () -> logger.jvm(ex));
            // TODO: Debug for JVM
            ex.printStackTrace();
        }
        return reference;
    }

    /**
     * @param actuator Zero executor
     * @param logger   Zero logger
     */
    static void zeroVoid(final ZeroActuator actuator, final Annal logger) {
        try {
            actuator.execute();
        } catch (final ZeroException ex) {
            Annal.sure(logger, () -> logger.zero(ex));
        } catch (final VertxException ex) {
            Annal.sure(logger, () -> logger.vertx(ex));
        } catch (final Throwable ex) {
            Annal.sure(logger, () -> logger.jvm(ex));
            // TODO: Debug for JVM
            ex.printStackTrace();
        }
    }

    /**
     * @param supplier Zero Supplier
     * @param logger   Zero Logger
     * @param <T>      Element of supplier ( T )
     *
     * @return T or throw out zero run exception
     */
    static <T> T zeroReturn(final ZeroSupplier<T> supplier, final Annal logger) {
        T ret = null;
        try {
            ret = supplier.get();
        } catch (final ZeroException ex) {
            Annal.sure(logger, () -> logger.zero(ex));
        } catch (final ZeroRunException ex) {
            Annal.sure(logger, () -> {
                logger.vertx(ex);
                throw ex;
            });
        } catch (final VertxException ex) {
            Annal.sure(logger, () -> logger.vertx(ex));
        } catch (final Throwable ex) {
            Annal.sure(logger, () -> logger.jvm(ex));
            // TODO: Debug for JVM
            ex.printStackTrace();
        }
        return ret;
    }


    static <T> T execReturn(final Supplier<T> supplier, final T defaultValue) {
        final T ret = supplier.get();
        return null == ret ? defaultValue : ret;
    }

    static void exec(final boolean condition, final Annal logger, final Actuator tSupplier, final Actuator fSupplier) {
        Wall.zeroVoid(() -> execZero(condition,
            () -> {
                if (null != tSupplier) {
                    tSupplier.execute();
                }
                return null;
            }, () -> {
                if (null != fSupplier) {
                    fSupplier.execute();
                }
                return null;
            }), logger);
    }

    @SuppressWarnings("all")
    static <T> T execZero(final boolean condition, final ZeroSupplier<T> tSupplier, final ZeroSupplier<T> fSupplier)
        throws ZeroException {
        T ret = null;
        if (condition) {
            if (null != tSupplier) {
                ret = tSupplier.get();
            }
        } else {
            if (null != fSupplier) {
                ret = fSupplier.get();
            }
        }
        return ret;
    }

    static void verifyEqLength(final Class<?> clazz, final int expected, final Object... args) {
        if (expected != args.length) {
            final String method = Errors.method(Wall.class, "eqLength");
            throw new ArgumentException(clazz, method, expected, "=");
        }
    }

    static void verifyEtLength(final Class<?> clazz, final int min, final Object... args) {
        if (min >= args.length) {
            final String method = Errors.method(Wall.class, "gtLength");
            throw new ArgumentException(clazz, method, min, ">");
        }
    }

    @SuppressWarnings("all")
    static <T> void execZero(final JsonObject data, final ZeroBiConsumer<T, String> fnIt)
        throws ZeroException {
        for (final String name : data.fieldNames()) {
            final Object item = data.getValue(name);
            if (null != item) {
                fnIt.accept((T) item, name);
            }
        }
    }

    /**
     * @param dataArray JsonArray that will be iterated
     * @param fnIt      iterator
     * @param <T>       element type
     */
    static <T> void execZero(final JsonArray dataArray, final ZeroBiConsumer<T, String> fnIt)
        throws ZeroException {
        execZero(dataArray, JsonObject.class, (element, index) -> execZero(element, fnIt));
    }

    /**
     * @param dataArray JsonArray that will be iterated
     * @param clazz     expected class
     * @param fnIt      iterator
     * @param <T>       element type T ( generic )
     *
     * @throws ZeroException element zero here
     */
    @SuppressWarnings("all")
    static <T> void execZero(final JsonArray dataArray, final Class<T> clazz, final ZeroBiConsumer<T, Integer> fnIt)
        throws ZeroException {
        final int size = dataArray.size();
        for (int idx = Values.IDX; idx < size; idx++) {
            final Object value = dataArray.getValue(idx);
            if (null != value) {
                if (clazz == value.getClass()) {
                    final T item = (T) value;
                    fnIt.accept(item, idx);
                }
            }
        }
    }

    /**
     * Memory cache pool implemented by ConcurrentMap( k = v ) instead of create new each time
     *
     * @param pool   Memory concurrent hash map
     * @param key    Input key for cache
     * @param poolFn Supplier of value when create new ( If not in cache )
     * @param <K>    key type
     * @param <V>    value type
     *
     * @return Get or Created V for value
     */
    static <K, V> V execPool(final ConcurrentMap<K, V> pool, final K key, final Supplier<V> poolFn) {
        if (Objects.isNull(key)) {
            throw new PoolKeyNullException();
        }
        V reference = pool.get(key);
        if (null == reference) {
            reference = poolFn.get();
            if (null != reference) {
                pool.put(key, reference);
            }
        }
        return reference;
    }
}
