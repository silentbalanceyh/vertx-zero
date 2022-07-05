package io.vertx.up.fn;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.UpException;
import io.vertx.up.exception.WebException;
import io.vertx.up.exception.ZeroException;
import io.vertx.up.exception.ZeroRunException;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.function.*;

/**
 * Unique interface to call function
 */
@SuppressWarnings("all")
public final class Fn {
    private Fn() {
    }

    // ------ ZeroException out
    public static void outZero(final boolean condition, final Annal logger, final Class<? extends ZeroException> zeroClass, final Object... args) throws ZeroException {
        if (condition) {
            Warning.outZero(logger, zeroClass, args);
        }
    }

    // ------ ZeroException to RunZeroException out
    public static void outUp(final ZeroActuator actuator, final Annal logger) {
        Wait.wrapper(logger, actuator);
    }

    // ------ RunZeroException out
    /* Old Style */
    public static void outUp(final boolean condition, final Annal logger, final Class<? extends ZeroRunException> upClass, final Object... args) {
        if (condition) {
            Warning.outUp(logger, upClass, args);
        }
    }

    /* New Style */
    public static void outUp(final boolean condition, final Class<? extends UpException> upClass, final Object... args) {
        if (condition) {
            Warning.outUp(upClass, args);
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
            Warning.out(clazz, args);
        }
    }

    // ------ WebException out
    /* Old Style */
    public static void outWeb(final boolean condition, final Annal logger, final Class<? extends WebException> webClass, final Object... args) {
        if (condition) {
            Warning.outWeb(logger, webClass, args);
        }
    }

    /* New Style */
    public static void outWeb(final boolean condition, final Class<? extends WebException> webClass, final Object... args) {
        if (condition) {
            Warning.outWeb(webClass, args);
        }
    }

    // ------ Jvm Safe

    public static void safeJvm(final JvmActuator actuator, final Annal logger) {
        Wall.jvmVoid(actuator, logger);
    }

    public static void safeJvm(final JvmActuator actuator) {
        Wall.jvmVoid(actuator, null);
    }

    public static <T> T safeJvm(final JvmSupplier<T> supplier, final Annal logger) {
        return Wall.jvmReturn(supplier, logger);
    }

    public static <T> T safeJvm(final JvmSupplier<T> supplier) {
        return Wall.jvmReturn(supplier, null);
    }

    public static <T> T getJvm(final JvmSupplier<T> supplier, final Object... input) {
        return Zero.getJvm(null, supplier, input);
    }

    public static <T> T getJvm(final T defaultValue, final JvmSupplier<T> supplier, final Object... input) {
        return Zero.getJvm(defaultValue, supplier, input);
    }

    // ------ Zero Safe
    public static <T> T getZero(final ZeroSupplier<T> supplier, final Annal logger) {
        return Wall.zeroReturn(supplier, logger);
    }

    public static void safeZero(final ZeroActuator actuator, final Annal logger) {
        Wall.zeroVoid(actuator, logger);
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
        return Wall.execReturn(supplier, defaultValue);
    }

    public static <T> T getEmpty(final Supplier<T> supplier, final String... input) {
        return Zero.getEmpty(null, supplier, input);
    }

    public static <T> T getEmpty(final T defaultValue, final Supplier<T> supplier, final String... input) {
        return Zero.getEmpty(defaultValue, supplier, input);
    }

    // ------ Function Processing
    public static <T> T wrap(final RunSupplier<T> supplier, final T defaultValue) {
        return Wait.wrapper(supplier, defaultValue);
    }

    public static <T> Future<T> wrapAsync(final RunSupplier<Future<T>> supplier, final T defaultValue) {
        return Wait.wrapperAsync(supplier, defaultValue);
    }

    // ------ Semi Safe
    public static void safeSemi(final boolean condition, final Annal logger, final Actuator tSupplier, final Actuator fSupplier) {
        Wall.exec(condition, logger, tSupplier, fSupplier);
    }

    public static void safeSemi(final boolean condition, final Annal logger, final Actuator tSupplier) {
        Wall.exec(condition, logger, tSupplier, null);
    }

    public static void safeSemi(final boolean condition, final Actuator tSupplier) {
        Wall.exec(condition, null, tSupplier, null);
    }

