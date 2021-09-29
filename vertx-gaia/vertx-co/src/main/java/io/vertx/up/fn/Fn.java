package io.vertx.up.fn;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.up.exception.UpException;
import io.vertx.up.exception.WebException;
import io.vertx.up.exception.ZeroException;
import io.vertx.up.exception.ZeroRunException;
import io.vertx.up.fn.wait.Case;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Unique interface to call function
 */
@SuppressWarnings("all")
public final class Fn {
    private Fn() {
    }

    // ------ Case Match
    public static <T> Future<T> match(final Case.DefaultCase<T> defaultCase, final Case<T>... matchers) {
        return Wait.match(() -> defaultCase, matchers).second.get();
    }

    public static <T> Future<T> match(final Supplier<Case.DefaultCase<T>> defaultCase, final Case<T>... matchers) {
        return Wait.match(defaultCase, matchers).second.get();
    }

    public static <T> Case.DefaultCase<T> fork(final Supplier<Future<T>> caseLine) {
        return Case.DefaultCase.item(caseLine);
    }

    public static <T> Case.DefaultCase<T> fork(final Actuator actuator, final Supplier<Future<T>> caseLine) {
        if (null != actuator) actuator.execute();
        return Case.DefaultCase.item(caseLine);
    }

    public static <T> Case<T> branch(final Supplier<Future<T>> caseLine) {
        return Wait.branch(caseLine);
    }

    public static <T> Case<T> branch(final Actuator executor, final Supplier<Future<T>> caseLine) {
        return Wait.branch(executor, caseLine);
    }

    public static <T> Case<T> branch(final boolean condition, final Supplier<Future<T>> caseLine) {
        return Wait.branch(condition, caseLine);
    }

    public static <T> Case<T> branch(final boolean condition, final Actuator executor, final Supplier<Future<T>> caseLine) {
        return Wait.branch(condition, executor, caseLine);
    }

    public static <T> Future<T> thenGeneric(final Consumer<Promise<T>> consumer) {
        return Wait.then(consumer);
    }

    public static <T> Future<T> thenGeneric(final Object result, final Promise<T> future, final Throwable ex) {
        return Wait.then(result, future, ex);
    }

    // ------ ZeroException out
    public static void outZero(final boolean condition, final Annal logger, final Class<? extends ZeroException> zeroClass, final Object... args) throws ZeroException {
        if (condition) {
            Announce.outZero(logger, zeroClass, args);
        }
    }

    // ------ ZeroException to RunZeroException out
    public static void outUp(final ZeroActuator actuator, final Annal logger) {
        Announce.toRun(logger, actuator);
    }

    // ------ RunZeroException out
    /* Old Style */
    public static void outUp(final boolean condition, final Annal logger, final Class<? extends ZeroRunException> upClass, final Object... args) {
        if (condition) {
            Announce.outUp(logger, upClass, args);
        }
    }

    /* New Style */
    public static void outUp(final boolean condition, final Class<? extends UpException> upClass, final Object... args) {
        if (condition) {
            Announce.outUp(upClass, args);
        }
    }

    /**
     * New structure for exception out ( RuntimeException )
     * UpException | WebException supported
     * --
     * outUp + outWeb
     *
     * @param condition The condition for throwing
     * @param clazz     The exception clazz here ( because all the exception class first argument type is Class<?>, it means
     *                  you can initialize logger inner this method instead of input
     * @param args      dynamic objects for exception
     */
    public static void out(final boolean condition, final Class<?> clazz, final Object... args) {
        if (condition) {
            Announce.out(clazz, args);
        }
    }

    // ------ WebException out
    /* Old Style */
    public static void outWeb(final boolean condition, final Annal logger, final Class<? extends WebException> webClass, final Object... args) {
        if (condition) {
            Announce.outWeb(logger, webClass, args);
        }
    }

    /* New Style */
    public static void outWeb(final boolean condition, final Class<? extends WebException> webClass, final Object... args) {
        if (condition) {
            Announce.outWeb(webClass, args);
        }
    }

    // ------ Jvm Safe

    public static void safeJvm(final JvmActuator actuator, final Annal logger) {
        Safer.jvmVoid(actuator, logger);
    }

    public static void safeJvm(final JvmActuator actuator) {
        Safer.jvmVoid(actuator, null);
    }

    public static <T> T safeJvm(final JvmSupplier<T> supplier, final Annal logger) {
        return Safer.jvmReturn(supplier, logger);
    }

    public static <T> T safeJvm(final JvmSupplier<T> supplier) {
        return Safer.jvmReturn(supplier, null);
    }

    public static <T> T getJvm(final JvmSupplier<T> supplier, final Object... input) {
        return Zero.getJvm(null, supplier, input);
    }

    public static <T> T getJvm(final T defaultValue, final JvmSupplier<T> supplier, final Object... input) {
        return Zero.getJvm(defaultValue, supplier, input);
    }

    // ------ Zero Safe
    public static <T> T getZero(final ZeroSupplier<T> supplier, final Annal logger) {
        return Safer.zeroReturn(supplier, logger);
    }

    public static void safeZero(final ZeroActuator actuator, final Annal logger) {
        Safer.zeroVoid(actuator, logger);
    }

