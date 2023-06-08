package io.vertx.up.util;

import io.aeon.runtime.channel.AeonService;
import io.horizon.eon.em.typed.ChangeFlag;
import io.horizon.uca.qr.syntax.Ir;
import io.horizon.util.HUt;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.exchange.BMapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.function.*;

@SuppressWarnings("all")
public final class Ut extends HUt {
    private Ut() {
    }

    /*
     * Array or List calculation
     * 1) elementAdd
     * 2) elementSave
     * 3) elementClimb
     * 4) elementFind
     * 5) elementZip
     * 6) elementGroup
     * 7) elementSubset
     * 8) elementChild
     * 9) elementEach
     * 10) elementFlat
     * 11) elementCompress
     * 12) elementSet
     * 13) elementRevert
     * 14) elementCount
     */

    public static JsonArray elementClimb(final JsonArray children, final JsonArray tree) {
        return Collection.climb(children, tree, null);
    }

    public static JsonArray elementClimb(final JsonObject child, final JsonArray tree) {
        return Collection.climb(child, tree, null);
    }

    public static JsonArray elementChild(final JsonArray children, final JsonArray tree) {
        return Collection.child(children, tree, null);
    }

    public static JsonArray elementChild(final JsonObject child, final JsonArray tree) {
        return Collection.child(child, tree, null);
    }

    public static JsonArray elementZip(final JsonArray array, final String fieldKey,
                                       final String fieldOn, final ConcurrentMap<String, JsonArray> grouped) {
        return Jackson.zip(array, fieldKey, fieldOn, grouped, null);
    }

    public static JsonArray elementZip(final JsonArray array, final String fieldKey,
                                       final String fieldOn,
                                       final ConcurrentMap<String, JsonArray> grouped, final String fieldTo) {
        return Jackson.zip(array, fieldKey, fieldOn, grouped, fieldTo);
    }

    public static <T, V> Set<V> elementSet(final List<T> listT, final Function<T, V> executor) {
        return Collection.set(listT, executor);
    }

    /*
     *
     * 1) inverseCount
     * 2) inverseSet
     */

    /*
     * Encryption method for string
     * 1) encryptMD5
     * 2) encryptSHA256
     * 3) encryptSHA512
     *
     * 4.1) encryptBase64
     * 4.2) decryptBase64
     *
     * 5.1) encryptUrl
     * 5.2) decryptUrl
     *
     * 6.1) encryptRSAP / decryptRSAV ( Mode 1 )
     * 6.2) encryptRSAV / decryptRSAP ( Mode 2 )
     */

    public static String encryptBase64(final String username, final String password) {
        final String input = username + ":" + password;
        return HUt.encryptBase64(input);
    }

    public static String encryptJ(final Object value) {
        return Jackson.encodeJ(value);
    }

    public static <T> T decryptJ(final String literal) {
        return Jackson.decodeJ(literal);
    }

    // This is usage in case1 for integration, that's why keep here
    //    public static String encryptRSAPIo(final String input, final String keyPath) {
    //        final String keyContent = Ut.ioString(keyPath);
    //        return ED.rsa(true).encrypt(input, keyContent);
    //    }
    /*
     * Comparing method of two
     * 1) compareTo: Generate comparing method to return int
     */
    public static int compareTo(final int left, final int right) {
        return Compare.compareTo(left, right);
    }

    public static int compareTo(final String left, final String right) {
        return Compare.compareTo(left, right);
    }

    public static <T> int compareTo(final T left, final T right, final BiFunction<T, T, Integer> fnCompare) {
        return Compare.compareTo(left, right, fnCompare);
    }

    /*
     * Interator method for different usage
     *  1) itMap
     *  2) itSet
     *  3) itDay
     *  4) itWeek
     *  5) itList
     *  6) itArray
     *  7) itMatrix
     *  8) itCollection
     *  9) itRepeat
     * 10) itJObject
     * 11) itJArray
     * 12) itJson ( For <T> extract by JsonObject/JsonArray )
     * 13) itJString
     * 14) itStart / itEnd / itPart
     *
     * `it` means iterator method here
     * `et` means `Error Iterator` to be sure comsumer should throw some checked exception
     * The previous `et` have been moved to `Fn` class instead, in Ut there are only `it` left
     */
    public static <K, V> void itMap(final ConcurrentMap<K, V> map, final BiConsumer<K, V> fnEach) {
        CollectionIt.exec(map, fnEach);
    }

    public static <V> void itSet(final Set<V> set, final BiConsumer<V, Integer> fnEach) {
        final List<V> list = new ArrayList<>(set);
        CollectionIt.exec(list, fnEach);
    }

    public static <V> void itList(final List<V> list, final BiConsumer<V, Integer> fnEach) {
        CollectionIt.exec(list, fnEach);
    }


