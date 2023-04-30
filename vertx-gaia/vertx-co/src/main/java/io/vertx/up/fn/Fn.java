package io.vertx.up.fn;

import io.horizon.eon.VMessage;
import io.horizon.exception.AbstractException;
import io.horizon.exception.ProgramException;
import io.horizon.fn.ErrorSupplier;
import io.horizon.fn.ProgramActuator;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.horizon.exception.BootingException;
import io.horizon.exception.WebException;
import io.horizon.uca.log.Annal;
import io.vertx.up.util.Ut;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Unique interface to call function in zero framework.
 * 基本格式：
 * --- [] 代表集合，
 * --- () 代表异步
 * 基础标记位处理：
 * 1. 参数类型标记位：
 * --- J: JsonObject
 * --- A: JsonArray
 * --- T: 泛型T
 * --- B: Boolean
 * --- M: Map哈希表
 * --- L: 列表 List
 * --- G: 分组专用
 * 2. 函数标记位（按参数可重载）
 * --- Supplier:    Or
 * --- Function:    Of
 * --- Predicate:   x
 * --- Consumer:    At
 * --- Actuator:    Act
 * 3. 函数异常标记位矩阵：
 * ------------ |  Throwable  |  Exception  |  ZeroException  |  ZeroRunException
 * - Supplier - |
 * - Function - |
 * - Consumer - |
 * - Predicate  |
 */
@SuppressWarnings("all")
public final class Fn extends _Out {
    private Fn() {
    }

    public static <T> void runMonad(final Supplier<T> supplier, final Consumer<T> consumer) {
        Extension.safeT(supplier, consumer);
    }

    // ---------------------------------------------------- 响应函数 ----------------------------------------------------
    // ------ ZeroException out
    public static void outZero(final boolean condition, final Annal logger, final Class<? extends ProgramException> zeroClass, final Object... args) throws ProgramException {
        if (condition) {
            Warning.outZero(logger, zeroClass, args);
        }
    }

    // ------ ZeroException to RunZeroException out
    public static void outUp(final ProgramActuator actuator, final Annal logger) {
        Wait.wrapper(logger, actuator);
    }

    // ------ RunZeroException out
    /* Old Style */
    public static void outUp(final boolean condition, final Annal logger, final Class<? extends AbstractException> upClass, final Object... args) {
        if (condition) {
            Warning.outUp(logger, upClass, args);
        }
    }

