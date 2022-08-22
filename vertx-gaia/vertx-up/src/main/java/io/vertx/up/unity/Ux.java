package io.vertx.up.unity;

import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.ClusterSerializable;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.FileUpload;
import io.vertx.tp.plugin.database.DataPool;
import io.vertx.tp.plugin.jooq.JooqDsl;
import io.vertx.tp.plugin.jooq.JooqInfix;
import io.vertx.up.atom.Kv;
import io.vertx.up.atom.query.Pagination;
import io.vertx.up.atom.record.Apt;
import io.vertx.up.atom.secure.AegisItem;
import io.vertx.up.atom.secure.Vis;
import io.vertx.up.commune.Envelop;
import io.vertx.up.commune.Record;
import io.vertx.up.commune.exchange.DConsumer;
import io.vertx.up.commune.exchange.DFabric;
import io.vertx.up.commune.exchange.DSetting;
import io.vertx.up.eon.Constants;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.em.AuthWall;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.exception.WebException;
import io.vertx.up.experiment.rule.RuleTerm;
import io.vertx.up.fn.Fn;
import io.vertx.up.secure.Lee;
import io.vertx.up.secure.LeeBuiltIn;
import io.vertx.up.uca.jooq.UxJoin;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.util.Ut;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentMap;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * #「Kt」Utility X Component in zero
 * <p>
 * Here Ux is a util interface of uniform to call different tools.
 * It just like helper for business usage.
 */
@SuppressWarnings("all")
public final class Ux {

    /*
     * output part converting
     */
    public static JsonObject outBool(final boolean checked) {
        return Async.bool(KName.RESULT, checked);
    }

    public static JsonObject outBool(final String key, final boolean checked) {
        return Async.bool(key, checked);
    }

    /**
     * Create new log instance for store `Annal` mapping
     * <p>
     * Debug method for help us to do development
     * 1) debug:
     * 2) otherwise:
     * 3) dataN/dataO -> New / Old Json
     * ( Business Part: Debugging )
     */

    public static void debug(final Object... objects) {
        Debug.monitor(objects);
    }

    public static <T> Future<T> debug(final T item) {
        return Debug.debug(item);
    }

    public static <T> T debug(final Throwable error, final Supplier<T> supplier) {
        return Debug.debug(error, supplier);
    }

    public static Function<Throwable, Envelop> otherwise() {
        return Debug.otherwise();
    }

    public static <T> Function<Throwable, T> otherwise(Supplier<T> supplier) {
        return Debug.otherwise(supplier);
    }

    public static <T> Function<Throwable, T> otherwise(T input) {
        return otherwise(() -> input);
    }


    /*
     * Update Data on Record
     * 1. Generic T ( Pojo )
     * 2. List<T>
     * 3. JsonObject
     * 4. JsonArray
     * 5. Record
     * 6. Record[]
     */
    public static <T> T cloneT(final T input) {
        return Compare.cloneT(input);
    }

    public static <T> T updateT(final T query, final JsonObject params) {
        return Compare.updateT(query, params);
    }

    public static <T> List<T> updateT(final List<T> query, final JsonArray params) {
        return Compare.updateT(query, params, KName.KEY);
    }

    public static <T> List<T> updateT(final List<T> query, final JsonArray params, final String field) {
        return Compare.updateT(query, params, field);
    }

    public static JsonArray updateJ(final JsonArray query, final JsonArray params) {
        return Compare.updateJ(query, params, KName.KEY);
    }

    public static JsonArray updateJ(final JsonArray query, final JsonArray params, final String field) {
        return Compare.updateJ(query, params, field);
    }

    public static Record updateR(final Record record, final JsonObject params) {
        return Compare.updateR(record, params, () -> UUID.randomUUID().toString());
    }

    public static Record[] updateR(final Record[] record, final JsonArray array) {
        return updateR(record, array, KName.KEY);
    }

    public static Record[] updateR(final Record[] record, final JsonArray array, final String field) {
        final List<Record> recordList = Arrays.asList(record);
        return Compare.updateR(recordList, array, field).toArray(new Record[]{});
    }

    /*
     * Rule Match
     * 1. single checking
     * 2. double checking
     * 3. array checking
     */
    public static JsonObject ruleAll(final Collection<RuleTerm> rules, final JsonObject input) {
        return Unique.ruleAll(rules, input);
    }

    public static ConcurrentMap<Boolean, JsonArray> ruleAll(final Collection<RuleTerm> rules, final JsonArray input) {
        return Unique.ruleAll(rules, input);
    }

    public static JsonObject ruleAll(final Collection<RuleTerm> rules, final JsonObject recordO, final JsonObject recordN) {
        return Unique.ruleAll(rules, recordO, recordN);
    }

    public static JsonObject ruleAll(final Collection<RuleTerm> rules, final JsonArray source, final JsonObject record) {
        return Unique.ruleAll(rules, source, record);
    }

    public static JsonObject ruleAny(final Collection<RuleTerm> rules, final JsonObject input) {
        return Unique.ruleAny(rules, input);
    }

    public static JsonObject ruleAny(final Collection<RuleTerm> rules, final JsonObject record0, final JsonObject recordN) {
        return Unique.ruleAny(rules, record0, recordN);
    }

    public static JsonObject ruleAny(final Collection<RuleTerm> rules, final JsonArray source, final JsonObject record) {
        return Unique.ruleAny(rules, source, record);
    }

    public static ConcurrentMap<Boolean, JsonArray> ruleAny(final Collection<RuleTerm> rules, final JsonArray input) {
        return Unique.ruleAny(rules, input);
    }

    public static JsonObject ruleTwins(final JsonObject recordO, final JsonObject recordN) {
        return Unique.ruleTwins(recordO, recordN);
    }

    public static JsonObject ruleNil(final JsonObject twins, final ChangeFlag flag) {
        return Unique.ruleNil(twins, flag);
    }

    public static JsonObject ruleNil(final JsonObject recordN, final JsonObject recordO) {
        return Objects.isNull(recordN) ? recordO : recordN;
    }

    public static Apt ruleApt(final JsonArray twins, final boolean isReplaced) {
        return Unique.ruleApt(twins, isReplaced);
    }