    public static <V> void itArray(final V[] array, final BiConsumer<V, Integer> fnEach) {
        CollectionIt.exec(Arrays.asList(array), fnEach);
    }

    public static <V> void itMatrix(final V[][] array, final Consumer<V> fnEach) {
        CollectionIt.exec(array, fnEach);
    }

    public static <F, S> void itCollection(final java.util.Collection<F> firsts, final Function<F, java.util.Collection<S>> seconds, final BiConsumer<F, S> consumer) {
        CollectionIt.exec(firsts, seconds, consumer);
    }

    public static <F, S> void itCollection(final java.util.Collection<F> firsts, final Function<F, java.util.Collection<S>> seconds, final BiConsumer<F, S> consumer, final BiPredicate<F, S> predicate) {
        CollectionIt.exec(firsts, seconds, consumer, predicate);
    }

    public static <T> void itStart(final JsonObject data, final String prefix, final BiConsumer<T, String> consumer) {
        CollectionIt.exec(data, prefix, true, consumer);
    }

    public static <T> void itPart(final JsonObject data, final String prefix, final BiConsumer<T, String> consumer) {
        CollectionIt.exec(data, prefix, null, consumer);
    }

    public static <T> void itEnd(final JsonObject data, final String prefix, final BiConsumer<T, String> consumer) {
        CollectionIt.exec(data, prefix, false, consumer);
    }

    /*
     * Reflection method to create instance
     * 1) instance
     * 2) singleton ( Global singleton object reference )
     *
     * Reflection method to be as action
     * 1) invoke / invokeAsync
     * 2) clazz
     * 3) isImplement
     * 4) proxy
     * 5) withNoArgConstructor
     * 6) childUnique
     * 7) field / fields
     * 8) contract / contractAsync ( @Contract )
     * 9) plugin
     * 10) service ( By Service Loader )
     */

    /**
     * 将 service 重命名，保证和原始的 service 不冲突
     * serviceChannel 会先检查 /META-INF/services/aeon/ 下的定义，如果没有则使用默认的
     *
     * @param interfaceCls
     * @param <T>
     *
     * @return
     */
    public static <T> T serviceChannel(final Class<T> interfaceCls) {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return AeonService.load(interfaceCls, classLoader);
    }

    public static <T> T plugin(final JsonObject options, final String pluginKey, final Class<T> interfaceCls) {
        return Instance.plugin(options, pluginKey, interfaceCls);
    }

    public static <T> T invoke(final Object instance, final String name, final Object... args) {
        return Invoker.invokeObject(instance, name, args);
    }

    public static <T> T invokeStatic(final Class<?> clazz, final String name, final Object... args) {
        return Invoker.invokeStatic(clazz, name, args);
    }

    public static <T> Future<T> invokeAsync(final Object instance, final Method method, final Object... args) {
        return Invoker.invokeAsync(instance, method, args);
    }

    public static Class<?> child(final Class<?> clazz) {
        return Instance.child(clazz);
    }

    public static Field[] fields(final Class<?> clazz) {
        return Instance.fields(clazz);
    }

    public static <T> Field contract(final T instance, final Class<?> fieldType) {
        return Instance.contract(Instance.class, instance, fieldType);
    }

    public static <T, V> void contract(final T instance, final Class<?> fieldType, final V value) {
        Instance.contract(Instance.class, instance, fieldType, value);
    }

    public static <T, V> Future<Boolean> contractAsync(final T instance, final Class<?> fieldType, final V value) {
        contract(instance, fieldType, value);
        return Future.succeededFuture(Boolean.TRUE);
    }

    /*
     * Serialization method operation method here.
     * 1) serialize / serializeJson
     * 2) deserialize
     *
     * Object converting here of serialization
     */
    public static <T, R extends Iterable> R serializeJson(final T t) {
        return Jackson.serializeJson(t, false);
    }

    public static <T, R extends Iterable> R serializeJson(final T t, final boolean isSmart) {
        return Jackson.serializeJson(t, isSmart);
    }

    public static <T> T deserialize(final JsonObject value, final Class<T> type) {
        return Jackson.deserialize(value, type, true);
    }

    public static <T> T deserialize(final JsonObject value, final Class<T> type, boolean isSmart) {
        return Jackson.deserialize(value, type, isSmart);
    }

    public static <T> T deserialize(final JsonArray value, final Class<T> type, boolean isSmart) {
        return Jackson.deserialize(value, type, isSmart);
    }

    public static <T> T deserialize(final String value, final Class<T> clazz, boolean isSmart) {
        return Jackson.deserialize(value, clazz, isSmart);
    }

