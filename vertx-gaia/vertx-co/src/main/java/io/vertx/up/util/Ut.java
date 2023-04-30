package io.vertx.up.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.horizon.eon.em.ChangeFlag;
import io.horizon.exception.WebException;
import io.horizon.specification.runtime.HService;
import io.horizon.util.HaS;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.commune.exchange.BMapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.function.*;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public final class Ut extends HaS {
    private Ut() {
    }

    public static JsonMapper mapper() {
        return Jackson.getMapper();
    }
    /*
     * Set Calculating
     * 1) intersect:    Set1 And Set2
     * 2) union:        Set1 Or  Set2
     * 3) diff:         Set1 -   Set2
     */

    /**
     * Collection intersect ( HashSet / TreeSet )
     * A = {1, 2}
     * B = {1, 3}
     * The result should be {1}
     *
     * @param left  First Set
     * @param right Second Set
     * @param <T>   The element type in Set
     *
     * @return The result set
     */
    public static <T> Set<T> intersect(final Set<T> left, final Set<T> right) {
        return Arithmetic.intersect(left, right);
    }

    public static <T> List<T> intersect(final List<T> left, final List<T> right) {
        return new ArrayList<>(intersect(new HashSet<>(left), new HashSet<>(right)));
    }

    public static <T, V> Set<T> intersect(final Set<T> left, final Set<T> right, final Function<T, V> fnGet) {
        return Arithmetic.intersect(left, right, fnGet);
    }

    public static <T, V> List<T> intersect(final List<T> left, final List<T> right, final Function<T, V> fnGet) {
        return new ArrayList<>(intersect(new HashSet<>(left), new HashSet<>(right), fnGet));
    }

    public static <T> Set<T> union(final Set<T> left, final Set<T> right) {
        return Arithmetic.union(left, right);
    }

    public static <T> List<T> union(final List<T> left, final List<T> right) {
        return new ArrayList<>(union(new HashSet<>(left), new HashSet<>(right)));
    }

    public static <T, V> Set<T> union(final Set<T> left, final Set<T> right, final Function<T, V> fnGet) {
        return Arithmetic.union(left, right, fnGet);
    }

    public static <T, V> List<T> union(final List<T> left, final List<T> right, final Function<T, V> fnGet) {
        return new ArrayList<>(union(new HashSet<>(left), new HashSet<>(right), fnGet));
    }

    public static <T> Set<T> diff(final Set<T> subtrahend, final Set<T> minuend) {
        return Arithmetic.diff(subtrahend, minuend);
    }

    public static <T> List<T> diff(final List<T> substrahend, final List<T> minuend) {
        return new ArrayList<>(diff(new HashSet<>(substrahend), new HashSet<>(minuend)));
    }

    public static <T, V> Set<T> diff(final Set<T> subtrahend, final Set<T> minuend, final Function<T, V> fnGet) {
        return Arithmetic.diff(subtrahend, minuend, fnGet);
    }

    public static <T, V> List<T> diff(final List<T> subtrahend, final List<T> minuend, final Function<T, V> fnGet) {
        return new ArrayList<>(diff(new HashSet<>(subtrahend), new HashSet<>(minuend), fnGet));
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
    public static JsonArray elementFlat(final JsonArray input) {
        return ArrayJ.flat(input);
    }

    public static JsonArray elementAdd(final JsonArray array, final JsonObject jsonObject, final String field) {
        return ArrayJ.add(array, jsonObject, field);
    }

    public static JsonArray elementSave(final JsonArray array, final JsonArray json, final String field) {
        return ArrayJ.save(array, json, field);
    }

    public static JsonArray elementSave(final JsonArray array, final JsonObject json, final String field) {
        return ArrayJ.save(array, json, field);
    }

    public static <T> List<T> elementSave(final List<T> list, final T entity, final String field) {
        return ArrayL.save(list, entity, item -> Ut.field(item, field));
    }

    public static <T> List<T> elementSave(final List<T> list, final T entity, final Function<T, String> keyFn) {
        return ArrayL.save(list, entity, keyFn);
    }

    public static <T> List<T> elementRemove(final List<T> list, final T entity, final String field) {
        return ArrayL.remove(list, entity, item -> Ut.field(item, field));
    }

    public static <T> List<T> elementRemove(final List<T> list, final T entity, final Function<T, String> keyFn) {
        return ArrayL.remove(list, entity, keyFn);
    }

    public static JsonArray elementClimb(final JsonArray children, final JsonArray tree) {
        return ArrayJ.climb(children, tree, null);
    }

    public static JsonArray elementClimb(final JsonObject child, final JsonArray tree) {
        return ArrayJ.climb(child, tree, null);
    }

    public static JsonArray elementChild(final JsonArray children, final JsonArray tree) {
        return ArrayJ.child(children, tree, null);
    }

    public static JsonArray elementChild(final JsonObject child, final JsonArray tree) {
        return ArrayJ.child(child, tree, null);
    }

    public static JsonObject elementFind(final JsonArray input, final String field, final Object value) {
        return ArrayJ.find(input, field, value);
    }

    public static JsonObject elementFind(final JsonArray input, final JsonObject subsetQ) {
        return ArrayJ.find(input, subsetQ);
    }

    public static <T> T elementFind(final List<T> list, final Predicate<T> fnFilter) {
        return ArrayL.find(list, fnFilter);
    }

    public static JsonArray elementZip(final JsonArray source, final JsonArray target, final String sourceKey, final String targetKey) {
        return Jackson.mergeZip(source, target, sourceKey, targetKey);
    }

    public static JsonArray elementZip(final JsonArray source, final JsonArray target, final String bothKey) {
        return Jackson.mergeZip(source, target, bothKey, bothKey);
    }

    public static JsonArray elementZip(final JsonArray source, final JsonArray target) {
        return Jackson.mergeZip(source, target, "key", "key");
    }

    public static <F, S, T> List<T> elementZip(final List<F> first, final List<S> second, final BiFunction<F, S, T> function) {
        return ArrayL.zipper(first, second, function);
    }

    public static <F, T> ConcurrentMap<F, T> elementZip(final List<F> keys, final List<T> values) {
        return ArrayL.zipper(keys, values);
    }

    public static <K, V, E> ConcurrentMap<K, V> elementZip(final Collection<E> object, final Function<E, K> keyFn, final Function<E, V> valueFn) {
        return ArrayL.zipper(object, keyFn, valueFn);
    }

    public static <K, V, E> ConcurrentMap<K, V> elementZip(final E[] object, final Function<E, K> keyFn, final Function<E, V> valueFn) {
        return ArrayL.zipper(Arrays.asList(object), keyFn, valueFn);
    }

    public static <K, T, V> ConcurrentMap<K, V> elementZip(final ConcurrentMap<K, T> source, final ConcurrentMap<T, V> target) {
        return ArrayL.zipper(source, target);
    }

    public static JsonObject elementZip(final JsonArray array, final String field) {
        return ArrayL.zipper(array, field);
    }

    public static JsonArray elementZip(final JsonArray array, final String fieldKey,
                                       final String fieldOn, final ConcurrentMap<String, JsonArray> grouped) {
        return ArrayL.zipper(array, fieldKey, fieldOn, grouped, null);
    }

    public static JsonArray elementZip(final JsonArray array, final String fieldKey,
                                       final String fieldOn,
                                       final ConcurrentMap<String, JsonArray> grouped, final String fieldTo) {
        return ArrayL.zipper(array, fieldKey, fieldOn, grouped, fieldTo);
    }

    public static JsonObject elementSubset(final JsonObject input, final String... fields) {
        return ArrayL.subset(input, fields);
    }

    public static JsonObject elementSubset(final JsonObject input, final Set<String> set) {
        return ArrayL.subset(input, set);
    }

    public static JsonArray elementSubset(final JsonArray input, final Set<String> set) {
        return ArrayL.subset(input, set);
    }

    public static JsonArray elementSubset(final JsonArray input, final Function<JsonObject, Boolean> fnFilter) {
        return ArrayL.subset(input, fnFilter);
    }

    public static ConcurrentMap<String, Integer> elementCount(final JsonArray input, final String... fields) {
        return ArrayL.count(input, Arrays.stream(fields).collect(Collectors.toSet()));
    }

    public static ConcurrentMap<String, Integer> elementCount(final JsonArray input, final Set<String> fieldSet) {
        return ArrayL.count(input, fieldSet);
    }

    public static ConcurrentMap<String, Integer> elementCount(final JsonArray input, final JsonArray fieldArray) {
        final Set<String> fieldSet = toSet(fieldArray);
        return ArrayL.count(input, fieldSet);
    }

    public static <K, V, E> ConcurrentMap<K, List<V>> elementGroup(final Collection<E> object, final Function<E, K> keyFn, final Function<E, V> valueFn) {
        return ArrayL.group(object, keyFn, valueFn);
    }

    public static <K, V, E> ConcurrentMap<K, List<V>> elementGroup(final Collection<E> object, final Function<E, K> keyFn) {
        return ArrayL.group(object, keyFn, item -> (V) item);
    }

    public static <K, V> ConcurrentMap<K, V> elementMap(final List<V> dataList, final Function<V, K> keyFn) {
        return ArrayL.map(dataList, keyFn, item -> item);
    }

    public static <K, V, E> ConcurrentMap<K, V> elementMap(final List<E> dataList, final Function<E, K> keyFn, final Function<E, V> valueFn) {
        return ArrayL.map(dataList, keyFn, valueFn);
    }

    public static ConcurrentMap<String, JsonObject> elementMap(final JsonArray dataArray, final String field) {
        return ArrayL.map(dataArray, field);
    }

    public static <T> ConcurrentMap<String, T> elementMap(final JsonArray dataArray, final String field, final String to) {
        return ArrayL.map(dataArray, field, to);
    }

    public static ConcurrentMap<String, JsonArray> elementGroup(final JsonArray source, final String field) {
        return ArrayL.group(source, field);
    }

    public static List<JsonArray> elementGroup(final JsonArray source, final Integer size) {
        return ArrayL.group(source, size);
    }

    public static ConcurrentMap<String, JsonArray> elementGroup(final JsonArray source, final Function<JsonObject, String> executor) {
        return ArrayL.group(source, executor);
    }

    public static <K, V> ConcurrentMap<K, List<V>> elementCompress(final List<ConcurrentMap<K, List<V>>> dataList) {
        return ArrayL.compress(dataList);
    }

    public static <K, V> ConcurrentMap<V, Set<K>> elementRevert(final ConcurrentMap<K, V> dataMap) {
        return ArrayL.revert(dataMap);
    }

    public static <T, V> Set<V> elementSet(final List<T> listT, final Function<T, V> executor) {
        return ArrayL.set(listT, executor);
    }

    /*
     *
     * 1) inverseCount
     * 2) inverseSet
     */
    public static <K, V> ConcurrentMap<V, Integer> inverseCount(final ConcurrentMap<K, V> input) {
        return ArrayL.inverse(input, Set::size);
    }

    public static <K, V> ConcurrentMap<V, Set<K>> inverseSet(final ConcurrentMap<K, V> input) {
        return ArrayL.inverse(input, set -> set);
    }

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
        return HaS.encryptBase64(input);
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
        Congregation.exec(map, fnEach);
    }

    public static <V> void itSet(final Set<V> set, final BiConsumer<V, Integer> fnEach) {
        final List<V> list = new ArrayList<>(set);
        Congregation.exec(list, fnEach);
    }

    public static <V> void itList(final List<V> list, final BiConsumer<V, Integer> fnEach) {
        Congregation.exec(list, fnEach);
    }


    public static <V> void itArray(final V[] array, final BiConsumer<V, Integer> fnEach) {
        Congregation.exec(Arrays.asList(array), fnEach);
    }

    public static <V> void itMatrix(final V[][] array, final Consumer<V> fnEach) {
        Congregation.exec(array, fnEach);
    }

    public static <F, S> void itCollection(final Collection<F> firsts, final Function<F, Collection<S>> seconds, final BiConsumer<F, S> consumer) {
        Congregation.exec(firsts, seconds, consumer);
    }

    public static <F, S> void itCollection(final Collection<F> firsts, final Function<F, Collection<S>> seconds, final BiConsumer<F, S> consumer, final BiPredicate<F, S> predicate) {
        Congregation.exec(firsts, seconds, consumer, predicate);
    }

    public static <T> void itJObject(final JsonObject data, final BiConsumer<T, String> fnEach) {
        Congregation.exec(data, fnEach);
    }

    public static <T> void itJArray(final JsonArray array, final Class<T> clazz, final BiConsumer<T, Integer> fnEach) {
        Congregation.exec(array, clazz, fnEach);
    }

    public static void itJArray(final JsonArray array, final BiConsumer<JsonObject, Integer> fnEach) {
        Congregation.exec(array, JsonObject.class, fnEach);
    }

    public static <T> void itStart(final JsonObject data, final String prefix, final BiConsumer<T, String> consumer) {
        Congregation.exec(data, prefix, true, consumer);
    }

    public static <T> void itPart(final JsonObject data, final String prefix, final BiConsumer<T, String> consumer) {
        Congregation.exec(data, prefix, null, consumer);
    }

    public static <T> void itEnd(final JsonObject data, final String prefix, final BiConsumer<T, String> consumer) {
        Congregation.exec(data, prefix, false, consumer);
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
     * 该方法会隐藏掉继承过来的方法
     *
     * @param interfaceCls
     * @param <T>
     *
     * @return
     */
    public static <T> T service(final Class<T> interfaceCls) {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return HService.load(interfaceCls, classLoader);
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

    public static <T> void field(final Object instance, final String name, final T value) {
        InstanceField.set(instance, name, value);
    }

    public static <T> void field(final Object instance, final Field field, final T value) {
        InstanceField.set(instance, field, value);
    }

    public static <T> T field(final Object instance, final String name) {
        return InstanceField.get(instance, name);
    }

    public static <T> T field(final Class<?> interfaceCls, final String name) {
        return InstanceField.getI(interfaceCls, name);
    }

    public static Field[] fields(final Class<?> clazz) {
        return InstanceField.fields(clazz);
    }

    public static <T> Field contract(final T instance, final Class<?> fieldType) {
        return InstanceField.contract(InstanceField.class, instance, fieldType);
    }

    public static <T, V> void contract(final T instance, final Class<?> fieldType, final V value) {
        InstanceField.contract(InstanceField.class, instance, fieldType, value);
    }

    public static <T, V> Future<Boolean> contractAsync(final T instance, final Class<?> fieldType, final V value) {
        contract(instance, fieldType, value);
        return Future.succeededFuture(Boolean.TRUE);
    }

    /*
     * 1) elementCopy
     * 2) elementAppend ( The old name is ifMerge )
     * 3) elementMerge
     * 4) elementFind
     */
    public static void elementCopy(final JsonObject target, final JsonObject source, final String... fields) {
        Jackson.jsonCopy(target, source, fields);
    }

    public static JsonObject elementAppend(final JsonObject target, final JsonObject... sources) {
        Arrays.stream(sources).forEach(source -> Jackson.jsonAppend(target, source, true));
        return target;
    }

    public static JsonObject elementMerge(final JsonObject target, final JsonObject... sources) {
        Arrays.stream(sources).forEach(source -> Jackson.jsonMerge(target, source, true));
        return target;
    }

    public static <T> T elementFind(final JsonObject item, final String field, final Class<T> clazz) {
        return Epsilon.vValue(item, field, clazz);
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

    public static <T> String serialize(final T t) {
        return Jackson.serialize(t);
    }

    public static <T> T deserialize(final JsonObject value, final Class<T> type) {
        return Jackson.deserialize(value, type, true);
    }

    public static <T> T deserialize(final JsonObject value, final Class<T> type, boolean isSmart) {
        return Jackson.deserialize(value, type, isSmart);
    }

    public static <T> T deserialize(final JsonArray value, final Class<T> type) {
        return Jackson.deserialize(value, type, false);
    }

    public static <T> T deserialize(final JsonArray value, final Class<T> type, boolean isSmart) {
        return Jackson.deserialize(value, type, isSmart);
    }

    public static <T> List<T> deserialize(final JsonArray value, final TypeReference<List<T>> type) {
        return Jackson.deserialize(value, type);
    }

    public static <T> T deserialize(final String value, final Class<T> clazz) {
        return Jackson.deserialize(value, clazz, false);
    }

    public static <T> T deserialize(final String value, final Class<T> clazz, boolean isSmart) {
        return Jackson.deserialize(value, clazz, isSmart);
    }

    public static <T> T deserialize(final String value, final TypeReference<T> type) {
        return Jackson.deserialize(value, type);
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
     * Network method
     * 1) netOk
     * 2) netIPv4 / netIPv6 / netIP
     * 3) netHostname
     * 4) netStatus
     * 5) netUri
     *
     */

    /*
     * Map reduce method here
     * 1) reduce
     */
    public static <K, T, V> ConcurrentMap<K, V> reduce(final ConcurrentMap<K, T> from, final ConcurrentMap<T, V> to) {
        return ArrayL.reduce(from, to);
    }

    public static <K, V> ConcurrentMap<K, V> reduce(final Set<K> from, final ConcurrentMap<K, V> to) {
        return ArrayL.reduce(from, to);
    }

    /*
     * isMatch of Regex
     */

    /*
     * Checking method for all
     * 1) isSame / isDate
     * 2) isBoolean
     * 3) isPositive / isNegative / isInteger
     * 4) isDecimal / isReal / isDecimalPositive / isDecimalNegative
     * 5) isSubset ( Compare 2 JsonObject )
     * 6) isJObject / isJArray
     * 7) isClass / isVoid / isPrimary
     * 8) isNil / notNil
     * 9) isRange
     */
    public static boolean isRange(final Integer value, final Integer min, final Integer max) {
        return Numeric.isRange(value, min, max);
    }

    public static boolean isIn(final JsonObject input, final String... fields) {
        return Types.isIn(input, fields);
    }

    public static <T> boolean isSame(final JsonObject record, final String field, final T expected) {
        return Types.isEqual(record, field, expected);
    }

    public static boolean isDiff(final JsonArray left, final JsonArray right, final Set<String> fields) {
        return !ArrayJ.isSame(left, right, fields, false);
    }

    public static boolean isSame(final JsonArray left, final JsonArray right, final Set<String> fields) {
        return ArrayJ.isSame(left, right, fields, false);
    }

    public static boolean isDiff(final JsonArray left, final JsonArray right) {
        return isDiff(left, right, new HashSet<>());
    }

    public static boolean isSame(final JsonArray left, final JsonArray right) {
        return isSame(left, right, new HashSet<>());
    }

    public static boolean isSameBy(final JsonArray left, final JsonArray right, final Set<String> fields) {
        return ArrayJ.isSame(left, right, fields, true);
    }

    public static boolean isSameBy(final JsonArray left, final JsonArray right) {
        return isSameBy(left, right, new HashSet<>());
    }

    public static boolean isDiffBy(final JsonArray left, final JsonArray right, final Set<String> fields) {
        return !ArrayJ.isSame(left, right, fields, true);
    }

    public static boolean isDiffBy(final JsonArray left, final JsonArray right) {
        return isDiffBy(left, right, new HashSet<>());
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

    public static JsonObject toJObject(final String literal, final Function<JsonObject, JsonObject> itemFn) {
        return To.toJObject(literal, itemFn);
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

    public static WebException toError(final Class<?> clazz, final Throwable error) {
        return To.toError(clazz, error);
    }

    public static WebException toError(final Class<? extends WebException> clazz, final Object... args) {
        return To.toError(clazz, args);
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
        return Epsilon.vQrField(field, Qr.Op.IN);
    }

    // Single Processing

    // mapping + replace/append
    /*
     * <Flag>Over: Replace the whole input
     * -- reference not change
     * <Flag>: Extract the data from input only
     */
    public static JsonObject valueToOver(final JsonObject input, final JsonObject mapping, final boolean smart) {
        final JsonObject converted = Epsilon.vTo(input, mapping, smart);
        input.mergeIn(converted, true);
        return input;
    }

    public static JsonArray valueToOver(final JsonArray input, final JsonObject mapping, final boolean smart) {
        Ut.itJArray(input).forEach(json -> valueToOver(json, mapping, smart));
        return input;
    }

    public static JsonObject valueTo(final JsonObject input, final JsonObject mapping, final boolean smart) {
        return Epsilon.vTo(input, mapping, smart);
    }

    public static JsonArray valueTo(final JsonArray input, final JsonObject mapping, final boolean smart) {
        return Epsilon.vTo(input, mapping, smart);
    }

    public static JsonArray valueTo(final JsonArray input, final JsonObject mapping, final BinaryOperator<JsonObject> itFn) {
        return Epsilon.vTo(input, mapping, true, itFn);
    }

    public static JsonObject valueFromOver(final JsonObject input, final JsonObject mapping, final boolean smart) {
        final JsonObject converted = Epsilon.vFrom(input, mapping, smart);
        input.mergeIn(converted, true);
        return input;
    }

    public static JsonArray valueFromOver(final JsonArray input, final JsonObject mapping, final boolean smart) {
        Ut.itJArray(input).forEach(json -> valueFromOver(json, mapping, smart));
        return input;
    }

    public static JsonObject valueFrom(final JsonObject input, final JsonObject mapping, final boolean smart) {
        return Epsilon.vFrom(input, mapping, smart);
    }

    public static JsonArray valueFrom(final JsonArray input, final JsonObject mapping, final boolean smart) {
        return Epsilon.vFrom(input, mapping, smart);
    }

    public static JsonArray valueFrom(final JsonArray input, final JsonObject mapping, final BinaryOperator<JsonObject> itFn) {
        return Epsilon.vFrom(input, mapping, true, itFn);
    }
}