    // ------------------------- Compare Json ------------------------
    /*
     *  1) ruleJOk
     *  2) ruleJReduce
     *  3) ruleJEqual
     *  4) ruleJFind
     */
    public static boolean ruleJOk(final JsonObject record, final Set<String> fields) {
        return CompareJ.ruleJOk(record, fields);
    }

    public static boolean ruleJOk(final JsonObject record, final JsonArray matrix) {
        return CompareJ.ruleJOk(record, matrix);
    }

    public static JsonArray ruleJReduce(final JsonArray records, final Set<String> fields) {
        return CompareJ.ruleJReduce(records, fields);
    }

    public static JsonArray ruleJReduce(final JsonArray records, final JsonArray matrix) {
        return CompareJ.ruleJReduce(records, matrix);
    }

    public static boolean ruleJEqual(final JsonObject record, final JsonObject latest, final Set<String> fields) {
        return CompareJ.ruleJEqual(record, latest, fields);
    }

    public static boolean ruleJEqual(final JsonObject record, final JsonObject latest, final JsonArray matrix) {
        return CompareJ.ruleJEqual(record, latest, matrix);
    }

    public static JsonObject ruleJFind(final JsonArray source, final JsonObject expected, final Set<String> fields) {
        return CompareJ.ruleJFind(source, expected, fields);
    }

    public static JsonObject ruleJFind(final JsonArray source, final JsonObject expected, final JsonArray matrix) {
        return CompareJ.ruleJFind(source, expected, matrix);
    }

    /*
     * Entity ( Pojo ) to JsonObject, support pojo file here
     * 1) toJson / fromJson
     * 2) toZip:  Toggle switch from interface style to worker here, the key should be "0", "1", "2", "3", ....
     * 3) toJArray
     * ( Business Part, support `pojoFile` conversation )
     * 4) toFile
     */

    public static <T> JsonObject toJson(final T entity) {
        return To.toJObject(entity, "");
    }

    public static <T> JsonObject toJson(final T entity, final String pojo) {
        return To.toJObject(entity, pojo);
    }

    public static <T> JsonArray toJson(final List<T> list) {
        return To.toJArray(list, "");
    }

    public static <T> JsonArray toJson(final List<T> list, final String pojo) {
        return To.toJArray(list, pojo);
    }

    public static JsonObject toZip(final Object... args) {
        return To.toToggle(args);
    }

    public static <T> T fromJson(final JsonObject data, final Class<T> clazz) {
        return From.fromJson(data, clazz, "");
    }

    public static <T> List<T> fromJson(final JsonArray array, final Class<T> clazz) {
        return From.fromJson(array, clazz, "");
    }

    public static <T> List<T> fromPage(final JsonObject data, final Class<T> clazz) {
        return fromJson(pageData(data), clazz);
    }

    public static <T> T fromJson(final JsonObject data, final Class<T> clazz, final String pojo) {
        return From.fromJson(data, clazz, pojo);
    }

    public static <T> List<T> fromJson(final JsonArray array, final Class<T> clazz, final String pojo) {
        return From.fromJson(array, clazz, pojo);
    }

    public static JsonObject criteria(final JsonObject data, final String pojo) {
        return From.fromJson(data, pojo);
    }

    /**
     * File upload tool to convert data
     *
     * @param fileUploads Set of file uploads
     * @param expected    The method declared type
     * @param consumer    File consumer to read `filename` to Buffer
     * @param <T>         Returned type for declared
     * @return T reference that converted
     */
    public static <T> T toFile(final Set<FileUpload> fileUploads, final Class<?> expected, final Function<String, Buffer> consumer) {
        return Upload.toFile(fileUploads, expected, consumer);
    }

    /**
     * Single file upload converting
     *
     * @param fileUpload The `FileUpload` reference
     * @param expected   The method declared type
     * @param consumer   File consumer to read `filename` to Buffer
     * @param <T>        Returned type of declared
     * @return T reference that converted
     */
    public static <T> T toFile(final FileUpload fileUpload, final Class<?> expected, final Function<String, Buffer> consumer) {
        return Upload.toFile(fileUpload, expected, consumer);
    }

    /**
     * Split `Set<FileUpload>` by fieldname
     *
     * @param fileUploads FileUpload Set
     * @return Map of `field = Set<FileUpload>`
     */
    public static ConcurrentMap<String, Set<FileUpload>> toFile(final Set<FileUpload> fileUploads) {
        return Upload.toFile(fileUploads);
    }

    /*
     * Envelop building here
     * 1) envelop: ( Get different Envelop )
     * 2) future: ( Wrapper Future.successedFuture / Future.failureFuture ) at same time
     * 3) handler: ( Handler<AsyncResult<T>> )
     * 4) compare: ( Compare two object )
     * 5) complex:
     *    - JsonObject -> condition -> executor
     *    - JsonArray -> condition -> grouper -> executor
     */
    public static Envelop fromEnvelop(final Class<? extends WebException> clazz, final Object... args) {
        return To.toEnvelop(clazz, args);
    }

    public static <T> Envelop fromEnvelop(final T entity) {
        return To.toEnvelop(entity);
    }

    public static <T> Envelop fromEnvelop(final T entity, final WebException error) {
        return To.toEnvelop(entity, error);
    }

    public static <T> Future<T> fromAsync(final CompletionStage<T> state) {
        return Async.fromAsync(state);
    }

    public static <T> Future<T> future(final T entity) {
        return To.future(entity);
    }

    public static <T> Future<T> future(final T input, final List<Function<T, Future<T>>> functions) {
        return Async.future(input, functions);
    }

    public static <T> Future<T> future(final T input, final Set<Function<T, Future<T>>> functions) {
        return Async.future(input, functions);
    }

    public static <T> Future<T> future() {
        return To.future(null);
    }

    public static Future<JsonObject> complex(final JsonObject input, final Predicate<JsonObject> predicate, final Supplier<Future<JsonObject>> executor) {
        return Complex.complex(input, predicate, executor);
    }

    public static Future<JsonArray> complex(final Pagination first, final Function<JsonObject, Future<Integer>> total, final Function<Pagination, Future<JsonObject>> page, final Function<JsonObject, Future<JsonArray>> result) {
        return Complex.complex(first, total, page, result, JsonArray::addAll);
    }