    /*
     * ConfigStoreOptions reading
     * 1) loadJson
     * 2) loadYaml
     * 3) loadProp
     */
    public static ConfigStoreOptions loadJson(final String filename) {
        return Store.getJson(filename);
    }

    public static ConfigStoreOptions loadYaml(final String filename) {
        return Store.getYaml(filename);
    }

    public static ConfigStoreOptions loadProp(final String filename) {
        return Store.getProp(filename);
    }

    /*
     * Complex compare method for two record
     * - oldRecord: Old data record that stored in our system
     * - newRecord: New data record that come into our system
     * There are additional parameters for different usage
     * 1) - ignores: You can set ignore fields that will be skipped when comparing
     * 2) - typeMap: It's required because it contains ( field = Class<?> ) to record the type mapping
     *
     * 3) - fnPredicate: Function to check when standard comparing return FALSE
     * 4) - arrayDiff: It contains array diff configuration instead of fixed
     */


    /*
     * Ai analyzing for type based on
     * some different business requirement
     */
    public static Object aiJValue(final Object input, final Class<?> type) {
        return Value.aiJValue(input, type);
    }

    public static Object aiJValue(final Object input) {
        return Value.aiJValue(input, null);
    }

    public static Object aiValue(final Object input, final Class<?> type) {
        return Value.aiValue(input, type);
    }

    public static Object aiValue(final Object input) {
        return Value.aiValue(input, null);
    }

    public static ChangeFlag aiFlag(final JsonObject recordN, final JsonObject recordO) {
        return Jackson.flag(recordN, recordO);
    }

    public static ChangeFlag aiFlag(final JsonObject input) {
        return Jackson.flag(input);
    }

    // Specifical Api for data
    public static JsonObject aiDataN(final JsonObject input) {
        return Jackson.data(input, false);
    }

    public static JsonObject aiDataO(final JsonObject input) {
        return Jackson.data(input, true);
    }

    public static String aiJArray(final String literal) {
        return Jackson.aiJArray(literal);
    }

    public static JsonObject aiIn(final JsonObject in, final BMapping mapping, final boolean keepNil) {
        return Value.aiIn(in, mapping, keepNil);
    }

    public static JsonObject aiIn(final JsonObject in, final BMapping mapping) {
        return Value.aiIn(in, mapping, true);
    }

    public static JsonObject aiOut(final JsonObject out, final BMapping mapping, final boolean keepNil) {
        return Value.aiOut(out, mapping, keepNil);
    }

    public static JsonObject aiOut(final JsonObject out, final BMapping mapping) {
        return Value.aiOut(out, mapping, true);
    }


    /*
     * To conversation here
     * 1) toJArray
     * 2) toJObject
     * 3) toJValue
     * 4) toMonth / toYear
     * 5) toEnum
     * 6) toCollection / toPrimary
     * 7) toString
     * 8) toDateTime / toDate / toTime / toAdjust
     * 9) toBytes
     * 10) toSet
     * 11) toMap
     * 12) toMatched
     */

    public static Set<String> toMatched(final String input, final String regex) {
        return StringUtil.matched(input, regex);
    }

    public static JsonObject toJObject(final MultiMap map) {
        return To.toJObject(map);
    }


    public static HttpMethod toMethod(final Supplier<String> supplier, final HttpMethod defaultValue) {
        return To.toMethod(supplier, defaultValue);
    }

    public static HttpMethod toMethod(final Supplier<String> supplier) {
        return To.toMethod(supplier, HttpMethod.GET);
    }

    public static HttpMethod toMethod(final String value, final HttpMethod defaultValue) {
        return To.toMethod(() -> value, defaultValue);
    }

    public static HttpMethod toMethod(final String value) {
        return To.toMethod(() -> value, HttpMethod.GET);
    }

    public static <V> ConcurrentMap<String, V> toConcurrentMap(final JsonObject data) {
        return To.toMap(data);
    }

    public static Map<String, Object> toMap(final JsonObject data) {
        return To.toMapExpr(data);
    }

    /*
     * JsonObject tree visiting
     * 1) visitJObject
     * 2) visitJArray
     * 3) visitInt
     * 4) visitString
     * 5) visitT
     */
    public static JsonObject visitJObject(final JsonObject item, final String... keys) {
        return Jackson.visitJObject(item, keys);
    }

    public static JsonArray visitJArray(final JsonObject item, final String... keys) {
        return Jackson.visitJArray(item, keys);
    }

    public static Integer visitInt(final JsonObject item, final String... keys) {
        return Jackson.visitInt(item, keys);
    }

    public static String visitString(final JsonObject item, final String... keys) {
        return Jackson.visitString(item, keys);
    }

    public static <T> T visitT(final JsonObject item, final String... keys) {
        return Jackson.visitT(item, keys);
    }