    public static <T> T getSemi(final boolean condition, final Annal logger, final Supplier<T> tSupplier, final Supplier<T> fSupplier) {
        return Wall.zeroReturn(() -> Wall.execZero(condition, tSupplier::get, fSupplier::get), logger);
    }

    public static <T> T getSemi(final boolean condition, final Annal logger, final Supplier<T> tSupplier) {
        return Wall.zeroReturn(() -> Wall.execZero(condition, tSupplier::get, null), logger);
    }

    public static <T> T getSemi(final boolean condition, final ZeroSupplier<T> tSupplier, final ZeroSupplier<T> fSupplier) throws ZeroException {
        return Wall.execZero(condition, tSupplier, fSupplier);
    }

    // ------ Specification for JsonFormat
    public static <T> T outRun(final Supplier<T> supplier, final Class<? extends ZeroRunException> runCls, final Object... args) {
        return Warning.execRun(supplier, runCls, args);
    }

    /*
     * Ensure method for some spec
     * 1) verifyLenEq:
     * 2) verifyLenMin:
     * To be sure arguments here
     */
    public static void verifyLenEq(final Class<?> clazz, final int expected, final Object... args) {
        Wall.verifyEqLength(clazz, expected, args);
    }

    public static void verifyLenMin(final Class<?> clazz, final int min, final Object... args) {
        Wall.verifyEtLength(clazz, min, args);
    }

    public static <T> void verifyJObject(final JsonObject data, final ZeroBiConsumer<T, String> fnIt) throws ZeroException {
        Wall.execZero(data, fnIt);
    }

    public static <T> void verifyJArray(final JsonArray dataArray, final Class<T> clazz, final ZeroBiConsumer<T, Integer> fnIt) throws ZeroException {
        Wall.execZero(dataArray, clazz, fnIt);
    }

    public static <T> void verifyJArray(final JsonArray dataArray, final ZeroBiConsumer<T, String> fnIt) throws ZeroException {
        Wall.execZero(dataArray, fnIt);
    }

    // ------ Must throw out exception in these two methods
    public static void safeRun(final Actuator actuator, final Annal logger) {
        Warning.outRun(actuator, logger);
    }

    public static void safeZero(final ZeroActuator actuator, final Object... input) throws ZeroException {
        Zero.execZero(actuator, input);
    }

    // ------ Manage
    public static <T> Future<T> passion(final T input, final List<Function<T, Future<T>>> executors) {
        return Wide.passion(input, executors);
    }

    public static <T> Future<T> passion(final T input, final Function<T, Future<T>>... executors) {
        return Wide.passion(input, Arrays.asList(executors));
    }

    public static <T> Future<T> parallel(final T input, final Set<Function<T, Future<T>>> executors) {
        return Wide.parallel(input, executors);
    }

    public static <T> Future<T> parallel(final T input, final List<Function<T, Future<T>>> executors) {
        return Wide.parallel(input, new HashSet<>(executors));
    }

    public static <T> Future<T> parallel(final T input, final Function<T, Future<T>>... executors) {
        return Wide.parallel(input, new HashSet<>(Arrays.asList(executors)));
    }