    public static Function<Pagination, Future<JsonArray>> complex(final Function<JsonObject, Future<Integer>> total, final Function<Pagination, Future<JsonObject>> page, final Function<JsonObject, Future<JsonArray>> result) {
        return Complex.complex(total, page, result);
    }

    public static Function<Pagination, Future<JsonArray>> complex(final Function<Pagination, Future<JsonObject>> page) {
        return complex(data -> To.future(data.getInteger("count")), page, response -> To.future(response.getJsonArray("list")));
    }

    public static <T> Handler<AsyncResult<T>> handler(final Message<Envelop> message) {
        return Web.toHandler(message);
    }

    public static <T> Future<T> handler(final Consumer<Handler<AsyncResult<T>>> handler) {
        return Web.toFuture(handler);
    }
    /*
     * 1) compare
     * 2) compareJ
     * 3) compareRun
     */

    public static <T, R> ConcurrentMap<ChangeFlag, List<T>> compare(final List<T> original, final List<T> current, final Function<T, R> fnValue, final String pojoFile) {
        return Compare.compare(original, current, fnValue, pojoFile);
    }

    public static <T, R> ConcurrentMap<ChangeFlag, List<T>> compare(final List<T> original, final List<T> current, final Function<T, R> fnValue) {
        return Compare.compare(original, current, fnValue, Strings.EMPTY);
    }

    public static <T, R> ConcurrentMap<ChangeFlag, List<T>> compare(final List<T> original, final List<T> current, final Set<String> uniqueSet, final String pojoFile) {
        return Compare.compare(original, current, uniqueSet, pojoFile);
    }

    public static <T, R> ConcurrentMap<ChangeFlag, List<T>> compare(final List<T> original, final List<T> current, final Set<String> uniqueSet) {
        return Compare.compare(original, current, uniqueSet, Strings.EMPTY);
    }

    public static ConcurrentMap<ChangeFlag, JsonArray> compareJ(
            final JsonArray original, final JsonArray current, final Set<String> fields) {
        return CompareJ.compareJ(original, current, fields);
    }

    public static ConcurrentMap<ChangeFlag, JsonArray> compareJ(
            final JsonArray original, final JsonArray current, final String field) {
        return CompareJ.compareJ(original, current, field);
    }

    public static ConcurrentMap<ChangeFlag, JsonArray> compareJ(
            final JsonArray original, final JsonArray current, final JsonArray matrix) {
        return CompareJ.compareJ(original, current, matrix);
    }

    public static Future<ConcurrentMap<ChangeFlag, JsonArray>> compareJAsync(
            final JsonArray original, final JsonArray current, final Set<String> fields) {
        return To.future(CompareJ.compareJ(original, current, fields));
    }

    public static Future<ConcurrentMap<ChangeFlag, JsonArray>> compareJAsync(
            final JsonArray original, final JsonArray current, final String field) {
        return To.future(CompareJ.compareJ(original, current, field));
    }

    public static Future<ConcurrentMap<ChangeFlag, JsonArray>> compareJAsync(
            final JsonArray original, final JsonArray current, final JsonArray matrix) {
        return To.future(CompareJ.compareJ(original, current, matrix));
    }

    public static <T> Future<JsonArray> compareRun(final ConcurrentMap<ChangeFlag, List<T>> compared, final Function<List<T>, Future<List<T>>> insertAsyncFn, final Function<List<T>, Future<List<T>>> updateAsyncFn) {
        return Compare.run(compared, insertAsyncFn, updateAsyncFn);
    }

    /*
     *  future prefix processing here
     *
     * JsonArray
     * 1) futureA
     * -- futureA()
     * -- futureA(List)
     * -- futureA(List, pojo)
     * -- futureA(String)
     * -- futureA(Record[])
     *
     * JsonObject
     * 2) futureJ
     * -- futureJ()
     * -- futureJ(T)
     * -- futureJ(T, pojo)
     * -- futureJ(String)
     * -- futureJ(Record)
     * -- futureJM(T, String)
     *
     * List
     * 3) futureL
     * -- futureL()
     * -- futureL(List)
     * -- futureL(List, pojo)
     * -- futureL(String)
     *
     * Grouped
     * 4) futureG
     * -- futureG(List, String)
     * -- futureG(T, String)
     * -- futureG(List)
     * -- futureG(T)
     *
     * Error Future
     * 5) futureE
     * -- futureE(T)
     * -- futureE(Supplier)
     *
     * Spec Data
     * 6) futureJA, futureB
     * -- futureJA(JsonArray)
     * -- futureB(JsonObject)
     * -- futureB(boolean)
     *
     * New Api for normalized mount
     * 7) futureN
     * -- futureN(JsonObject, JsonObject)
     * -- futureN(JsonArray, JsonArray)
     * -- futureN(JsonArray, JsonArray, String)
     * > N for normalize and add new field:  __data,  __flag instead of original data
     *
     * Combine JsonObject and JsonArray by index
     * 8) futureC
     *
     * Filter JsonObject and JsonArray
     * 9) futureF
     * -- futureF(String...)
     * -- futureF(Set<String>)
     * -- futureF(ClustSerializble, String...)
     * -- futureF(JsonArray, String...)
     * -- futureF(JsonObject, Set<String>)
     * -- futureF(JsonArray, Set<String>)
     */
    // ----------------------- futureF ----------------------

    public static <T extends ClusterSerializable> Function<T, Future<T>> futureF(final Set<String> removed) {
        return input -> (input instanceof JsonObject) ?
                futureF((JsonObject) input, removed).compose(json -> To.future((T) json)) :
                futureF((JsonArray) input, removed).compose(array -> To.future((T) array));
    }

    public static <T extends ClusterSerializable> Function<T, Future<T>> futureF(final String... removed) {
        return futureF(Arrays.stream(removed).collect(Collectors.toSet()));
    }

    public static Future<JsonObject> futureF(final JsonObject input, final String... removed) {
        return To.future(To.subset(input, Arrays.stream(removed).collect(Collectors.toSet())));
    }

    public static Future<JsonObject> futureF(final JsonObject input, final Set<String> removed) {
        return To.future(To.subset(input, removed));
    }

    public static Future<JsonArray> futureF(final JsonArray input, final String... removed) {
        return To.future(To.subset(input, Arrays.stream(removed).collect(Collectors.toSet())));
    }