    /* New Style */
    public static void outUp(final boolean condition, final Class<? extends BootingException> upClass, final Object... args) {
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

    /*
     * Program Used:
     * outOr / outQr
     * 1. This point should be first line of one method:
     *    if(condition){
     *        throw out exception of `_412NullValueException.class`
     *    }
     * 2. This point should be QR result checking after Future<T>
     *    Future.compose(record -> {
     *        if(Objects.isNull(record)){
     *             throw out exception of `_412NullValueException.class`
     *        }
     *    })
     *
     * The default message:
     * 1. Or -> [ Program ] Null Input
     * 2. Qr -> [ Program ] Null Record in database
     * */
    public static <T> void outOr(final T condition, final Class<?> clazz, final String message) {
        Warning.outOr(condition, clazz, message);
    }

    public static <T> void outOr(final T condition, final Class<?> clazz) {
        outOr(condition, clazz, VMessage.PROGRAM_NULL);
    }

    public static <T> void outQr(final T condition, final Class<?> clazz) {
        outOr(condition, clazz, VMessage.PROGRAM_QR);
    }

    // ------ Specification for JsonFormat
    public static <T> T runOut(final Supplier<T> supplier, final Class<? extends AbstractException> runCls, final Object... args) {
        return Warning.execRun(supplier, runCls, args);
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
    public static <T> T wrap(final ErrorSupplier<T> supplier, final T defaultValue) {
        return Wait.wrapper(supplier, defaultValue);
    }

    public static <T> Future<T> wrapAsync(final ErrorSupplier<Future<T>> supplier, final T defaultValue) {
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

    public static Future<JsonObject> wrapJ(final String field, final JsonObject data) {
        return Future.succeededFuture(Wander.wrapJ(field, data));
    }

    public static Future<JsonObject> wrapJ(final JsonArray data) {
        return Future.succeededFuture(Wander.wrapJ(KName.DATA, data));
    }

    public static Future<JsonObject> wrapJ(final JsonArray data, final JsonObject config) {
        return Future.succeededFuture(Wander.wrapJ(KName.DATA, data, config));
    }

    public static Future<JsonObject> wrapJ(final String field, final JsonArray data, final JsonObject config) {
        return Future.succeededFuture(Wander.wrapJ(field, data, config));
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

    public static Future<JsonObject> wrapWeb(final JsonObject json, final String field) {
        return Wander.wrapWeb(json, field);
    }

    public static Function<JsonObject, Future<JsonObject>> wrapWeb(final String field) {
        return json -> wrapWeb(json, field);
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
     * 7) 扩展非空检查专用方法
     *    ifNil     - 异步空检查             ofNil  默认值异步
     *    ifNull    - 同步空检查             ofNull 默认值异步
     *    ifEmpty   - 异步集合检查（无同步）
     */
    public static JsonObject ifString(final JsonObject json, final String... fields) {
        Arrays.stream(fields).forEach(field -> If.ifString(json, field));
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
        Arrays.stream(fields).forEach(field -> If.ifJson(json, field));
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
        return If.ifDefault(record, field, value);
    }

    public static Function<JsonObject, Future<JsonObject>> ifDefault(final String field, final Object value) {
        return record -> Future.succeededFuture(ifDefault(record, field, value));
    }

    // ======================= 和业务些许相关的复杂操作（特殊类API）

    public static JsonObject ifCopy(final JsonObject record, final String from, final String to) {
        return If.ifCopy(record, from, to);
    }

    public static Function<JsonObject, Future<JsonObject>> ifCopy(final String from, final String to) {
        return json -> Future.succeededFuture(ifCopy(json, from, to));
    }

    public static JsonObject ifCopies(final JsonObject target, final JsonObject source, final String... fields) {
        return If.ifCopies(target, source, fields);
    }

    public static Function<JsonObject, Future<JsonObject>> ifCopies(final JsonObject source, final String... fields) {
        return target -> Future.succeededFuture(ifCopies(target, source, fields));
    }

    /*
     * 「双态型」两种形态
     */
    public static JsonObject ifPage(final JsonObject pageData, final String... fields) {
        return If.ifPage(pageData, fields);
    }

    public static Function<JsonObject, Future<JsonObject>> ifPage(final String... fields) {
        return pageData -> Future.succeededFuture(ifPage(pageData, fields));
    }

    // 单模式特殊方法（无第二形态）
    public static <T> Function<T, Future<JsonObject>> ifMerge(final JsonObject input) {
        return t -> Future.succeededFuture(If.ifField(input, null, t));
    }

    public static <T> Function<T, Future<JsonObject>> ifField(final JsonObject input, final String field) {
        return t -> Future.succeededFuture(If.ifField(input, field, t));
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

    // ------------------------------- 异步处理 -----------------
    // 直接包装
    public static <I, T> Function<I, Future<T>> ifNil(final Function<I, Future<T>> executor) {
        return Wash.ifNil(executor);
    }

    // 默认值同步
    public static <I, T> Function<I, Future<T>> ifNil(final Supplier<T> supplier, final Supplier<Future<T>> executor) {
        return ifNil(supplier, (i) -> executor.get() /* Function */);
    }


    public static <I> Function<I, Future<JsonObject>> ifJObject(final Supplier<JsonObject> executor) {
        return ofJObject(() -> Future.succeededFuture(executor.get()));
    }

    public static <I> Function<I, Future<JsonObject>> ifJObject(final Function<I, JsonObject> executor) {
        return ofJObject(item -> Future.succeededFuture(executor.apply(item)));
    }

    public static <I> Function<I, Future<JsonArray>> ifJArray(final Supplier<JsonArray> executor) {
        return ofJArray(() -> Future.succeededFuture(executor.get()));
    }

    public static <I> Function<I, Future<JsonArray>> ifJArray(final Function<I, JsonArray> executor) {
        return ofJArray(item -> Future.succeededFuture(executor.apply(item)));
    }

    public static <I, T> Function<I, Future<T>> ifNil(final Supplier<T> supplier, final Function<I, Future<T>> executor) {
        return input -> ofNil(() -> Future.succeededFuture(supplier.get()), executor).apply(input);
    }

    // 默认值异步
    public static <I, T> Function<I, Future<T>> ofNil(final Supplier<Future<T>> supplier, final Supplier<Future<T>> executor) {
        return ofNil(supplier, i -> executor.get() /* Function */);
    }

    public static <I, T> Function<I, Future<T>> ofNil(final Supplier<Future<T>> supplier, final Function<I, Future<T>> executor) {
        return Wash.ifNil(supplier, executor);
    }

    // 变种（全异步，默认值同步）JsonObject
    public static <I> Function<I, Future<JsonObject>> ofJObject(final Function<I, Future<JsonObject>> executor) {
        return ofNil(() -> Future.succeededFuture(new JsonObject()), executor);
    }

    public static <I> Function<I, Future<JsonObject>> ofJObject(final Supplier<Future<JsonObject>> executor) {
        return ofJObject(i -> executor.get());
    }

    // 变种（全异步，默认值同步）JsonArray
    public static <I> Function<I, Future<JsonArray>> ofJArray(final Function<I, Future<JsonArray>> executor) {
        return ofNil(() -> Future.succeededFuture(new JsonArray()), executor);
    }

    public static <I> Function<I, Future<JsonArray>> ofJArray(final Supplier<Future<JsonArray>> executor) {
        return ofJArray(i -> executor.get());
    }

    // ------------------------------- 同步处理 -----------------
    public static <I, T> Function<I, Future<T>> ifNul(final Function<I, T> executor) {
        return Wash.ifNul(executor);
    }

    // 默认值同步
    public static <I, T> Function<I, Future<T>> ifNul(final Supplier<T> supplier, final Supplier<T> executor) {
        return ifNul(supplier, (i) -> executor.get() /* Function */);
    }

    public static <I, T> Function<I, Future<T>> ifNul(final Supplier<T> supplier, final Function<I, T> executor) {
        return input -> ofNul(() -> Future.succeededFuture(supplier.get()), executor).apply(input);
    }

    // 默认值异步
    public static <I, T> Function<I, Future<T>> ofNul(final Supplier<Future<T>> supplier, final Supplier<T> executor) {
        return ofNul(supplier, i -> executor.get() /* Function */);
    }

    public static <I, T> Function<I, Future<T>> ofNul(final Supplier<Future<T>> supplier, final Function<I, T> executor) {
        return Wash.ifNul(supplier, executor);
    }

    public static <T> Function<T[], Future<T[]>> ifEmpty(final Function<T[], Future<T[]>> executor) {
        return Wash.ifEmpty(executor);
    }
}