    public static <T> T visitTSmart(final JsonObject item, final String path) {
        // Fix issue of split by regex, the . must be \\.
        final String[] pathes = path.split("\\.");
        return Jackson.visitT(item, pathes);
    }

    /*
     * String conversation
     * 1) fromBuffer
     * 2) fromObject
     * 3) fromJObject
     * 4) fromJoin
     * 5) fromAdjust
     * 6) fromExpression
     * 7) fromExpressionT
     * 8) fromPrefix
     * 9) fromDate
     */

    public static String fromObject(final Object value) {
        return StringUtil.from(value);
    }

    public static String fromJObject(final JsonObject value) {
        return StringUtil.from(value);
    }

    public static JsonObject fromPrefix(final JsonObject data, final String prefix) {
        return StringUtil.prefix(data, prefix);
    }

    /*
     * Math method
     * 1) mathMultiply
     * 2) mathSumDecimal
     * 3) mathSumInteger
     * 4) mathSumLong
     */
    public static Integer mathMultiply(final Integer left, final Integer right) {
        return Numeric.mathMultiply(left, right);
    }

    public static BigDecimal mathSumDecimal(final JsonArray source, final String field) {
        return Numeric.mathJSum(source, field, BigDecimal.class);
    }

    public static Integer mathSumInteger(final JsonArray source, final String field) {
        return Numeric.mathJSum(source, field, Integer.class);
    }

    public static Long mathSumLong(final JsonArray source, final String field) {
        return Numeric.mathJSum(source, field, Long.class);
    }

    /*
     * Mapping operation
     *
     * - valueJObject(JsonObject)                     Null Checking
     * - valueJObject(JsonObject, String)             JsonObject -> field -> JsonObject
     * - valueJArray(JsonArray)                       Null Checking
     * - valueJArray(JsonObject, String)              JsonObject -> field -> JsonArray
     * - valueSetArray(JsonArray, String)             JsonArray -> field -> Set<JsonArray>
     * - valueSetString(JsonArray, String)            JsonArray -> field -> Set<String>
     * - valueSetString(List<T>, Function<T,String> ) List<T> -> function -> Set<String>
     * - valueString(JsonArray, String)               JsonArray -> field -> String ( Unique Mapping )
     * - valueC
     * - valueCI
     *
     * - valueTime(LocalTime, LocalDateTime)
     */

    // Qr Field Processing
    public static String valueQrIn(final String field) {
        return Mapping.vQrField(field, Ir.Op.IN);
    }

    /*
     * 「双态型」两种形态
     */
    public static JsonObject valueToPage(final JsonObject pageData, final String... fields) {
        return To.valueToPage(pageData, fields);
    }

    // Single Processing

    // mapping + replace/append
    /*
     * <Flag>Over: Replace the whole input
     * -- reference not change
     * <Flag>: Extract the data from input only
     */
    public static JsonObject valueToOver(final JsonObject input, final JsonObject mapping, final boolean smart) {
        final JsonObject converted = Mapping.vTo(input, mapping, smart);
        input.mergeIn(converted, true);
        return input;
    }

    public static JsonArray valueToOver(final JsonArray input, final JsonObject mapping, final boolean smart) {
        Ut.itJArray(input).forEach(json -> valueToOver(json, mapping, smart));
        return input;
    }

    public static JsonObject valueTo(final JsonObject input, final JsonObject mapping, final boolean smart) {
        return Mapping.vTo(input, mapping, smart);
    }

    public static JsonArray valueTo(final JsonArray input, final JsonObject mapping, final boolean smart) {
        return Mapping.vTo(input, mapping, smart);
    }

    public static JsonArray valueTo(final JsonArray input, final JsonObject mapping, final BinaryOperator<JsonObject> itFn) {
        return Mapping.vTo(input, mapping, true, itFn);
    }

    public static JsonObject valueFromOver(final JsonObject input, final JsonObject mapping, final boolean smart) {
        final JsonObject converted = Mapping.vFrom(input, mapping, smart);
        input.mergeIn(converted, true);
        return input;
    }

    public static JsonArray valueFromOver(final JsonArray input, final JsonObject mapping, final boolean smart) {
        Ut.itJArray(input).forEach(json -> valueFromOver(json, mapping, smart));
        return input;
    }

    public static JsonObject valueFrom(final JsonObject input, final JsonObject mapping, final boolean smart) {
        return Mapping.vFrom(input, mapping, smart);
    }

    public static JsonArray valueFrom(final JsonArray input, final JsonObject mapping, final boolean smart) {
        return Mapping.vFrom(input, mapping, smart);
    }

    public static JsonArray valueFrom(final JsonArray input, final JsonObject mapping, final BinaryOperator<JsonObject> itFn) {
        return Mapping.vFrom(input, mapping, true, itFn);
    }
}
