package io.vertx.up.fn;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
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

    public static <T> T orJvm(final JvmSupplier<T> supplier, final Object... input) {
        return Zero.getJvm(null, supplier, input);
    }

    public static <T> T orJvm(final T defaultValue, final JvmSupplier<T> supplier, final Object... input) {
        return Zero.getJvm(defaultValue, supplier, input);
    }

    // ------ Zero Safe
    public static <T> T orZero(final ZeroSupplier<T> supplier, final Annal logger) {
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

    /*
     * 修改原 get 前缀为 or，代表有可能得情况，这部分API改动量巨大，且和if可能会有些许重复
     * 重复部分暂时先维持原始信息，等之后合并
     */
    public static <T> T orNull(final Supplier<T> supplier, final Object... input) {
        return Zero.get(null, supplier, input);
    }

    public static <T> T orNull(final T defaultValue, final Supplier<T> supplier, final Object... input) {
        return Zero.get(defaultValue, supplier, input);
    }

    public static <T> T orNull(final T defaultValue, final Supplier<T> supplier) {
        return Wall.execReturn(supplier, defaultValue);
    }

    public static <T> T orEmpty(final Supplier<T> supplier, final String... input) {
        return Zero.getEmpty(null, supplier, input);
    }

    public static <T> T orEmpty(final T defaultValue, final Supplier<T> supplier, final String... input) {
        return Zero.getEmpty(defaultValue, supplier, input);
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

    public static <T> T orSemi(final boolean condition, final Annal logger, final Supplier<T> tSupplier, final Supplier<T> fSupplier) {
        return Wall.zeroReturn(() -> Wall.execZero(condition, tSupplier::get, fSupplier::get), logger);
    }

    public static <T> T orSemi(final boolean condition, final Annal logger, final Supplier<T> tSupplier) {
        return Wall.zeroReturn(() -> Wall.execZero(condition, tSupplier::get, null), logger);
    }

    public static <T> T orSemi(final boolean condition, final ZeroSupplier<T> tSupplier, final ZeroSupplier<T> fSupplier) throws ZeroException {
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
     * 「2 Dim」
     * The input is matrix, this method will compress the 2 dim matrix to 1 dim, the code logical is:
     *
     * 1. Process each element ( Future<List<T>> ) async operation to get the result ( List<T> ).
     * 2. Combine the result   ( List<List<T>> )  =>  ( List<T> ) only.
     * 3. The final data structure is:  [[]] => []
     */
    public static <T> Future<List<T>> compressL(
        final List<Future<List<T>>> futures) {
        return War.compressL(futures);
    }

    /*
     * 「2 Dim」
     * This method is the same as `compressL` except the container type is JsonArray.
     */
    public static Future<JsonArray> compressA(
        final List<Future<JsonArray>> futures) {
        return War.comicA(futures);
    }

    /*
     * Workflow:
     * [
     *      ( [k=t,k=t,k=t] )   -->     ...
     *      ( [k=t] )           -->     ...     --> ( [k=t,k=t,k=t,   k=t,   k=t,k=t] )
     *      ( [k=t,k=t] )       -->     ...
     * ]
     *
     * 「2 Dim」
     * This input is matrix, the method will comrpess the 2 dim matrix to 1 dim, but the input source
     * 1st dim and 2dim data structure are different. 1st dim is hash map and 2nd dim is list, the code
     * logical is:
     *
     * 1. Process each element ( Future<Map<String,T>> ) async aoperation to get the result ( Map<String, T> ).
     * 2. Combine the result   ( List<Map<String,T>> ) => ( Map<String, T> ) only.
     * 3. The final data structure is: [[k=t]] => [k=t]
     */
    public static <T> Future<ConcurrentMap<String, T>> compressM(
        final List<Future<ConcurrentMap<String, T>>> futures,
        final BinaryOperator<T> binaryOperator) {
        return War.compressM(futures, binaryOperator);
    }

    /*
     * 「2 Dim」
     * This method is the same as `compressM` except the container type is JsonArray.
     */
    public static Future<ConcurrentMap<String, JsonArray>> compressM(
        final List<Future<ConcurrentMap<String, JsonArray>>> futures) {
        return War.compressM(futures, (original, latest) -> original.addAll(latest));
    }


    // -------------------------- combine -----------------------

    /*
     * Workflow:
     * [                                                   t + t  =>  tn         [
     *      t       -->     fx      -->    (t)     -->          fx      -->         tn
     *      t       -->     fx      -->    (t)     -->          fx      -->         tn
     *      t       -->     fx      -->    (t)     -->          fx      -->         tn
     * ]
     *
     * 「1 Dim」
     * This input is collection ( JsonArray ) and this is common combine operation as:
     *
     * 1. Process the single async operation to get the result ( JsonArray ).
     * 2. ( t = JsonObject ) iterate the collection to get each JsonObject element.
     * 3. 「F」Execute the `generateFun` ( t => (t) ) to process each JsonObject element to get another one.
     * 4. 「F」Combine the element result with input element ( t + t => tn ).
     *     operatorFun = (t,t)
     *        - 1st t is the element of input collection ( JsonArray )
     *        - 2nd t is the processed result based on the 1st t with `generateFun`
     * 5. Finally, combine all the element result from `operatorFun` into new collection ( JsonArray )
     */
    public static Future<JsonArray> combineA(
        final Future<JsonArray> source,
        final Function<JsonObject, Future<JsonObject>> generateFun,
        final BinaryOperator<JsonObject> operatorFun
    ) {
        return War.combineA(source, generateFun, operatorFun);
    }

    /*
     * Workflow:
     * [
     *      t       -->     fx      -->    (t)
     *      t       -->     fx      -->    (t)     -->     ( [t,t,t] )
     *      t       -->     fx      -->    (t)
     * ]
     *
     * 「1 Dim」
     * The input is collection ( JsonArray ) and this is common combine operation as:
     *
     * 1. ( t = JsonObject ) iterate the collection ( JsonArray ) to get each JsonObject element.
     * 2. 「F」Execute the `generateFun` ( t => (t) ) to process each JsonObject element to get another one.
     * 3. Combine all the element result from `generateFun` into new collection ( JsonArray )
     */
    public static Future<JsonArray> combineA(
        final JsonArray input,
        final Function<JsonObject, Future<JsonObject>> generateFun) {
        final List<Future<JsonObject>> futures = new ArrayList<>();
        Ut.itJArray(input).map(generateFun).forEach(futures::add);
        return War.combineA(futures);
    }

    /*
     * Workflow:
     * [
     *      (t)
     *      (t)         -->      ( [t,t,t] )
     *      (t)
     * ]
     *
     * 「1 Dim」
     * The input is collection ( JsonArray ) and this is common combine operation as:
     *
     * **: Combine all the element result into new collection ( JsonArray )
     */
    public static Future<JsonArray> combineA(
        final List<Future<JsonObject>> futures) {
        return War.combineA(futures);
    }

    /*
     * Workflow:
     * [                                               (
     *      k=(t)                                          k=t,
     *      k=(t)         -->      (t)           =         k=t,
     *      k=(t)                                          k=t,
     * ]                                               )
     *
     * 「1 Dim」
     * The input source is map collection and this is common combine operation as:
     *
     * 1. Iterate the has map to extract each entry ( key = async operation ).
     * 2. Process each k = async operation to get each element result t.
     * 3. Finally, combine all `k = t` into single hash map.
     */
    public static <K, T> Future<ConcurrentMap<K, T>> combineM(
        final ConcurrentMap<K, Future<T>> futureMap) {
        return War.combineM(futureMap);
    }

    /*
     * Workflow:
     * [                                             (
     *      (t)                                          0 = t
     *      (t)         -->      (t)           =         1 = t
     *      (t)                                          2 = t
     * ]                                             )
     *
     * 「1 Dim」
     * The input source is dynamic array collection, it's common combine operation as:
     *
     * 1. Process each element (t), execute async operation to get element result t.
     * 2. Combine result t based on index of array.
     * 3. The final data structure is `i = t` into single hash map, here the i is input index of array.
     */
    public static Future<JsonObject> combineJ(
        final Future<JsonObject>... futures) {
        return War.combineJ(futures);
    }


    /*
     * Workflow:
     *
     *                                  t1 + t2 => (t3)
     *      fx,(t1)             -->         fx          -->     (t3)
     *              fx,(t2)     -->
     *
     * 「1 Dim」
     * Here are two critical inputs, it's common combine operation as:
     *
     * 1. Process first async operation `futureF` to get the first result t1.
     * 2. After the first async operation, process second async operation `futureS` to get the second result t2.
     * 3. Combine the results :  t1 + t2 => (t3)
     * 4. Finally return async operation result: t3.
     */
    public static <F, S, T> Future<T> combineT(
        final Supplier<Future<F>> futureF,
        final Supplier<Future<S>> futureS,
        final BiFunction<F, S, Future<T>> consumer) {
        return War.combineT(futureF, futureS, consumer);
    }

    /*
     * Workflow:
     *
     *                         t1 + t2 => (t3)
     *      (t1)        -->         fx          -->     (t3)
     *      (t2)        -->
     *
     * 「1 Dim」
     * This method is the same as above except the input data structure.
     */
    public static <F, S, T> Future<T> combineT(
        final Future<F> futureF,
        final Future<S> futureS,
        final BiFunction<F, S, Future<T>> consumer) {
        return War.combineT(() -> futureF, () -> futureS, consumer);
    }

    /*
     * Workflow:
     *
     *                                  t1 + t2 => (t3)
     *      fx,(t1)             -->         fx          -->     (t3)
     *        t1 -->  fx,(t2)   -->
     *
     * 「1 Dim」
     * Here are two critical inputs, it's common combine operation as:
     *
     * 1. Process first async operation `futureF` to get the first result t1.
     * 2. After the first async operation, process second async operation `futureS` to get the second result t2,
     *    this step is different from above api, when you process `futureS`, the t1 is input and it means that
     *    the t2 operation depend on `futureF` result.
     * 3. Combine the results :  t1 + t2 => (t3)
     * 4. Finally return async operation result: t3.
     */
    public static <F, S, T> Future<T> combineT(
        final Supplier<Future<F>> futureF,
        final Function<F, Future<S>> futureS,
        final BiFunction<F, S, Future<T>> consumer) {
        return War.combineT(futureF, futureS, consumer);
    }

    /*
     * Workflow:
     *
     * [
     *      (t)
     *      (t)     -->     ( [t,t,t,t,t] )
     *      (t)
     * ]
     *
     * 「1 Dim」
     * Here are common combine operation as:
     *
     * **: Combine all the element result into new collection ( List<T> )
     */
    public static <T> Future<List<T>> combineT(
        final List<Future<T>> futures) {
        return War.combineT(futures);
    }

    public static <T> Future<List<T>> combineT(
        final CompositeFuture res) {
        return War.combineT(res);
    }

    public static <T> Future<Set<T>> combineT(
        final Set<Future<T>> futures) {
        return War.combineT(futures);
    }

    public static <T> Future<Boolean> combineB(
        final List<Future<T>> futures) {
        return War.combineT(futures)
            .compose(nil -> Future.succeededFuture(Boolean.TRUE));
    }

    public static <T> Future<Boolean> combineB(
        final Set<Future<T>> futures) {
        return War.combineT(futures)
            .compose(nil -> Future.succeededFuture(Boolean.TRUE));
    }

    /*
     * Workflow:
     *
     * [
     *      t1  -->     fx      (t2)
     *      t1  -->     fx      (t2)            --> ( [t2,t2,t2,t2,t2] )
     *      t1  -->     fx      (t2)
     * ]
     *
     * 「1 Dim」
     * Here are common combine operation as:
     *
     * 1. Iterate the collection ( List<T> ) to get each element t1
     * 2. 「F」Execute `generateFun` on each element: t1 -> (t2)
     * 3. Combine all the async result to List<T>
     */
    public static <I, T> Future<List<T>> combineT(
        final List<I> source, final Function<I, Future<T>> generateFun) {
        final List<Future<T>> futures = new ArrayList<>();
        Ut.itList(source).map(generateFun).forEach(futures::add);
        return War.combineT(futures);
    }

    public static <I, T> Future<Boolean> combineB(
        final List<I> source, final Function<I, Future<T>> generateFun) {
        final List<Future<T>> futures = new ArrayList<>();
        Ut.itList(source).map(generateFun).forEach(futures::add);
        return War.combineT(futures).compose(nil -> Future.succeededFuture(Boolean.TRUE));
    }

    public static <I, T> Future<Set<T>> combineT(
        final Set<I> source, final Function<I, Future<T>> generateFun) {
        final Set<Future<T>> futures = new HashSet<>();
        Ut.itSet(source).map(generateFun).forEach(futures::add);
        return War.combineT(futures);
    }

    public static <I, T> Future<Boolean> combineB(
        final Set<I> source, final Function<I, Future<T>> generateFun) {
        final Set<Future<T>> futures = new HashSet<>();
        Ut.itSet(source).map(generateFun).forEach(futures::add);
        return War.combineT(futures).compose(nil -> Future.succeededFuture(Boolean.TRUE));
    }

    /*
     * Workflow:
     *
     * ( [
     *      t1  -->     fx      (t2)
     *      t1  -->     fx      (t2)            --> ( [t2,t2,t2,t2,t2] )
     *      t1  -->     fx      (t2)
     * ] )
     *
     * 「1 Dim」
     *
     * The method is the same as above except the input data structure.
     */
    public static <I, T> Future<List<T>> combineT(
        final Future<List<I>> futureS, final Function<I, Future<T>> generateFun) {
        return futureS.compose(source -> combineT(source, generateFun));
    }

    // -------------------------- comic -----------------------


    /*
     * Workflow:
     *
     *                       t  =>  ([t,t,t])                                       t + t  =>  tn
     *      t         -->         fx          -->      ( [t,t,t] )          -->         fx          -->     (tn)
     *
     * 「1 Dim」
     * The input data is element ( JsonObject ), it's changed version of common combine as:
     *
     * 1. 「F」Execute the `generateFun` based on input JsonObject to get collection, here each element of
     *    collection is async wrapped
     * 2. Process async operation on each element of generated collection get the result of each
     * 3. Here are zip combining the result such as following:
     *      t          -->       t1 ( index = 0 )     -->       operatorFun ( index = 0 )  -->  t1
     *                           t2 ( index = 1 )     -->       operatorFun ( index = 1 )  -->  t2
     *                           ...                  -->       ...                        -->  ...
     * 4. Finally, return the `input` data ( Just like fluent style )
     *
     * This api is different from other api, the operatorFun has two version:
     *
     * 1) If `source` modified, you can execute operatorFun to impact the input data ( Modify reference )
     * 2) If `source` unchanged, you can do other operation inner each operatorFun and do not impact
     *    the input data ( Keep reference unchange )
     */
    public static Future<JsonObject> comicJ(
        final JsonObject source,
        final Function<JsonObject, List<Future>> generateFun,
        final BiConsumer<JsonObject, JsonObject>... operatorFun) {
        return War.combineJ(Future.succeededFuture(source), generateFun, operatorFun);
    }

    /*
     * Workflow:
     *
     *                       t  =>  ([t,t,t])                                       t + t  =>  tn
     *      (t)         -->         fx          -->      ( [t,t,t] )        -->         fx          -->     (tn)
     *
     *
     * 「1 Dim」
     * This method is the same as `comicJ`, the input data structure is different.
     */
    public static Future<JsonObject> comicJ(
        final Future<JsonObject> source,
        final Function<JsonObject, List<Future>> generateFun,
        final BiConsumer<JsonObject, JsonObject>... operatorFun
    ) {
        return War.combineJ(source, generateFun, operatorFun);
    }

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
     * 「1 Dim」
     * This method is common comic, the input data structure is collection ( JsonArray ) as:
     *
     * 1. Filter the `source` based on `clazz` type, other type will be ignored.
     * 2. 「F」Process each element t1 ( selected Class<T> ) to generate the new collection.
     * 3. Until this step the final result set is matrix: [[]]
     * 4. Combine the matrix from 2 dim to 1 dim: [[]] => []
     */
    public static <T> Future<JsonArray> comicA(
        final JsonArray source,
        final Class<T> clazz,
        final Function<T, Future<JsonArray>> generateFun) {
        final List<Future<JsonArray>> futures = new ArrayList<>();
        Ut.itJArray(source, clazz, (item, index) -> futures.add(generateFun.apply(item)));
        return War.comicA(futures);
    }

    /*
     * The method is the same as `comicA` except the element type is JsonObject
     *
     * The o = JsonObject
     */
    public static Future<JsonArray> comicA(
        final JsonArray source,
        final Function<JsonObject, Future<JsonArray>> generateFun) {
        return comicA(source, JsonObject.class, generateFun);
    }

    /*
     * The workflow processing here.
     *
     * 1. Extract object reference from JsonObject 'input' by `field`
     * 2. Check the code flow by the object reference type 'JsonArray' or 'JsonObject'
     * 3. Execute different function that input
     * -- JsonObject: itemFnJ
     * -- JsonArray:  itemFnA
     * 4. Put the result of each function back into input
     */
    public static <J, A> Future<JsonObject> choiceJ(final JsonObject input, final String field,
                                                    final Function<JsonObject, Future<J>> itemFnJ,
                                                    final Function<JsonArray, Future<A>> itemFnA) {
        return War.choiceJ(input, field, itemFnJ, itemFnA);
    }


    // ------ Function Processing for Output
    /*
     * 1）wrap / wrapAsync：执行封装
     * 2）wrapJ：最终返回的是 Future<JsonObject>,  JS = Json Sync，同步返回
     * 3）wrapB: 最终返回的是 Future<Boolean>
     * 4）wrapTo / wrapOn: 最终返回的是 Function，且返回值是 Future<JsonObject>
     * --- 只有异步，没有同步
     *
     * To:  T -> mount( field = T )
     * On:  T -> T ( field = mount )
     */
    public static <T> T wrap(final RunSupplier<T> supplier, final T defaultValue) {
        return Wait.wrapper(supplier, defaultValue);
    }

    public static <T> Future<T> wrapAsync(final RunSupplier<Future<T>> supplier, final T defaultValue) {
        return Wait.wrapperAsync(supplier, defaultValue);
    }

    // bool -> json
    public static Future<JsonObject> wrapJ(final String field, final boolean result) {
        return Future.succeededFuture(Wander.wrapJ(field, result));
    }

    public static JsonObject wrapJS(final String field, final boolean result) {
        return Wander.wrapJ(field, result);
    }

    public static Future<JsonObject> wrapJ(final boolean result) {
        return Future.succeededFuture(Wander.wrapJ(KName.RESULT, result));
    }

    // JsonArray -> json
    public static Future<JsonObject> wrapJ(final String field, final JsonArray data) {
        return Future.succeededFuture(Wander.wrapJ(field, data));
    }

    public static Future<JsonObject> wrapJ(final JsonArray data) {
        return Future.succeededFuture(Wander.wrapJ(KName.DATA, data));
    }

    // json -> bool
    public static Future<Boolean> wrapB(final String field, final JsonObject input) {
        return Future.succeededFuture(Wander.wrapB(field, input));
    }

    public static Future<Boolean> wrapB(final JsonObject input) {
        return Future.succeededFuture(Wander.wrapB(KName.RESULT, input));
    }

    // json -> data( field = json )
    public static <T> Function<T, Future<JsonObject>> wrapTo(final String field, final JsonObject data) {
        return t -> Future.succeededFuture(Wander.wrapTo(field, data).apply(t));
    }

    // json -> json ( field = data )
    public static <T> Function<JsonObject, Future<JsonObject>> wrapOn(
        final String field, final Function<T, Future<JsonObject>> executor) {
        return Wander.wrapOn(field, executor);
    }

    public static <T> Function<JsonObject, Future<JsonObject>> wrapTree(
        final String field, final Function<T, Future<JsonObject>> executor) {
        return Wander.wrapTree(field, false, executor);
    }

    public static <T> Function<JsonObject, Future<JsonObject>> wrapTree(
        final String field, final boolean deeply, final Function<T, Future<JsonObject>> executor) {
        return Wander.wrapTree(field, deeply, executor);
    }

    // ------ 防御式专用API
    /*
     * smart 功能未开启之前的检查必备
     * 1) ifString - 检查JsonObject中的 JsonArray / JsonObject，转换成 String
     *    ifStrings - 检查JsonArray中....
     * 上述方法对应的逆方法
     *    ifJObject
     *    ifJArray
     * 2) ifPage - 分页专用方法规范：list / count 属性，特定场景使用
     * 3) ifMerge - 合并双对象专用
     * 4) ifField - 提取字段执行专用，存在一个单字段消费链接专用的API，生成 Consumer<JsonObject>
     * 5) ifCopy / ifCopies - 复制专用，由于复制的第二参和第三参有可能会引起重载时JVM的错误判断，所以采用单复数模式
     * 6) ifDefault - 原来的 ifJValue
     * 7) 非空检查批量方法
     *    ifNil / ifNilJ / ifEmpty / ifEmptyA
     */
    public static JsonObject ifString(final JsonObject json, final String... fields) {
        Arrays.stream(fields).forEach(field -> Wag.ifString(json, field));
        return json;
    }

    public static Function<JsonObject, Future<JsonObject>> ifString(final String... fields) {
        return json -> Future.succeededFuture(ifString(json, fields));
    }

    public static JsonArray ifStrings(final JsonArray array, final String... fields) {
        Ut.itJArray(array).forEach(json -> ifString(json, fields));
        return array;
    }

    public static Function<JsonArray, Future<JsonArray>> ifStrings(final String... fields) {
        return array -> Future.succeededFuture(ifStrings(array, fields));
    }

    public static JsonObject ifJObject(final JsonObject json, final String... fields) {
        Arrays.stream(fields).forEach(field -> Wag.ifJson(json, field));
        return json;
    }

    public static Function<JsonObject, Future<JsonObject>> ifJObject(final String... fields) {
        return json -> Future.succeededFuture(ifJObject(json, fields));
    }

    public static JsonArray ifJArray(final JsonArray array, final String... fields) {
        Ut.itJArray(array).forEach(json -> ifJObject(json, fields));
        return array;
    }

    public static Function<JsonArray, Future<JsonArray>> ifJArray(final String... fields) {
        return array -> Future.succeededFuture(ifJArray(array, fields));
    }

    public static JsonObject ifDefault(final JsonObject record, final String field, final Object value) {
        return Wag.ifDefault(record, field, value);
    }

    public static Function<JsonObject, Future<JsonObject>> ifDefault(final String field, final Object value) {
        return record -> Future.succeededFuture(ifDefault(record, field, value));
    }

    // ======================= 和业务些许相关的复杂操作（特殊类API）

    public static JsonObject ifCopy(final JsonObject record, final String from, final String to) {
        return Wag.ifCopy(record, from, to);
    }

    public static Function<JsonObject, Future<JsonObject>> ifCopy(final String from, final String to) {
        return json -> Future.succeededFuture(ifCopy(json, from, to));
    }

    public static JsonObject ifCopies(final JsonObject target, final JsonObject source, final String... fields) {
        return Wag.ifCopies(target, source, fields);
    }

    public static Function<JsonObject, Future<JsonObject>> ifCopies(final JsonObject source, final String... fields) {
        return target -> Future.succeededFuture(ifCopies(target, source, fields));
    }

    /*
     * 「双态型」两种形态
     */
    public static JsonObject ifPage(final JsonObject pageData, final String... fields) {
        return Wag.ifPage(pageData, fields);
    }

    public static Function<JsonObject, Future<JsonObject>> ifPage(final String... fields) {
        return pageData -> Future.succeededFuture(ifPage(pageData, fields));
    }

    // 单模式特殊方法（无第二形态）
    public static <T> Function<T, Future<JsonObject>> ifMerge(final JsonObject input) {
        return t -> Future.succeededFuture(Wag.ifField(input, null, t));
    }

    public static <T> Function<T, Future<JsonObject>> ifField(final JsonObject input, final String field) {
        return t -> Future.succeededFuture(Wag.ifField(input, field, t));
    }

    public static <T, V> Consumer<JsonObject> ifField(final String field, final Function<V, T> executor) {
        return input -> {
            final JsonObject inputJ = Ut.valueJObject(input);
            if (inputJ.containsKey(field)) {
                final Object value = inputJ.getValue(field);
                if (Objects.nonNull(value)) {
                    executor.apply((V) value);
                }
            }
        };
    }
}
