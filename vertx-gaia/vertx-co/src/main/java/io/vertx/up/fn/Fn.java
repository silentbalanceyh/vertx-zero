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


    public static <T> Future<T> unbox(
        final Consumer<Promise<T>> consumer) {
        return Wait.then(consumer);
    }

    public static <T> Future<T> unbox(final Object result, final Promise<T> future, final Throwable ex) {
        return Wait.then(result, future, ex);
    }


    public static <T> Future<T> error(final Class<? extends WebException> clazz, final Object... args) {
        return War.thenError(clazz, args);
    }

    public static <T> Future<T> error(final Class<?> clazz, final String sigma, final Supplier<Future<T>> supplier) {
        return War.thenError(clazz, sigma, supplier);
    }

    // ---------------- Arrange Async Future ----------------------
    /*
     * This arrange part will replace `thenCombine?` method. Here are detail readme information:
     *
     * 1. Flag
     *    Here the flag identified the return value of internal ( Generic T )
     * -  A:        JsonArray
     * -  J:        JsonObject
     * -  T:        Generice <T>
     * -  M:        Map
     * -  L:        List
     *
     * 2. Prefix
     * - combine,   From element to container, this situation often happens as following
     *              1) The element of collection contains async operation.
     *              2) The result should be collection and major thread must wait for each element async operation finished.
     * - comic,     Expand element to multi, this situation often happens as following
     *              1) The element of collection is single type.
     *              2) The single type will generate multi elements into collection formed
     * - compress,  Compress collection to single, this situation often happens as following
     *              1) The collection has 2 layers and each element is also another collection.
     *              2) This kind of API will compress the collection into 1 layer ( single collection ).
     *
     * 3. The mark of data structure
     *
     *      o  -  Pure element type without any attached information.
     *            If there are more types, I'll use o1, o2, o3.
     *     [o] - 「Container」Collection type and the element type is o.
     *     (o) - 「Container」Async type and the element type is o.
     *     fx  -  Consumer Function
     */


    // -------------------------- compress -----------------------

    /*
     * Workflow:
     *
     * [
     *      ( [t,t] )           -->    ...
     *      ( [t,t,t,t] )       -->    ...   -->  ( [t,t,   t,t,t,t,   t] )
     *      ( [t] )             -->    ...
     * ]
     *
     */
    public static <T> Future<List<T>> compressL(
        final List<Future<List<T>>> futures) {
        return War.thenCombineArrayT(futures);
    }

    /*
     * This method is the same as `compressL` except the container type is JsonArray.
     */
    public static Future<JsonArray> compressA(
        final List<Future<JsonArray>> futures) {
        return War.thenCombineArray(futures);
    }

    /*
     * Workflow:
     * [
     *      ( [k=t,k=t,k=t] )   -->     ...
     *      ( [k=t] )           -->     ...     --> ( [k=t,k=t,k=t,   k=t,   k=t,k=t] )
     *      ( [k=t,k=t] )       -->     ...
     * ]
     */
    public static <T> Future<ConcurrentMap<String, T>> compressM(
        final List<Future<ConcurrentMap<String, T>>> futures,
        final BinaryOperator<T> binaryOperator) {
        return War.thenCompress(futures, binaryOperator);
    }

    /*
     * This method is the same as `compressM` except the container type is JsonArray.
     */
    public static Future<ConcurrentMap<String, JsonArray>> compressM(
        final List<Future<ConcurrentMap<String, JsonArray>>> futures) {
        return War.thenCompress(futures, (original, latest) -> original.addAll(latest));
    }


    // -------------------------- combine -----------------------

    /*
     * Workflow:
     * [                                                   t + t  =>  tn         [
     *      t       -->     fx      -->    (t)     -->          fx      -->         tn
     *      t       -->     fx      -->    (t)     -->          fx      -->         tn
     *      t       -->     fx      -->    (t)     -->          fx      -->         tn
     * ]                                                                         ]
     */
    public static Future<JsonArray> combineA(
        final Future<JsonArray> source,
        final Function<JsonObject, Future<JsonObject>> generateFun,
        final BinaryOperator<JsonObject> operatorFun
    ) {
        return War.thenCombine(source, generateFun, operatorFun);
    }

    /*
     * Workflow:
     * [
     *      t       -->     fx      -->    (t)
     *      t       -->     fx      -->    (t)     -->     ( [t,t,t] )
     *      t       -->     fx      -->    (t)
     * ]
     */
    public static Future<JsonArray> combineA(
        final JsonArray input,
        final Function<JsonObject, Future<JsonObject>> function) {
        final List<Future<JsonObject>> futures = new ArrayList<>();
        Ut.itJArray(input).map(function).forEach(futures::add);
        return War.thenCombine(futures);
    }

    /*
     * Workflow:
     * [
     *      (t)
     *      (t)         -->      ( [t,t,t] )
     *      (t)
     * ]
     */
    public static Future<JsonArray> combineA(
        final List<Future<JsonObject>> futures) {
        return War.thenCombine(futures);
    }

    /*
     * Workflow:
     * [                                             (
     *      (t)                                          0 = t
     *      (t)         -->      (t)           =         1 = t
     *      (t)                                          2 = t
     * ]                                             )
     */
    public static <K, T> Future<ConcurrentMap<K, T>> combineM(
        final ConcurrentMap<K, Future<T>> futureMap) {
        return War.thenCombine(futureMap);
    }

    /*
     * Workflow:
     * [                                             (
     *      (t)                                          0 = t
     *      (t)         -->      (t)           =         1 = t
     *      (t)                                          2 = t
     * ]                                             )
     */
    public static Future<JsonObject> combineJ(
        final Future<JsonObject>... futures) {
        return War.thenCombine(futures);
    }

    /*
     * Workflow:
     *
     *                       t  =>  ([t,t,t])                                       t + t  =>  tn
     *      t         -->         fx          -->      ( [t,t,t] )          -->         fx          -->     (tn)
     *
     */
    public static Future<JsonObject> combineJ(
        final JsonObject source,
        final Function<JsonObject, List<Future>> generateFun,
        final BiConsumer<JsonObject, JsonObject>... operatorFun) {
        return War.thenCombine(Future.succeededFuture(source), generateFun, operatorFun);
    }

    /*
     * Workflow:
     *
     *                       t  =>  ([t,t,t])                                       t + t  =>  tn
     *      (t)         -->         fx          -->      ( [t,t,t] )        -->         fx          -->     (tn)
     *
     */
    public static Future<JsonObject> combineJ(
        final Future<JsonObject> source,
        final Function<JsonObject, List<Future>> generateFun,
        final BiConsumer<JsonObject, JsonObject>... operatorFun
    ) {
        return War.thenCombine(source, generateFun, operatorFun);
    }


    /*
     * Workflow:
     *
     *                                  t1 + t2 => (t3)
     *      fx,(t1)             -->         fx          -->     (t3)
     *              fx,(t2)     -->
     */
    public static <F, S, T> Future<T> combineT(
        final Supplier<Future<F>> futureF,
        final Supplier<Future<S>> futureS,
        final BiFunction<F, S, Future<T>> consumer) {
        return War.thenCombine(futureF, futureS, consumer);
    }

    /*
     * Workflow:
     *
     *                                  t1 + t2 => (t3)
     *      fx,(t1)             -->         fx          -->     (t3)
     *        t1 -->  fx,(t2)   -->
     */
    public static <F, S, T> Future<T> thenCombine(
        final Supplier<Future<F>> futureF,
        final Function<F, Future<S>> futureS,
        final BiFunction<F, S, Future<T>> consumer) {
        return War.thenCombine(futureF, futureS, consumer);
    }

    /*
     * Workflow:
     *
     *                         t1 + t2 => (t3)
     *      (t1)        -->         fx          -->     (t3)
     *      (t2)        -->
     */
    public static <F, S, T> Future<T> combineT(
        final Future<F> futureF,
        final Future<S> futureS,
        final BiFunction<F, S, Future<T>> consumer) {
        return War.thenCombine(() -> futureF, () -> futureS, consumer);
    }

    /*
     * Workflow:
     *
     * [
     *      (t)
     *      (t)     -->     ( [t,t,t,t,t] )
     *      (t)
     * ]
     */
    public static <T> Future<List<T>> combineT(
        final List<Future<T>> futures) {
        return War.thenCombineT(futures);
    }

    /*
     * Workflow:
     *
     * [
     *      t1  -->     fx      (t2)
     *      t1  -->     fx      (t2)            --> ( [t2,t2,t2,t2,t2] )
     *      t1  -->     fx      (t2)
     * ]
     */
    public static <I, T> Future<List<T>> combineT(
        final List<I> source, final Function<I, Future<T>> consumer) {
        final List<Future<T>> futures = new ArrayList<>();
        Ut.itList(source).map(consumer).forEach(futures::add);
        return War.thenCombineT(futures);
    }

    // -------------------------- comic -----------------------

    /*
     * Workflow:
     *
     * [
     *      t   -->     fx    --> ( [...] )
     *      t   -->     fx    --> ( [...] )       --> ( [... ... ... ... ...] )
     *      t   -->     fx    --> ( [...] )
     * ]
     *
     * The o = Class<T>
     */
    public static <T> Future<JsonArray> comicA(
        final JsonArray source,
        final Class<T> clazz,
        final Function<T, Future<JsonArray>> consumer) {
        final List<Future<JsonArray>> futures = new ArrayList<>();
        Ut.itJArray(source, clazz, (item, index) -> futures.add(consumer.apply(item)));
        return War.thenCombineArray(futures);
    }

    /*
     * The method is the same as `comicA` except the element type is JsonObject
     *
     * The o = JsonObject
     */
    public static Future<JsonArray> comicA(
        final JsonArray source,
        final Function<JsonObject, Future<JsonArray>> consumer) {
        return comicA(source, JsonObject.class, consumer);
    }
}