    public static Future<JsonArray> futureF(final JsonArray input, final Set<String> removed) {
        return To.future(To.subset(input, removed));
    }

    // ----------------------- futureN ----------------------
    public static Future<JsonObject> futureN(final JsonObject input, final JsonObject previous, final JsonObject current) {
        return Norm.effect(input, previous, current);
    }

    public static Future<JsonArray> futureN(final JsonArray previous, final JsonArray current) {
        return Norm.effect(previous, current, KName.KEY);
    }

    public static Future<JsonArray> futureN(final JsonArray previous, final JsonArray current, final String field) {
        return Norm.effect(previous, current, field);
    }

    public static <T> Future<T> futureC(final T input, final T processed) {
        return Norm.combine(input, processed);
    }

    public static Future<Boolean> futureT() {
        return To.future(Boolean.TRUE);
    }

    public static <T> Future<Boolean> futureT(final T input) {
        return To.future(Boolean.TRUE);
    }

    public static Future<Boolean> futureF() {
        return To.future(Boolean.FALSE);
    }

    public static <T> Future<List<T>> futureL() {
        return future(new ArrayList<>());
    }

    public static <T> Future<List<T>> futureL(final T single) {
        final List<T> list = new ArrayList<>();
        list.add(single);
        return future(list);
    }

    public static Future<JsonObject> futureB(final boolean checked) {
        return To.future(outBool(checked));
    }

    public static Future<Boolean> futureB(final JsonObject checked) {
        return To.future(Async.bool(checked));
    }

    public static Future<JsonObject> futureJA(final JsonArray array) {
        return To.future(Async.array(array));
    }

    public static <T> Function<Throwable, Future<T>> futureE(final T input) {
        return Async.toErrorFuture(() -> input);
    }

    public static <T> Function<Throwable, Future<T>> futureE(final Supplier<T> supplier) {
        return Async.toErrorFuture(supplier);
    }

    public static <T> Future<JsonArray> futureA(final List<T> list, final String pojo) {
        return Future.succeededFuture(To.toJArray(list, pojo));
    }

    public static Future<JsonArray> futureA() {
        return futureA(new ArrayList<>(), Strings.EMPTY);
    }

    public static Future<JsonArray> futureA(Throwable ex) {
        return Async.<JsonArray>toErrorFuture(JsonArray::new).apply(ex);
    }

    public static <T> Future<JsonArray> futureA(final List<T> list) {
        return futureA(list, Strings.EMPTY);
    }

    public static <T> Function<List<T>, Future<JsonArray>> futureA(final String pojo) {
        return list -> futureA(list, pojo);
    }

    // --------------- T of entity processing -----------------

    public static <T> Future<JsonObject> futureJ(final T entity, final String pojo) {
        return Future.succeededFuture(To.toJObject(entity, pojo));
    }

    public static <T, R> Function<List<R>, Future<JsonObject>> futureJM(final T entity, final String field) {
        return list -> Future.succeededFuture(To.toMerge(entity, field, list));
    }

    public static Future<JsonObject> futureJ() {
        return futureJ(new JsonObject(), Strings.EMPTY);
    }

    public static Future<JsonObject> futureJ(Throwable ex) {
        return Async.<JsonObject>toErrorFuture(JsonObject::new).apply(ex);
    }

    public static <T> Future<JsonObject> futureJ(final T entity) {
        return futureJ(entity, Strings.EMPTY);
    }

    public static <T> Function<T, Future<JsonObject>> futureJ(final String pojo) {
        return entity -> futureJ(entity, pojo);
    }

    // --------------- List<T> of future processing -----------------
    public static <T> Future<List<JsonObject>> futureL(final List<T> list, final String pojo) {
        return Future.succeededFuture(To.toJList(list, pojo));
    }

    public static <T> Future<List<JsonObject>> futureLJ() {
        return futureL(new ArrayList<>(), Strings.EMPTY);
    }

    public static <T> Future<List<JsonObject>> futureL(final List<T> list) {
        return futureL(list, Strings.EMPTY);
    }

    public static <T> Function<List<T>, Future<List<JsonObject>>> futureL(final String pojo) {
        return list -> futureL(list, pojo);
    }

    // --------------- Record processing -----------------

    public static Future<JsonObject> futureJ(final Record record) {
        return Fn.getNull(futureJ(), () -> To.future(record.toJson()), record);
    }

    public static Future<JsonArray> futureA(final Record[] records) {
        return Fn.getNull(futureA(), () -> To.future(Ut.toJArray(records)), records);
    }

    // --------------- Future of Map -----------------
    public static <T> Future<ConcurrentMap<String, JsonArray>> futureG(final List<T> item, final String field) {
        return futureG(To.toJArray(item, ""), field);
    }

    public static Future<ConcurrentMap<String, JsonArray>> futureG(final JsonArray item, final String field) {
        return Future.succeededFuture(Ut.elementGroup(item, field));
    }

    public static <T> Future<ConcurrentMap<String, JsonArray>> futureG(final List<T> item) {
        return futureG(To.toJArray(item, ""), "type");
    }

    public static Future<ConcurrentMap<String, JsonArray>> futureG(final JsonArray item) {
        return futureG(item, "type");
    }

    /*
     * Channel Execution
     *
     * 1. channel
     * 2. channelS
     * 3. channelA
     */
    public static <T, O> Future<O> channel(final Class<T> clazz, final Supplier<O> supplier,
                                           final Function<T, Future<O>> executor) {
        return Async.channel(clazz, supplier, executor);
    }


    public static <T, O> O channelS(final Class<T> clazz, final Supplier<O> supplier,
                                    final Function<T, O> executor) {
        return Async.channelSync(clazz, supplier, executor);
    }

    public static <T, O> O channelS(final Class<T> clazz, final Function<T, O> executor) {
        return Async.channelSync(clazz, () -> null, executor);
    }

    public static <T, O> Future<O> channelA(final Class<T> clazz, final Supplier<Future<O>> supplier,
                                            final Function<T, Future<O>> executor) {
        return Async.channelAsync(clazz, supplier, executor);
    }