    // ------ Null Safe
    public static void safeNull(final Actuator actuator, final Object... input) {
        Zero.exec(actuator, input);
    }

    public static <T> void safeNull(final Consumer<T> consumer, final T input) {
        Zero.exec(consumer, input);
    }

    public static <T> T getNull(final Supplier<T> supplier, final Object... input) {
        return Zero.get(null, supplier, input);
    }

    public static <T> T getNull(final T defaultValue, final Supplier<T> supplier, final Object... input) {
        return Zero.get(defaultValue, supplier, input);
    }

    public static <T> T getNull(final T defaultValue, final Supplier<T> supplier) {
        return Semi.execReturn(supplier, defaultValue);
    }

    public static <T> T getEmpty(final Supplier<T> supplier, final String... input) {
        return Zero.getEmpty(null, supplier, input);
    }

    public static <T> T getEmpty(final T defaultValue, final Supplier<T> supplier, final String... input) {
        return Zero.getEmpty(defaultValue, supplier, input);
    }

    // ------ Function Processing
    public static <T> T wrap(final RunSupplier<T> supplier, final T defaultValue) {
        return Wrapper.wrapper(supplier, defaultValue);
    }

    public static <T> Future<T> wrapAsync(final RunSupplier<Future<T>> supplier, final T defaultValue) {
        return Wrapper.wrapperAsync(supplier, defaultValue);
    }

    // ------ Semi Safe
    public static void safeSemi(final boolean condition, final Annal logger, final Actuator tSupplier, final Actuator fSupplier) {
        Semi.exec(condition, logger, tSupplier, fSupplier);
    }

    public static void safeSemi(final boolean condition, final Annal logger, final Actuator tSupplier) {
        Semi.exec(condition, logger, tSupplier, null);
    }

    public static void safeSemi(final boolean condition, final Actuator tSupplier) {
        Semi.exec(condition, null, tSupplier, null);
    }

    public static <T> T getSemi(final boolean condition, final Annal logger, final Supplier<T> tSupplier, final Supplier<T> fSupplier) {
        return Safer.zeroReturn(() -> Semi.execZero(condition, tSupplier::get, fSupplier::get), logger);
    }

    public static <T> T getSemi(final boolean condition, final Annal logger, final Supplier<T> tSupplier) {
        return Safer.zeroReturn(() -> Semi.execZero(condition, tSupplier::get, null), logger);
    }

    public static <T> T getSemi(final boolean condition, final ZeroSupplier<T> tSupplier, final ZeroSupplier<T> fSupplier) throws ZeroException {
        return Semi.execZero(condition, tSupplier, fSupplier);
    }

    // ------ Specification for JsonFormat
    public static <T> T outRun(final Supplier<T> supplier, final Class<? extends ZeroRunException> runCls, final Object... args) {
        return Deliver.execRun(supplier, runCls, args);
    }

    /*
     * Ensure method for some spec
     * 1) inLenEq:
     * 2) inLenMin:
     * To be sure arguments here
     */
    public static void inLenEq(final Class<?> clazz, final int expected, final Object... args) {
        Input.eqLength(clazz, expected, args);
    }

    public static void inLenMin(final Class<?> clazz, final int min, final Object... args) {
        Input.gtLength(clazz, min, args);
    }

    public static <T> void etJObject(final JsonObject data, final ZeroBiConsumer<T, String> fnIt) throws ZeroException {
        Et.execZero(data, fnIt);
    }

    public static <T> void etJArray(final JsonArray dataArray, final Class<T> clazz, final ZeroBiConsumer<T, Integer> fnIt) throws ZeroException {
        Et.execZero(dataArray, clazz, fnIt);
    }

    public static <T> void etJArray(final JsonArray dataArray, final ZeroBiConsumer<T, String> fnIt) throws ZeroException {
        Et.execZero(dataArray, fnIt);
    }

    // ------ Must throw out exception in these two methods
    public static void onRun(final Actuator actuator, final Annal logger) {
        Announce.shuntRun(actuator, logger);
    }

    public static void onZero(final ZeroActuator actuator, final Object... input) throws ZeroException {
        Zero.execZero(actuator, input);
    }

    public static <T> void onTest(final TestContext context, final Future<T> future, final Consumer<T> consumer) {
        Wait.testing(context, future, consumer);
    }

    // ------ Pool
    public static <K, V> V pool(final ConcurrentMap<K, V> pool, final K key, final Supplier<V> poolFn) {
        return Deliver.exec(pool, key, poolFn);
    }

    /*
     * Speicial function to pool by thread name instead of other key here, for multi-thread environment
     */
    public static <V> V poolThread(final ConcurrentMap<String, V> pool, final Supplier<V> poolFn) {
        return poolThread(pool, poolFn, null);
    }

    public static <V> V poolThread(final ConcurrentMap<String, V> pool, final Supplier<V> poolFn, final String root) {
        final String threadName = Thread.currentThread().getName();
        if (Ut.isNil(root)) {
            return Deliver.exec(pool, threadName, poolFn);
        } else {
            return Deliver.exec(pool, root + "/" + threadName, poolFn);
        }
    }
}