    // ------ Pool
    public static <K, V> V pool(final ConcurrentMap<K, V> pool, final K key, final Supplier<V> poolFn) {
        return Wall.execPool(pool, key, poolFn);
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
            return Wall.execPool(pool, threadName, poolFn);
        } else {
            return Wall.execPool(pool, root + "/" + threadName, poolFn);
        }
    }

    // ------- War ( New Version )


    public static <T> Future<T> thenUnbox(
        final Consumer<Promise<T>> consumer) {
        return Wait.then(consumer);
    }

    public static <T> Future<T> thenUnbox(final Object result, final Promise<T> future, final Throwable ex) {
        return Wait.then(result, future, ex);
    }


    public static <T> Future<List<T>> thenCombineArrayT(
        final List<Future<List<T>>> futures) {
        return War.thenCombineArrayT(futures);
    }

    public static Future<JsonArray> thenCombine(
        final Future<JsonArray> source,
        final Function<JsonObject, Future<JsonObject>> generateFun,
        final BinaryOperator<JsonObject> operatorFun
    ) {
        return War.thenCombine(source, generateFun, operatorFun);
    }

    public static Future<JsonObject> thenCombine(
        final Future<JsonObject>... futures) {
        return War.thenCombine(futures);
    }

    public static <K, T> Future<ConcurrentMap<K, T>> thenCombine(
        final ConcurrentMap<K, Future<T>> futureMap) {
        return War.thenCombine(futureMap);
    }

    public static Future<JsonArray> thenCombine(
        final JsonArray input,
        final Function<JsonObject, Future<JsonObject>> function) {
        final List<Future<JsonObject>> futures = new ArrayList<>();
        Ut.itJArray(input).map(function).forEach(futures::add);
        return Fn.thenCombine(futures);
    }

    public static Future<JsonArray> thenCombine(
        final List<Future<JsonObject>> futures) {
        return War.thenCombine(futures);
    }

    public static <F, S, T> Future<T> thenCombine(
        final Supplier<Future<F>> futureF,
        final Supplier<Future<S>> futureS,
        final BiFunction<F, S, Future<T>> consumer) {
        return War.thenCombine(futureF, futureS, consumer);
    }

    public static <F, S, T> Future<T> thenCombine(
        final Future<F> futureF,
        final Future<S> futureS,
        final BiFunction<F, S, Future<T>> consumer) {
        return War.thenCombine(() -> futureF, () -> futureS, consumer);
    }

    // ----- thenCombine
    public static Future<JsonObject> thenCombine(
        final JsonObject source,
        final Function<JsonObject, List<Future>> generateFun,
        final BiConsumer<JsonObject, JsonObject>... operatorFun) {
        return War.thenCombine(Future.succeededFuture(source), generateFun, operatorFun);
    }

    public static Future<JsonObject> thenCombine(
        final Future<JsonObject> source,
        final Function<JsonObject, List<Future>> generateFun,
        final BiConsumer<JsonObject, JsonObject>... operatorFun
    ) {
        return War.thenCombine(source, generateFun, operatorFun);
    }

    // ----- thenCombineT

    public static <T> Future<List<T>> thenCombineT(
        final List<Future<T>> futures) {
        return War.thenCombineT(futures);
    }

    public static <I, O> Future<List<O>> thenCombineT(
        final List<I> source, final Function<I, Future<O>> consumer) {
        final List<Future<O>> futures = new ArrayList<>();
        Ut.itList(source).map(consumer).forEach(futures::add);
        return War.thenCombineT(futures);
    }

    // ----- thenCombineArray
    public static Future<JsonArray> thenCombineArray(
        final List<Future<JsonArray>> futures) {
        return War.thenCombineArray(futures);
    }

    public static <T> Future<JsonArray> thenCombineArray(
        final JsonArray source,
        final Class<T> clazz,
        final Function<T, Future<JsonArray>> consumer) {
        final List<Future<JsonArray>> futures = new ArrayList<>();
        Ut.itJArray(source, clazz, (item, index) -> futures.add(consumer.apply(item)));
        return War.thenCombineArray(futures);
    }

    public static Future<JsonArray> thenCombineArray(
        final JsonArray source,
        final Function<JsonObject, Future<JsonArray>> consumer) {
        return thenCombineArray(source, JsonObject.class, consumer);
    }

    // ----- thenCompress
    /*
     * List<Future<Map<String,T>>> futures ->
     *      Future<Map<String,T>>
     * Exchange data by key here.
     *      The binary operator should ( T, T ) -> T
     */
    public static <T> Future<ConcurrentMap<String, T>> thenCompress(
        final List<Future<ConcurrentMap<String, T>>> futures,
        final BinaryOperator<T> binaryOperator) {
        return War.thenCompress(futures, binaryOperator);
    }

    public static Future<ConcurrentMap<String, JsonArray>> thenCompress(
        final List<Future<ConcurrentMap<String, JsonArray>>> futures) {
        return thenCompress(futures, (original, latest) -> original.addAll(latest));
    }

    public static <T> Future<T> thenError(final Class<? extends WebException> clazz, final Object... args) {
        return War.thenError(clazz, args);
    }

    public static <T> Future<T> thenError(final Class<?> clazz, final String sigma, final Supplier<Future<T>> supplier) {
        return War.thenError(clazz, sigma, supplier);
    }

}