    /*
     * Flatting method for function executing
     * 1) attach -> JsonObject ( field )
     * 2) attachJ -> Advanced JsonObject ( field )
     */
    public static <T> Function<JsonObject, Future<JsonObject>> attach(final String field, final Function<T, Future<JsonObject>> function) {
        return Web.toAttach(field, function);
    }

    public static <T> Function<JsonObject, Future<JsonObject>> attachJ(final String field, final Function<T, Future<JsonObject>> function) {
        return Web.toAttachJ(field, function);
    }

    public static <T> Function<T, Future<JsonObject>> attachJ(final String field, final JsonObject data) {
        return Web.toAttachJ(field, data);
    }

    /*
     * Normalize pageData in framework
     * {
     *      "list": [],
     *      "count": xx
     * }
     * Normalize old/new data in framework
     */

    public static JsonObject pageData() {
        return Web.pageData(new JsonArray(), 0L);
    }

    public static JsonObject pageData(final JsonArray data, final long size) {
        return Web.pageData(data, size);
    }

    public static JsonArray pageData(final JsonObject data) {
        return Ut.valueJArray(data.getJsonArray(KName.LIST));
    }

    public static JsonObject pageData(final JsonObject pageData, final Function<JsonArray, JsonArray> function) {
        return Web.pageData(pageData, function);
    }

    public static Future<JsonObject> thenEffect(final JsonObject input, final BiFunction<JsonObject, JsonObject, Future<JsonObject>> executor) {
        return Norm.effectTabb(input, executor);
    }

    /*
     * JqTool Engine method
     * 1) whereDay
     * 2) whereAnd
     * 3) whereOr
     * 4) whereKeys
     */
    public static JsonObject whereDay(final JsonObject filters, final String field, final Instant instant) {
        return Where.whereDay(filters, field, instant);
    }

    public static JsonObject whereDay(final JsonObject filters, final String field, final LocalDateTime instant) {
        return Where.whereDay(filters, field, Ut.parse(instant).toInstant());
    }

    public static JsonObject whereKeys(final Set<String> keys) {
        return Where.whereKeys(Ut.toJArray(keys));
    }

    public static JsonObject whereKeys(final JsonArray keys) {
        return Where.whereKeys(keys);
    }

    public static JsonObject whereAnd() {
        return Where.whereAnd();
    }

    public static JsonObject whereAnd(final String field, final Object value) {
        return Where.whereAnd().put(field, value);
    }

    public static JsonObject whereOr() {
        return Where.whereOr();
    }

    public static JsonObject whereOr(final String field, final Object value) {
        return Where.whereOr().put(field, value);
    }

    // Where current condition is null
    /*
     * Query Engine API
     *
     * QH -> Query with Criteria   （全格式）
     * H  -> Criteria              （查询条件）
     * V  -> Projection            （列过滤）
     *
     * 1) irNil / irAnd / irOne
     *    - irNil:          判断查询条件是否为空，"": true | false 单节点也为空
     *    - irOp:           判断查询条件是 AND 还是 OR，AND返回true，OR返回false
     *    - irOne:          判断查询条件是否单条件（只有一个条件）
     * 2) irAndQH / irAndH
     *    - irAndQH:        参数本身为全格式:
     *      {
     *           "criteria": {}
     *      }
     *    - irAndH:         参数本身为全格式中的 criteria 纯格式
     * 3) irQV / irV
     *    - irQV:           参数本身为全格式:
     *      {
     *           "projection": []
     *      }
     *    - irV:            参数本身为全格式中的 projection 纯格式
     *
     * 全格式返回全格式，纯格式返回纯格式
     */
    public static boolean irNil(final JsonObject condition) {
        return Query.irNil(condition);
    }

    public static boolean irOp(final JsonObject condition) {
        return Query.irAnd(condition);
    }

    public static boolean irOne(final JsonObject condition) {
        return Query.irOne(condition);
    }

    // ---------------------- Qr Modification --------------------------
    public static JsonObject irAndQH(final JsonObject qr, final Kv<String, Object> kv) {
        Objects.requireNonNull(kv);
        return Query.irQH(qr, kv.getKey(), kv.getValue());
    }

    public static JsonObject irAndQH(final JsonObject qr, final String field, final Object value) {
        return Query.irQH(qr, field, value);
    }

    public static JsonObject irAndQH(final JsonObject query, final JsonObject criteria, final boolean clear) {
        return Query.irQH(query, criteria, clear);
    }

    public static JsonObject irAndH(final JsonObject original, final JsonObject criteria) {
        return Query.irH(original, criteria);
    }

    public static JsonObject irAndH(final JsonObject original, final String field, final Object value) {
        return Query.irH(original, field, value);
    }

    public static JsonObject irAndH(final JsonObject original, final Kv<String, Object> kv) {
        Objects.requireNonNull(kv);
        return Query.irH(original, kv.getKey(), kv.getValue());
    }

    // Qr Combine ( projection + projection )
    public static JsonObject irQV(final JsonObject query, final JsonArray projection, final boolean clear) {
        return Query.irQV(query, projection, clear);
    }

    public static JsonArray irV(final JsonArray original, final JsonArray projection) {
        return Query.irV(original, projection);
    }

    // ---------------------- Request Data Extract --------------------------
    // -> Message<Envelop> -> T ( Interface mode )

    public static JsonArray getArray(final Envelop envelop, final int index) {
        return In.request(envelop, index, JsonArray.class);
    }

    // -> Message<Envelop> -> T ( Interface mode )
    public static JsonArray getArray(final Envelop envelop) {
        return In.request(envelop, 0, JsonArray.class);
    }

    // -> Message<Envelop> -> T ( Interface mode )

    public static JsonArray getArray1(final Envelop envelop) {
        return In.request(envelop, 1, JsonArray.class);
    }

    // -> Message<Envelop> -> T ( Interface mode )

    public static JsonArray getArray2(final Envelop envelop) {
        return In.request(envelop, 2, JsonArray.class);
    }

    // -> Message<Envelop> -> String ( Interface mode )

    public static String getString(final Envelop envelop, final int index) {
        return In.request(envelop, index, String.class);
    }

    // -> Message<Envelop> -> String ( Interface mode )
    public static String getString(final Envelop envelop) {
        return In.request(envelop, 0, String.class);
    }

    // -> Message<Envelop> -> String ( Interface mode )
    public static String getString1(final Envelop envelop) {
        return In.request(envelop, 1, String.class);
    }

    // -> Message<Envelop> -> String ( Interface mode )
    public static String getString2(final Envelop envelop) {
        return In.request(envelop, 2, String.class);
    }

    // -> Message<Envelop> -> String ( Interface mode )

    public static Vis getVis(final Envelop envelop, final int index) {
        return In.request(envelop, index, Vis.class);
    }

    // -> Message<Envelop> -> String ( Interface mode )
    public static Vis getVis(final Envelop envelop) {
        return In.request(envelop, 0, Vis.class);
    }

    // -> Message<Envelop> -> String ( Interface mode )
    public static Vis getVis1(final Envelop envelop) {
        return In.request(envelop, 1, Vis.class);
    }

    // -> Message<Envelop> -> String ( Interface mode )
    public static Vis getVis2(final Envelop envelop) {
        return In.request(envelop, 2, Vis.class);
    }

    // -> Message<Envelop> -> JsonObject ( Interface mode )
    public static JsonObject getJson(final Envelop envelop, final int index) {
        return In.request(envelop, index, JsonObject.class);
    }
    // -> Message<Envelop> -> JsonObject ( Interface mode )

    public static <T> T fromEnvelop(final Envelop envelop, final Class<T> clazz, final String pojo) {
        return From.fromJson(getJson(envelop), clazz, pojo);
    }

    public static JsonObject fromEnvelop(final Envelop envelop, final String pojo) {
        return From.fromJson(Ux.getJson(envelop), pojo);
    }

    public static JsonObject getJson(final Envelop envelop) {
        return In.request(envelop, 0, JsonObject.class);
    }

    // -> Message<Envelop> -> JsonObject ( Interface mode )

    public static <T> T fromEnvelop1(final Envelop envelop, final Class<T> clazz, final String pojo) {
        return From.fromJson(getJson1(envelop), clazz, pojo);
    }

    public static JsonObject fromEnvelop1(final Envelop envelop, final String pojo) {
        return From.fromJson(Ux.getJson1(envelop), pojo);
    }

    public static JsonObject getJson1(final Envelop envelop) {
        return In.request(envelop, 1, JsonObject.class);
    }

    // -> Message<Envelop> -> JsonObject ( Interface mode )

    public static JsonObject getJson2(final Envelop envelop) {
        return In.request(envelop, 2, JsonObject.class);
    }

    public static JsonObject fromEnvelop2(final Envelop envelop, final String pojo) {
        return From.fromJson(Ux.getJson2(envelop), pojo);
    }

    public static <T> T fromEnvelop2(final Envelop envelop, final Class<T> clazz, final String pojo) {
        return From.fromJson(getJson2(envelop), clazz, pojo);
    }

    // -> Message<Envelop> -> Integer ( Interface mode )

    public static Integer getInteger(final Envelop envelop, final int index) {
        return In.request(envelop, index, Integer.class);
    }

    public static Integer getInteger(final Envelop envelop) {
        return In.request(envelop, 0, Integer.class);
    }

    // -> Message<Envelop> -> Integer ( Interface mode )
    public static Integer getInteger1(final Envelop envelop) {
        return In.request(envelop, 1, Integer.class);
    }

    // -> Message<Envelop> -> Integer ( Interface mode )
    public static Integer getInteger2(final Envelop envelop) {
        return In.request(envelop, 2, Integer.class);
    }

    // -> Message<Envelop> -> Long ( Interface mode )

    public static Long getLong(final Envelop envelop, final int index) {
        return In.request(envelop, index, Long.class);
    }

    // -> Message<Envelop> -> Long ( Interface mode )
    public static Long getLong(final Envelop envelop) {
        return In.request(envelop, 0, Long.class);
    }

    // -> Message<Envelop> -> Long ( Interface mode )
    public static Long getLong1(final Envelop envelop) {
        return In.request(envelop, 1, Long.class);
    }

    // -> Message<Envelop> -> Long ( Interface mode )
    public static Long getLong2(final Envelop envelop) {
        return In.request(envelop, 2, Long.class);
    }

    // -> Message<Envelop> -> T ( Interface mode )

    public static <T> T getT(final Envelop envelop, final int index, final Class<T> clazz) {
        return In.request(envelop, index, clazz);
    }

    // -> Message<Envelop> -> T ( Interface mode )

    public static <T> T getT(final Envelop envelop, final Class<T> clazz) {
        return In.request(envelop, 0, clazz);
    }

    // -> Message<Envelop> -> T ( Interface mode )

    public static <T> T getT1(final Envelop envelop, final Class<T> clazz) {
        return In.request(envelop, 1, clazz);
    }

    // -> Message<Envelop> -> T ( Interface mode )

    public static <T> T getT2(final Envelop envelop, final Class<T> clazz) {
        return In.request(envelop, 2, clazz);
    }

    // ---------------------- Agent mode usage --------------------------
    // -> Message<Envelop> -> JsonObject ( Agent mode )
    // -> Envelop -> JsonObject ( Agent mode )
    public static JsonObject getBody(final Envelop envelop) {
        return In.request(envelop, JsonObject.class);
    }

    // -> Envelop -> T ( Agent mode )
    public static <T> T getBodyT(final Envelop envelop, final Class<T> clazz) {
        return In.request(envelop, clazz);
    }

    // ---------------------- Agent mode usage --------------------------

    /**
     * This method will be configured in `vertx-extension.yml` file in common situation,
     * The file content should be as following:
     * <p>
     * ```yml
     * // <pre><code>
     * init:
     *   - component: "[ComponentName1]"
     *   - component: "[ComponentName2]"
     * // </code></pre>
     * ```
     * <p>
     * All components here will be called when container starting, the component must declare the init method as
     * <p>
     * ```java
     * // <pre><code>
     * public static void init(){
     *     // Here are initialize code logical
     *     Ke.banner("「Εκδήλωση」- Crud ( Ix )");
     *
     *     Ix.infoInit(LOGGER, "IxConfiguration...");
     * }
     * // </code></pre>
     * ```
     * <p>
     * This method should be used when you want to develop zero extension module for business requirement.
     *
     * @param init The configuration data came from `init` node in file
     */
    public static Future<Boolean> nativeInit(final JsonArray components, final Vertx vertx) {
        return Atomic.nativeInit(components, vertx);
    }

    public static Vertx nativeVertx() {
        return Atomic.nativeVertx();
    }

    public static WorkerExecutor nativeWorker(final String name) {
        return Atomic.nativeWorker(name, 10);
    }

    public static <T> Future<T> nativeWorker(final String name, final Handler<Promise<T>> handler) {
        return Atomic.nativeWorker(name, handler);
    }

    // -> Dict for caculation
    /*
     * Keep following dict method
     */
    public static ConcurrentMap<String, DConsumer> dictEpsilon(final JsonObject epsilon) {
        return DConsumer.mapEpsilon(epsilon);
    }

    public static Future<ConcurrentMap<String, JsonArray>> dictCalc(final DSetting dict, final MultiMap paramsMap) {
        return DiTool.dictCalc(dict, paramsMap);
    }

    public static <T> Future<T> dictTo(final T record, final DFabric fabric) {
        return DiTool.dictTo(record, fabric);
    }

    /*
     * key part for extract data from environment
     */
    public static String keyUser(final User user) {
        Objects.requireNonNull(user);
        final JsonObject principle = user.principal();
        if (principle.containsKey(KName.USER)) {
            return principle.getString(KName.USER);
        } else {
            /*
             * To avoid fetch user information from JWT token
             */
            final String accessToken = principle.getString(KName.ACCESS_TOKEN);
            final JsonObject credential = Jwt.extract(accessToken);
            return credential.getString(KName.USER);
        }
    }

    // region Deprecated Code ( Removed in future, plan in release 1.0 version. )

    @Deprecated
    public static Future<JsonArray> thenCombine(final Future<JsonArray> source, final Function<JsonObject, Future<JsonObject>> generateFun, final BinaryOperator<JsonObject> operatorFun) {
        // return Fn.combine(source, generateFun, operatorFun);
        throw new RuntimeException("「Version 0.9+ Removed」 Fn.combine(source, generateFun, operatorFun) instead!!");

    }

    @Deprecated
    public static Future<JsonObject> thenCombine(final JsonObject source, final Function<JsonObject, List<Future>> generateFun, final BiConsumer<JsonObject, JsonObject>... operatorFun) {
        // return Fn.combine(source, generateFun, operatorFun);
        throw new RuntimeException("「Version 0.9+ Removed」 Fn.combine(source, generateFun, operatorFun) instead!!");
    }

    @Deprecated
    public static <T> Future<T> thenError(final Class<? extends WebException> clazz, final Object... args) {
        // return Fn.thenError(clazz, args);
        throw new RuntimeException("「Version 0.9+ Removed」 Fn.thenError(clazz, args) instead!!");
    }

    @Deprecated
    public static <T> Future<T> thenError(final Class<?> clazz, final String sigma, final Supplier<Future<T>> supplier) {
        // return Fn.thenError(clazz, sigma, supplier);
        throw new RuntimeException("「Version 0.9+ Removed」 Fn.thenError(clazz, sigma, supplier) instead!!");
    }

    @Deprecated
    public static Future<JsonArray> thenCombine(final List<Future<JsonObject>> futures) {
        // return Fn.combine(futures);
        throw new RuntimeException("「Version 0.9+ Removed」 Fn.combine(futures) instead!!");
    }

    @Deprecated
    public static <F, S, T> Future<T> thenCombine(final Supplier<Future<F>> futureF, final Supplier<Future<S>> futureS,
                                                  final BiFunction<F, S, Future<T>> consumer) {
        // return Fn.combine(futureF, futureS, consumer);
        throw new RuntimeException("「Version 0.9+ Removed」 Fn.combine(futureF, futureS, consumer) instead!!");
    }

    @Deprecated
    public static <F, S, T> Future<T> thenCombine(final Future<F> futureF, final Future<S> futureS,
                                                  final BiFunction<F, S, Future<T>> consumer) {
        // return Fn.combine(futureF, futureS, consumer);
        throw new RuntimeException("「Version 0.9+ Removed」 Fn.combine(futureF, futureS, consumer) instead!!");
    }

    @Deprecated
    public static Future<JsonArray> thenCombine(final JsonArray input, final Function<JsonObject, Future<JsonObject>> function) {
        // return Fn.combine(input, function);
        throw new RuntimeException("「Version 0.9+ Removed」 Fn.combine(input, function) instead!!");
    }

    @Deprecated
    public static <I, O> Future<List<O>> thenCombineT(final List<I> source, final Function<I, Future<O>> consumer) {
        // return Fn.combineT(source, consumer);
        throw new RuntimeException("「Version 0.9+ Removed」 Fn.combineT(source, consumer) instead!!");
    }

    @Deprecated
    public static <T> Future<List<T>> thenCombineT(final List<Future<T>> futures) {
        // return Fn.combineT(futures);
        throw new RuntimeException("「Version 0.9+ Removed」 Fn.combineT(futures) instead!!");
    }

    @Deprecated
    public static <K, T> Future<ConcurrentMap<K, T>> thenCombine(final ConcurrentMap<K, Future<T>> futureMap) {
        throw new RuntimeException("「Version 0.9+ Removed」 Fn.combine(futureMap) instead!!");
    }

    @Deprecated
    /*
     * Specific combine method here.
     */
    public static Future<JsonArray> thenCombineArray(final List<Future<JsonArray>> futures) {
        throw new RuntimeException("「Version 0.9+ Removed」 Fn.combineA(futures) instead!!");
    }

    @Deprecated
    public static Future<JsonArray> thenCombineArray(final JsonArray source, final Function<JsonObject, Future<JsonArray>> consumer) {
        throw new RuntimeException("「Version 0.9+ Removed」 Fn.combineA(source, consumer) instead!!");
    }

    @Deprecated
    public static <T> Future<JsonArray> thenCombineArray(final JsonArray source, final Class<T> clazz, final Function<T, Future<JsonArray>> consumer) {
        throw new RuntimeException("「Version 0.9+ Removed」 Fn.combineA(source, clazz, consumer) instead!!");
    }

    @Deprecated
    public static <T> Future<List<T>> thenCombineArrayT(final List<Future<List<T>>> futures) {
        throw new RuntimeException("「Version 0.9+ Removed」 Fn.combineL(...) instead!!");
    }

    @Deprecated
    public static Future<ConcurrentMap<String, JsonArray>> thenCompress(final List<Future<ConcurrentMap<String, JsonArray>>> futures) {
        throw new RuntimeException("「Version 0.9+ Removed」 Fn.compress(...) instead!!");
    }
    // endregion

    // ---------------------------------- Children Utility

    /**
     * Inner class of `Jooq` tool of Jooq Engine operations based on pojo here.
     * When developers want to access database and select zero default implementation.
     *
     * @author lang
     * <pre><code>
     *
     *     public Future<JsonObject> fetchAsync(final User user){
     *         return Ux.Jooq.on(UserDao.class)
     *                    .insertAsync(user)
     *                    .compose(Ux::fnJObject);
     *     }
     *
     * </code></pre>
     * <p>
     * Here you can do database access smartly and do nothing then.
     */
    public static class Jooq {

        /**
         * Get reference of UxJooq that bind to Dao class, this method won't access standard database,
         * instead it will access history database that has been configured in `vertx-jooq.yml` file.
         * <p>
         * key = orbit
         *
         * <pre><code>
         *
         * jooq:
         *   orbit:
         *     driverClassName: "com.mysql.cj.jdbc.Driver"
         *     ......
         *
         * </code></pre>
         *
         * @param clazz The class of `VertxDao` that has been generated by jooq tool
         * @return UxJooq reference that has been initialized
         */
        public static UxJooq ons(final Class<?> clazz) {
            final JooqDsl dsl = JooqInfix.getDao(clazz, Constants.DEFAULT_JOOQ_HISTORY);
            return Cache.CC_JOOQ_HIS.pick(() -> new UxJooq(clazz, dsl), dsl.poolKey());
            // return Fn.po?lThread(Cache.JOOQ_POOL_HIS, () -> new UxJooq(clazz, dsl), dsl.poolKey());
        }

        /**
         * Get reference of UxJooq that bind to Dao class, this method access standard database,
         * the configured position is `vertx-jooq.yml`
         * <p>
         * key = provider
         *
         * <pre><code>
         * jooq:
         *   provider:
         *     driverClassName: "com.mysql.cj.jdbc.Driver"
         * </code></pre>
         *
         * @param clazz The class of `VertxDao` that has been generated by jooq tool
         * @return UxJooq reference that has been initialized
         */
        public static UxJooq on(final Class<?> clazz) {
            final JooqDsl dsl = JooqInfix.getDao(clazz);
            return Cache.CC_JOOQ.pick(() -> new UxJooq(clazz, dsl), dsl.poolKey());
            // return Fn.po?lThread(Cache.JOOQ_POOL, () -> new UxJooq(clazz, dsl), dsl.poolKey());
        }

        /**
         * The overloading method of above `on(Class<?>)` method here.
         *
         * @param clazz The class of `VertxDao` that has been generated by jooq tool
         * @param pool  Input data pool reference, it provide developers to access other database in one application.
         * @return UxJooq reference that has been initialized
         */
        public static UxJooq on(final Class<?> clazz, final DataPool pool) {
            final JooqDsl dsl = JooqInfix.getDao(clazz, pool);
            return Cache.CC_JOOQ.pick(() -> new UxJooq(clazz, dsl), dsl.poolKey());
            // return Fn.po?lThread(Cache.JOOQ_POOL, () -> new UxJooq(clazz, dsl), dsl.poolKey());
        }

        /**
         * The overloading method of above `on(Class<?>)` method here.
         *
         * @param clazz The class of `VertxDao` that has been generated by jooq tool
         * @param key   the key configuration in vertx-jooq.yml such as above "orbit", "provider"
         * @return UxJooq reference that has been initialized
         */
        public static UxJooq on(final Class<?> clazz, final String key) {
            final JooqDsl dsl = JooqInfix.getDao(clazz, key);
            return Cache.CC_JOOQ.pick(() -> new UxJooq(clazz, dsl), dsl.poolKey());
            // return Fn.po?lThread(Cache.JOOQ_POOL, () -> new UxJooq(clazz, dsl), dsl.poolKey());
        }
    }

    // -> Jooq -> Multi
    public static class Join {

        public static UxJoin on(final String configFile) {
            return new UxJoin(configFile);
        }

        public static UxJoin on() {
            return new UxJoin(null);
        }

        public static UxJoin on(final Class<?> daoCls) {
            return new UxJoin(null).add(daoCls);
        }
    }

    public static class Pool {

        public static UxPool on(final String name) {
            return Cache.CC_UX_POOL.pick(() -> new UxPool(name), name);
            // return Fn.po?l(Cache.MAP_POOL, name, () -> new UxPool(name));
        }

        public static UxPool on() {
            return new UxPool();
        }
    }

    public static class Job {
        public static UxJob on() {
            return new UxJob();
        }
    }

    public static class Opt {
        public static UxOpt on() {
            return new UxOpt();
        }
    }

    public static class Timer {
        public static UxTimer on() {
            return new UxTimer();
        }
    }

    /*
     * Here the Jwt class is for lagency system because all following method will be called and existed
     * But the new structure is just like following:
     *
     * 1. Lee -> ( Impl ) by Service Loader
     * 2. The `AuthWall.JWT` will be selected and called API of Lee interface
     * 3. The final result is token ( encoding / decoding ) part
     * 4. The implementation class is defined in `zero-ifx-auth` instead of standard framework
     *
     * If you want to use security module, you should set-up `zero-ifx-auth` infix instead, or
     * you can run zero framework in non-secure mode
     */
    public static class Jwt {

        public static String token(final JsonObject data) {
            final Lee lee = Ut.service(LeeBuiltIn.class);
            return lee.encode(data, AegisItem.configMap(AuthWall.JWT));
        }

        public static JsonObject extract(final String token) {
            final Lee lee = Ut.service(LeeBuiltIn.class);
            return lee.decode(token, AegisItem.configMap(AuthWall.JWT));
        }

        public static JsonObject extract(final JsonObject jwtToken) {
            return extract(jwtToken.getString(KName.ACCESS_TOKEN));
        }
    }

}
