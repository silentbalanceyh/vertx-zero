package io.vertx.up.uca.jooq;

import io.horizon.eon.VString;
import io.horizon.eon.VValue;
import io.horizon.eon.em.EmDS;
import io.horizon.uca.log.Annal;
import io.horizon.uca.qr.Sorter;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.plugin.jooq.JooqDsl;
import io.vertx.up.uca.jooq.util.JqCond;
import io.vertx.up.uca.jooq.util.JqFlow;
import io.vertx.up.uca.jooq.util.JqTool;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiPredicate;

@SuppressWarnings("all")
public final class UxJooq {

    private static final Annal LOGGER = Annal.get(UxJooq.class);

    private transient final Class<?> clazz;
    /* Analyzer */
    private transient final JqAnalyzer analyzer;
    /* Aggre */
    private transient final JqAggregator aggregator;
    /* Writer */
    private transient final JqWriter writer;
    /* Reader */
    private transient final JqReader reader;
    /*
     * New Structure for usage
     */
    private transient final JqFlow workflow;
    private transient EmDS.Format format = EmDS.Format.JSON;

    public <T> UxJooq(final Class<T> clazz, final JooqDsl dsl) {
        /* New exception to avoid programming missing */
        this.clazz = clazz;

        /* Analyzing column for Jooq */
        this.analyzer = JqAnalyzer.create(dsl);
        this.aggregator = JqAggregator.create(this.analyzer);

        /* Reader connect Analayzer */
        this.reader = JqReader.create(this.analyzer);

        /* Writer connect Reader */
        this.writer = JqWriter.create(this.analyzer);

        /* New Structure */
        this.workflow = JqFlow.create(this.analyzer);
    }

    // -------------------- Bind Config --------------------

    /*
     * Bind configuration range here
     * Pojo mode of complex processing
     */
    public UxJooq on(final String pojo) {
        this.analyzer.on(pojo, this.clazz);
        /*
         * Because the JqAnalyzer has been changed here, instead we should
         * re-create the workflow component.
         *
         * This feature is used in old system of previous version,
         * 1) The entity is correct in our system and no error here.
         * 2) There exists `pojo` file that bind to this entity for data conversation
         * 3) The `pojo` will impact all the interface API that contains `pojo` parameters
         */
        this.workflow.on(this.analyzer);
        return this;
    }

    /**
     * 条件压缩器
     */
    public JsonObject compress(final JsonObject criteria) {
        return JqCond.compress(criteria);
    }

    public UxJooq on(final EmDS.Format format) {
        this.format = format;
        return this;
    }

    public JqAnalyzer analyzer() {
        return this.analyzer;
    }

    public Set<String> columns() {
        return this.analyzer.columns().keySet();
    }

    public String table() {
        return this.analyzer.table().getName();
    }

    private JsonObject andOr(final JsonObject criteria) {
        if (!criteria.containsKey(VString.EMPTY)) {
            criteria.put(VString.EMPTY, Boolean.TRUE);
        }
        return criteria;
    }

    // -------------------- INSERT --------------------
    /*
     * Async Only
     * Disabled increament primary key processing, this method is not used in our system once,
     * In distributed system environment, it's no usage.
     *
     * public <T> Future<T> insertReturningPrimaryAsync(final T entity, final Consumer<Long> consumer) {
     *    return this.writer.insertReturningPrimaryAsync(entity, consumer);
     * }
     */
    /*
     * insertAsync(T)
     *      <-- insertAsync(JsonObject)
     *      <-- insertAsync(JsonObject,pojo)
     *      <-- insertJAsync(T)
     *      <-- insertJAsync(JsonObject, pojo)
     *      <-- insertJAsync(JsonObject)
     * */
    public <T> Future<T> insertAsync(final T entity) {
        return this.writer.insertAsync(entity);
    }

    public <T> Future<T> insertAsync(final JsonObject data) {
        return this.workflow.<T>inputAsync(data).compose(this::insertAsync);
    }

    public <T> Future<T> insertAsync(final JsonObject data, final String pojo) {
        return JqFlow.create(this.analyzer, pojo).<T>inputAsync(data).compose(this::insertAsync);
    }

    public <T> Future<JsonObject> insertJAsync(final T entity) {
        return this.insertAsync(entity).compose(this.workflow::outputAsync);
    }

    public <T> Future<JsonObject> insertJAsync(final JsonObject data) {
        return this.workflow.<T>inputAsync(data).compose(this::insertAsync).compose(this.workflow::outputAsync);
    }

    public <T> Future<JsonObject> insertJAsync(final JsonObject data, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return flow.<T>inputAsync(data).compose(this::insertAsync).compose(flow::outputAsync);
    }

    /*
     * insert(T)
     *      <-- insert(JsonObject)
     *      <-- insert(JsonObject, pojo)
     *      <-- insertJ(T)
     *      <-- insertJ(JsonObject)
     *      <-- insertJ(JsonObject, pojo)
     * */
    public <T> T insert(final T entity) {
        return this.writer.insert(entity);
    }

    public <T> T insert(final JsonObject data) {
        return this.insert((T) this.workflow.input(data));
    }

    public <T> T insert(final JsonObject data, final String pojo) {
        return this.insert((T) JqFlow.create(this.analyzer, pojo).input(data));
    }

    public <T> JsonObject insertJ(final T entity) {
        return this.workflow.output(this.insert(entity));
    }

    public <T> JsonObject insertJ(final JsonObject data) {
        return this.workflow.output(this.insert((T) this.workflow.input(data)));  // T & List<T> Diff
    }

    public <T> JsonObject insertJ(final JsonObject data, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return flow.output(this.insert((T) flow.input(data)));          // T & List<T> Diff
    }

    /*
     * insertAsync(List<T>)
     *      <-- insertAsync(JsonArray)
     *      <-- insertAsync(JsonArray, pojo)
     *      <-- insertJAsync(List<T>)
     *      <-- insertJAsync(JsonArray)
     *      <-- insertJAsync(JsonArray, pojo)
     */
    public <T> Future<List<T>> insertAsync(final List<T> entities) {
        return this.writer.insertAsync(entities);
    }

    public <T> Future<List<T>> insertAsync(final JsonArray input) {
        return this.workflow.<T>inputAsync(input).compose(this::insertAsync);          // --> `insertAsync(List<T>)`
    }

    public <T> Future<List<T>> insertAsync(final JsonArray input, final String pojo) {
        return JqFlow.create(this.analyzer, pojo).<T>inputAsync(input).compose(this::insertAsync);         // --> `insertAsync(List<T>)`
    }

    public <T> Future<JsonArray> insertJAsync(final List<T> list) {
        return this.insertAsync(list).compose(this.workflow::outputAsync);
    }

    public <T> Future<JsonArray> insertJAsync(final JsonArray input) {
        return this.workflow.<T>inputAsync(input).compose(this::insertAsync).compose(this.workflow::outputAsync);
    }

    public <T> Future<JsonArray> insertJAsync(final JsonArray input, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return flow.<T>inputAsync(input).compose(this::insertAsync).compose(flow::outputAsync);
    }

    /*
     * insert(List<T>)
     *      <-- insert(JsonArray)
     *      <-- insert(JsonArray,pojo)
     *      <-- insertJ(List<T>)
     *      <-- insertJ(JsonArray)
     *      <-- insertJ(JsonArray, pojo)
     */
    public <T> List<T> insert(final List<T> entities) {
        return this.writer.insert(entities);
    }

    public <T> List<T> insert(final JsonArray data) {
        return this.insert(this.workflow.input(data));
    }

    public <T> List<T> insert(final JsonArray data, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return this.insert(flow.input(data));
    }

    public <T> JsonArray insertJ(final List<T> list) {
        return this.workflow.output(this.insert(list));
    }

    public JsonArray insertJ(final JsonArray data) {
        return this.workflow.output(this.insert(this.workflow.input(data)));
    }

    public <T> JsonArray insertJ(final JsonArray data, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return flow.<T>output(this.insert(flow.input(data)));
    }

    // -------------------- Search Operation -----------
    /*
     * searchAsync(JsonObject, pojo)
     * searchAsync(JsonObject)
     * search(JsonObject, pojo)
     * search(JsonObject)
     */
    public Future<JsonObject> searchAsync(final JsonObject params, final String pojo) {
        return this.reader.searchAsync(params, JqFlow.create(this.analyzer, pojo));
    }

    public Future<JsonObject> searchAsync(final JsonObject params) {
        return this.reader.searchAsync(params, this.workflow);
    }

    public JsonObject search(final JsonObject params, final String pojo) {
        return this.reader.search(params, JqFlow.create(this.analyzer, pojo));
    }

    public JsonObject search(final JsonObject params) {
        return this.reader.search(params, this.workflow);
    }

    // -------------------- Fetch List -------------------
    /*
     * fetchAllAsync()
     *      <-- fetchJAllAsync()
     *      <-- fetchJAllAsync(pojo)
     *
     * fetchAll()
     *      <-- fetchJAll()
     *      <-- fetchJAll(pojo)
     *
     * fetchAsync(String, Object)
     *      <-- fetchJAsync(String, Object)
     *      <-- fetchInAsync(String, Object...)
     *      <-- fetchInAsync(String, Collection)
     *      <-- fetchInAsync(String, JsonArray)
     *      <-- fetchJInAsync(String, Object...)
     *      <-- fetchJInAsync(String, Collection)
     *      <-- fetchJInAsync(String, JsonArray)
     * fetch(String, Object)
     *      <-- fetchJ(String, Object)
     *      <-- fetchIn(String, Object...)
     *      <-- fetchIn(String, Collection)
     *      <-- fetchIn(String, JsonArray)
     *      <-- fetchJIn(String, Object...)
     *      <-- fetchJIn(String, Collection)
     *      <-- fetchJIn(String, JsonArray)
     *
     * fetchAsync(JsonObject)
     *      <-- fetchJAsync(JsonObject)
     *      <-- fetchAndAsync(JsonObject)
     *      <-- fetchJAndAsync(JsonObject)
     *      <-- fetchOrAsync(JsonObject)
     *      <-- fetchJOrAsync(JsonObject)
     *
     * fetchAsync(JsonObject, pojo)
     *      <-- fetchJAsync(JsonObject, pojo)
     *      <-- fetchAndAsync(JsonObject, pojo)
     *      <-- fetchJAndAsync(JsonObject, pojo)
     *      <-- fetchOrAsync(JsonObject, pojo)
     *      <-- fetchJOrAsync(JsonObject, pojo)
     *
     * fetch(JsonObject)
     *      <-- fetchJ(JsonObject)
     *      <-- fetchAnd(JsonObject)
     *      <-- fetchJAnd(JsonObject)
     *      <-- fetchOr(JsonObject)
     *      <-- fetchJOr(JsonObject)
     *
     * fetch(JsonObject, pojo)
     *      <-- fetchJ(JsonObject, pojo)
     *      <-- fetchAnd(JsonObject, pojo)
     *      <-- fetchJAnd(JsonObject, pojo)
     *      <-- fetchOr(JsonObject, pojo)
     *      <-- fetchJOr(JsonObject, pojo)
     */
    /* fetchAllAsync() */
    public <T> Future<List<T>> fetchAllAsync() {
        return this.reader.fetchAllAsync();
    }

    public Future<JsonArray> fetchJAllAsync() {
        return this.fetchAllAsync().compose(this.workflow::outputAsync);
    }

    public Future<JsonArray> fetchJAllAsync(final String pojo) {
        return this.fetchAllAsync().compose(JqFlow.create(this.analyzer, pojo)::outputAsync);
    }

    /* fetchAll() */
    public <T> List<T> fetchAll() {
        return this.reader.fetchAll();
    }

    public JsonArray fetchJAll() {
        return this.workflow.output(this.fetchAll());
    }

    public JsonArray fetchJAll(final String pojo) {
        return JqFlow.create(this.analyzer, pojo).output(this.fetchAll());
    }

    /* fetchAsync(String, Object) */
    public <T> Future<List<T>> fetchAsync(final String field, final Object value) {
        return this.reader.fetchAsync(field, value);
    }

    public <T> Future<List<T>> fetchInAsync(final String field, final Object... value) {
        return this.fetchAsync(field, Arrays.asList(value));
    }

    public <T> Future<List<T>> fetchInAsync(final String field, final JsonArray values) {
        return this.fetchAsync(field, values.getList());
    }

    public <T, K> Future<List<T>> fetchInAsync(final String field, final Collection<K> collection) {
        return this.fetchAsync(field, collection);
    }

    public Future<JsonArray> fetchJAsync(final String field, final Object value) {
        return this.fetchAsync(field, value).compose(this.workflow::outputAsync);
    }

    public Future<JsonArray> fetchJInAsync(final String field, final Object... value) {
        return this.fetchAsync(field, Arrays.asList(value)).compose(this.workflow::outputAsync);
    }

    public Future<JsonArray> fetchJInAsync(final String field, final JsonArray values) {
        return this.fetchAsync(field, values.getList()).compose(this.workflow::outputAsync);
    }

    public <K> Future<JsonArray> fetchJInAsync(final String field, final Collection<K> collection) {
        return this.fetchAsync(field, collection).compose(this.workflow::outputAsync);
    }

    /* fetch(String, Object) */
    public <T> List<T> fetch(final String field, final Object value) {
        return this.reader.fetch(field, value);
    }

    public <T> List<T> fetchIn(final String field, final Object... values) {
        return this.fetch(field, Arrays.asList(values));
    }

    public <T> List<T> fetchIn(final String field, final JsonArray values) {
        return this.fetch(field, values.getList());
    }

    public <T, K> List<T> fetchIn(final String field, final Collection<K> collection) {
        return this.fetch(field, collection);
    }

    public JsonArray fetchJ(final String field, final Object value) {
        return this.workflow.output(this.fetch(field, value));
    }

    public JsonArray fetchJIn(final String field, final Object... values) {
        return this.workflow.output(this.fetch(field, Arrays.asList(values)));
    }

    public JsonArray fetchJIn(final String field, final JsonArray values) {
        return this.workflow.output(this.fetch(field, values.getList()));
    }

    public <K> JsonArray fetchJIn(final String field, final Collection<K> collection) {
        return this.workflow.output(this.fetch(field, collection));
    }

    /*
     * fetchAsync(JsonObject)
     * fetchAsync(JsonObject, Sorter)
     **/
    public <T> Future<List<T>> fetchAsync(final JsonObject criteria) {
        return this.workflow.inputQrJAsync(criteria).compose(this.reader::fetchAsync);
    }

    public <T> Future<List<T>> fetchAndAsync(final JsonObject criteria) {
        return this.fetchAsync(criteria.put(VString.EMPTY, Boolean.TRUE));
    }

    public <T> Future<List<T>> fetchOrAsync(final JsonObject criteria) {
        return this.fetchAsync(criteria.put(VString.EMPTY, Boolean.FALSE));
    }

    public Future<JsonArray> fetchJAsync(final JsonObject criteria) {
        return this.fetchAsync(criteria).compose(this.workflow::outputAsync);
    }

    public Future<JsonArray> fetchJAndAsync(final JsonObject criteria) {
        return this.fetchAsync(criteria.put(VString.EMPTY, Boolean.TRUE)).compose(this.workflow::outputAsync);
    }

    public Future<JsonArray> fetchJOrAsync(final JsonObject criteria) {
        return this.fetchAsync(criteria.put(VString.EMPTY, Boolean.FALSE)).compose(this.workflow::outputAsync);
    }

    public <T> Future<List<T>> fetchAsync(final JsonObject criteria, final Sorter sorter) {
        return this.workflow.inputQrJAsync(criteria).compose(qr -> this.reader.fetchAsync(qr, sorter));
    }


    public <T> Future<List<T>> fetchAndAsync(final JsonObject criteria, final Sorter sorter) {
        return this.fetchAsync(criteria.put(VString.EMPTY, Boolean.TRUE), sorter);
    }

    public <T> Future<List<T>> fetchOrAsync(final JsonObject criteria, final Sorter sorter) {
        return this.fetchAsync(criteria.put(VString.EMPTY, Boolean.FALSE), sorter);
    }

    public Future<JsonArray> fetchJAsync(final JsonObject criteria, final Sorter sorter) {
        return this.fetchAsync(criteria, sorter).compose(this.workflow::outputAsync);
    }

    public Future<JsonArray> fetchJAndAsync(final JsonObject criteria, final Sorter sorter) {
        return this.fetchAsync(criteria.put(VString.EMPTY, Boolean.TRUE), sorter).compose(this.workflow::outputAsync);
    }

    public Future<JsonArray> fetchJOrAsync(final JsonObject criteria, final Sorter sorter) {
        return this.fetchAsync(criteria.put(VString.EMPTY, Boolean.FALSE), sorter).compose(this.workflow::outputAsync);
    }

    /* fetch(JsonObject)
     * fetch(JsonObject, Sorter)
     **/
    public <T> List<T> fetch(final JsonObject criteria) {
        return this.reader.fetch(this.workflow.inputQrJ(criteria));
    }

    public <T> List<T> fetchAnd(final JsonObject criteria) {
        return this.fetch(criteria.put(VString.EMPTY, Boolean.TRUE));
    }

    public <T> List<T> fetchOr(final JsonObject criteria) {
        return this.fetch(criteria.put(VString.EMPTY, Boolean.FALSE));
    }

    public JsonArray fetchJ(final JsonObject criteria) {
        return this.workflow.output(this.fetch(criteria));
    }

    public JsonArray fetchJAnd(final JsonObject criteria) {
        return this.workflow.output(this.fetch(criteria.put(VString.EMPTY, Boolean.TRUE)));
    }

    public JsonArray fetchJOr(final JsonObject criteria) {
        return this.workflow.output(this.fetch(criteria.put(VString.EMPTY, Boolean.FALSE)));
    }

    public <T> List<T> fetch(final JsonObject criteria, final Sorter sorter) {
        return this.reader.fetch(this.workflow.inputQrJ(criteria), sorter);
    }

    public <T> List<T> fetchAnd(final JsonObject criteria, final Sorter sorter) {
        return this.fetch(criteria.put(VString.EMPTY, Boolean.TRUE), sorter);
    }

    public <T> List<T> fetchOr(final JsonObject criteria, final Sorter sorter) {
        return this.fetch(criteria.put(VString.EMPTY, Boolean.FALSE), sorter);
    }

    public JsonArray fetchJ(final JsonObject criteria, final Sorter sorter) {
        return this.workflow.output(this.fetch(criteria, sorter));
    }

    public JsonArray fetchJAnd(final JsonObject criteria, final Sorter sorter) {
        return this.workflow.output(this.fetch(criteria.put(VString.EMPTY, Boolean.TRUE), sorter));
    }

    public JsonArray fetchJOr(final JsonObject criteria, final Sorter sorter) {
        return this.workflow.output(this.fetch(criteria.put(VString.EMPTY, Boolean.FALSE), sorter));
    }

    /* fetchAsync(JsonObject, pojo) */
    public <T> Future<List<T>> fetchAsync(final JsonObject criteria, final String pojo) {
        return JqFlow.create(this.analyzer, pojo).inputQrJAsync(criteria).compose(this.reader::fetchAsync);
    }

    public <T> Future<List<T>> fetchAndAsync(final JsonObject criteria, final String pojo) {
        return this.fetchAsync(criteria.put(VString.EMPTY, Boolean.TRUE), pojo);
    }

    public <T> Future<List<T>> fetchOrAsync(final JsonObject criteria, final String pojo) {
        return this.fetchAsync(criteria.put(VString.EMPTY, Boolean.FALSE), pojo);
    }

    public Future<JsonArray> fetchJAsync(final JsonObject criteria, final String pojo) {
        return this.fetchAsync(criteria, pojo).compose(JqFlow.create(this.analyzer, pojo)::outputAsync);
    }

    public Future<JsonArray> fetchJAndAsync(final JsonObject criteria, final String pojo) {
        return this.fetchAsync(criteria.put(VString.EMPTY, Boolean.TRUE), pojo).compose(JqFlow.create(this.analyzer, pojo)::outputAsync);
    }

    public Future<JsonArray> fetchJOrAsync(final JsonObject criteria, final String pojo) {
        return this.fetchAsync(criteria.put(VString.EMPTY, Boolean.FALSE), pojo).compose(JqFlow.create(this.analyzer, pojo)::outputAsync);
    }

    /* fetch(JsonObject, pojo) */
    public <T> List<T> fetch(final JsonObject criteria, final String pojo) {
        return this.reader.fetch(JqFlow.create(this.analyzer, pojo).inputQrJ(criteria));
    }

    public <T> List<T> fetchAnd(final JsonObject criteria, final String pojo) {
        return this.fetch(criteria.put(VString.EMPTY, Boolean.TRUE), pojo);
    }

    public <T> List<T> fetchOr(final JsonObject criteria, final String pojo) {
        return this.fetch(criteria.put(VString.EMPTY, Boolean.FALSE), pojo);
    }

    public JsonArray fetchJ(final JsonObject criteria, final String pojo) {
        return JqFlow.create(this.analyzer, pojo).output(this.fetch(criteria, pojo));
    }

    public JsonArray fetchJAnd(final JsonObject criteria, final String pojo) {
        return JqFlow.create(this.analyzer, pojo).output(this.fetch(criteria.put(VString.EMPTY, Boolean.TRUE), pojo));
    }
    // -------------------- Fetch One/All --------------------

    public JsonArray fetchJOr(final JsonObject criteria, final String pojo) {
        return JqFlow.create(this.analyzer, pojo).output(this.fetch(criteria.put(VString.EMPTY, Boolean.FALSE), pojo));
    }

    /*
     * fetchByIdAsync(Object)
     *      <-- fetchJByIdAsync(Object)
     *
     * fetchById(Object)
     *      <-- fetchJById(Object)
     *
     * fetchOneAsync(String, Object)
     *      <-- fetchJOneAsync(String, Object)
     *
     * fetchOne(String, Object)
     *      <-- fetchJOne(String, Object)
     *
     * fetchOneAsync(JsonObject)
     *      <-- fetchJOneAsync(JsonObject)
     *
     * fetchOne(JsonObject)
     *      <-- fetchJOne(JsonObject)
     *
     * fetchOneAsync(JsonObject, pojo)
     *      <-- fetchJOneAsync(JsonObject, pojo)
     *
     * fetchOne(JsonObject, pojo)
     *      <-- fetchJOne(JsonObject, pojo)
     */
    public <T> Future<T> fetchByIdAsync(final Object id) {
        return this.reader.fetchByIdAsync(id);
    }

    public Future<JsonObject> fetchJByIdAsync(final Object id) {
        return this.fetchByIdAsync(id).compose(this.workflow::outputAsync);
    }

    public <T> T fetchById(final Object id) {
        return this.reader.fetchById(id);
    }

    public <T> JsonObject fetchJById(final Object id) {
        return this.workflow.output((T) this.fetchById(id));
    }

    public <T> Future<T> fetchOneAsync(final String field, final Object value) {
        return this.reader.fetchOneAsync(field, value);
    }

    public Future<JsonObject> fetchJOneAsync(final String field, final Object value) {
        return this.fetchOneAsync(field, value).compose(this.workflow::outputAsync);
    }

    public <T> T fetchOne(final String field, final Object value) {
        return this.reader.fetchOne(field, value);
    }

    public <T> JsonObject fetchJOne(final String field, final Object value) {
        return this.workflow.output((T) this.fetchOne(field, value));
    }

    public <T> Future<T> fetchOneAsync(final JsonObject criteria) {
        criteria.put(VString.EMPTY, Boolean.TRUE);                                                  // Unique Forced
        return this.workflow.inputQrJAsync(criteria).compose(this.reader::fetchOneAsync);
    }

    public Future<JsonObject> fetchJOneAsync(final JsonObject criteria) {
        return this.fetchOneAsync(criteria).compose(this.workflow::outputAsync);
    }

    public <T> T fetchOne(final JsonObject criteria) {
        criteria.put(VString.EMPTY, Boolean.TRUE);                                                  // Unique Forced
        return this.reader.fetchOne(this.workflow.inputQrJ(criteria));
    }

    public <T> JsonObject fetchJOne(final JsonObject criteria) {
        return this.workflow.output((T) this.fetchOne(criteria));
    }

    public <T> Future<T> fetchOneAsync(final JsonObject criteria, final String pojo) {
        criteria.put(VString.EMPTY, Boolean.TRUE);                                                  // Unique Forced
        return JqFlow.create(this.analyzer, pojo).inputQrJAsync(criteria).compose(this.reader::fetchOneAsync);
    }

    public Future<JsonObject> fetchJOneAsync(final JsonObject criteria, final String pojo) {
        return this.fetchOneAsync(criteria, pojo).compose(JqFlow.create(this.analyzer, pojo)::outputAsync);
    }

    public <T> T fetchOne(final JsonObject criteria, final String pojo) {
        criteria.put(VString.EMPTY, Boolean.TRUE);                                                  // Unique Forced
        return this.reader.fetchOne(JqFlow.create(this.analyzer, pojo).inputQrJ(criteria));
    }

    public <T> JsonObject fetchJOne(final JsonObject criteria, final String pojo) {
        return JqFlow.create(this.analyzer, pojo).output((T) this.fetchOne(criteria, pojo));
    }

    // -------------------- Fetch Record -------------------
    /*
     * update(T)
     *      <-- update(JsonObject)
     *      <-- update(JsonObject, pojo)
     *      <-- updateJ(T)
     *      <-- updateJ(JsonObject)
     *      <-- updateJ(JsonObject, pojo)
     */
    public <T> T update(final T entity) {
        return this.writer.update(entity);
    }

    public <T> T update(final JsonObject data) {
        return this.update((T) this.workflow.input(data));
    }

    public <T> T update(final JsonObject data, final String pojo) {
        return this.update((T) JqFlow.create(this.analyzer, pojo).input(data));
    }

    public <T> JsonObject updateJ(final T entity) {
        return this.workflow.output(this.update(entity));
    }

    public <T> JsonObject updateJ(final JsonObject data) {
        return this.workflow.output(this.update((T) this.workflow.input(data)));
    }

    public <T> JsonObject updateJ(final JsonObject data, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return flow.output(this.update((T) flow.input(data)));
    }

    /*
     * updateAsync(T)
     *      <-- updateAsync(JsonObject)
     *      <-- updateAsync(JsonObject, pojo)
     *      <-- updateAsyncJ(T)
     *      <-- updateAsyncJ(JsonObject)
     *      <-- updateAsyncJ(JsonObject, pojo)
     */
    public <T> Future<T> updateAsync(final T entity) {
        return this.writer.updateAsync(entity);
    }

    public <T> Future<T> updateAsync(final JsonObject data) {
        return this.workflow.<T>inputAsync(data).compose(this::updateAsync);
    }

    public <T> Future<T> updateAsync(final JsonObject data, final String pojo) {
        return JqFlow.create(this.analyzer, pojo).<T>inputAsync(data).compose(this::updateAsync);
    }

    public <T> Future<JsonObject> updateAsyncJ(final T entity) {
        return this.updateAsync(entity).compose(this.workflow::outputAsync);
    }

    public <T> Future<JsonObject> updateAsyncJ(final JsonObject data) {
        return this.workflow.<T>inputAsync(data).compose(this::updateAsync).compose(this.workflow::outputAsync);
    }

    public <T> Future<JsonObject> updateAsyncJ(final JsonObject data, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return flow.<T>inputAsync(data).compose(this::updateAsync).compose(flow::outputAsync);
    }

    /*
     * update(List<T>)
     *      <-- update(JsonArray)
     *      <-- update(JsonArray, pojo)
     *      <-- updateJ(T)
     *      <-- updateJ(JsonArray)
     *      <-- updateJ(JsonArray, pojo)
     */
    public <T> List<T> update(final List<T> entities) {
        return this.writer.update(entities);
    }

    public <T> List<T> update(final JsonArray data) {
        return this.update(this.workflow.input(data));
    }

    public <T> List<T> update(final JsonArray data, final String pojo) {
        return this.update(JqFlow.create(this.analyzer, pojo).input(data));
    }

    public <T> JsonArray updateJ(final List<T> entities) {
        return this.workflow.output(this.update(entities));
    }

    public JsonArray updateJ(final JsonArray data) {
        return this.workflow.output(this.update(this.workflow.input(data)));
    }

    public <T> JsonArray updateJ(final JsonArray data, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return flow.output(this.update(flow.input(data)));
    }

    /*
     * updateAsync(List<T>)
     *      <-- updateAsync(JsonArray)
     *      <-- updateAsync(JsonArray, pojo)
     *      <-- updateJAsync(List<T>)
     *      <-- updateJAsync(JsonArray)
     *      <-- updateJAsync(JsonArray, pojo)
     */
    public <T> Future<List<T>> updateAsync(final List<T> entities) {
        return this.writer.updateAsync(entities);
    }

    public <T> Future<List<T>> updateAsync(final JsonArray data) {
        return this.workflow.<T>inputAsync(data).compose(this::updateAsync);
    }

    public <T> Future<List<T>> updateAsync(final JsonArray data, final String pojo) {
        return JqFlow.create(this.analyzer, pojo).<T>inputAsync(data).compose(this::updateAsync);
    }

    public <T> Future<JsonArray> updateAsyncJ(final List<T> entities) {
        return this.updateAsync(entities).compose(this.workflow::outputAsync);
    }

    public <T> Future<JsonArray> updateAsyncJ(final JsonArray input) {
        return this.workflow.<T>inputAsync(input).compose(this::updateAsync).compose(this.workflow::outputAsync);
    }

    public <T> Future<JsonArray> updateAsyncJ(final JsonArray input, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return flow.<T>inputAsync(input).compose(this::updateAsync).compose(flow::outputAsync);
    }

    /*
     * update(id, T)
     *      <-- update(id, JsonObject)
     *      <-- update(id, JsonObject, pojo)
     *      <-- updateJ(id, T)
     *      <-- updateJ(id, JsonObject)
     *      <-- updateJ(id, JsonObject, pojo)
     */
    public <T> T update(final Object id, final T updated) {
        return this.writer.update(id, updated);
    }

    public <T> T update(final Object id, final JsonObject data) {
        return this.update(id, (T) this.workflow.input(data));
    }

    public <T> T update(final Object id, final JsonObject data, final String pojo) {
        return this.update(id, (T) JqFlow.create(this.analyzer, pojo).input(data));
    }

    public <T> JsonObject updateJ(final Object id, final T updated) {
        return this.workflow.output(this.update(id, updated));
    }

    public <T> JsonObject updateJ(final Object id, final JsonObject data) {
        return this.workflow.output(this.update(id, (T) this.workflow.input(data)));
    }

    public <T> JsonObject updateJ(final Object id, final JsonObject data, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return flow.output(this.update(id, (T) flow.input(data)));
    }

    /*
     * updateAsync(id, T)
     *      <-- updateAsync(id, JsonObject)
     *      <-- updateAsync(id, JsonObject, pojo)
     *      <-- updateJAsync(id, T)
     *      <-- updateJAsync(id, JsonObject)
     *      <-- updateJAsync(id, JsonObject, pojo)
     */
    public <T, ID> Future<T> updateAsync(final ID id, final T updated) {
        return this.writer.updateAsync(id, updated);
    }

    public <T, ID> Future<T> updateAsync(final ID id, final JsonObject data) {
        return this.workflow.<T>inputAsync(data).compose(entity -> this.updateAsync(id, entity));
    }

    public <T, ID> Future<T> updateAsync(final ID id, final JsonObject data, final String pojo) {
        return JqFlow.create(this.analyzer, pojo).<T>inputAsync(data).compose(entity -> this.updateAsync(id, entity));
    }

    public <T, ID> Future<JsonObject> updateJAsync(final ID id, final T updated) {
        return this.updateAsync(id, updated).compose(this.workflow::outputAsync);
    }

    public <T, ID> Future<JsonObject> updateJAsync(final ID id, final JsonObject data) {
        return this.workflow.<T>inputAsync(data).compose(entity -> this.updateAsync(id, entity)).compose(this.workflow::outputAsync);
    }

    public <T, ID> Future<JsonObject> updateJAsync(final ID id, final JsonObject data, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return flow.<T>inputAsync(data).compose(entity -> this.updateAsync(id, entity)).compose(flow::outputAsync);
    }

    /*
     * update(criteria, T)
     *      <-- update(criteria, JsonObject)
     *      <-- updateJ(criteria, T)
     *      <-- updateJ(criteria, JsonObject)
     * update(criteria, T, pojo)
     *      <-- update(criteria, JsonObject, pojo)
     *      <-- updateJ(criteria, T, pojo)
     *      <-- updateJ(criteria, JsonObject, pojo)
     */
    public <T> T update(final JsonObject criteria, final T updated) {
        criteria.put(VString.EMPTY, Boolean.TRUE);                                                  // Unique Forced
        return this.writer.update(this.workflow.inputQrJ(criteria), updated);
    }

    public <T> T update(final JsonObject criteria, final JsonObject data) {
        return this.update(criteria, (T) this.workflow.input(data));
    }

    public <T> JsonObject updateJ(final JsonObject criteria, final T updated) {
        return this.workflow.output(this.update(criteria, updated));
    }

    public <T> JsonObject updateJ(final JsonObject criteria, final JsonObject data) {
        return this.workflow.output(this.update(criteria, (T) this.workflow.input(data)));
    }

    public <T> T update(final JsonObject criteria, final T updated, final String pojo) {
        criteria.put(VString.EMPTY, Boolean.TRUE);                                                  // Unique Forced
        return this.writer.update(JqFlow.create(this.analyzer, pojo).inputQrJ(criteria), updated);
    }

    public <T> T update(final JsonObject criteria, final JsonObject data, final String pojo) {
        return this.update(criteria, (T) JqFlow.create(this.analyzer, pojo).input(data), pojo);
    }

    public <T> JsonObject updateJ(final JsonObject criteria, final T updated, final String pojo) {
        return this.workflow.output(this.update(criteria, updated, pojo));
    }

    public <T> JsonObject updateJ(final JsonObject criteria, final JsonObject data, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return flow.output(this.update(criteria, (T) flow.input(data), pojo));
    }

    /*
     * updateAsync(criteria, T)
     *      <-- updateAsync(criteria, JsonObject)
     *      <-- updateJAsync(criteria, T)
     *      <-- updateJAsync(criteria, JsonObject)
     * updateAsync(criteria, T, pojo)
     *      <-- updateAsync(criteria, JsonObject, pojo)
     *      <-- updateJAsync(criteria, T, pojo)
     *      <-- updateJAsync(criteria, JsonObject, pojo)
     */
    public <T> Future<T> updateAsync(final JsonObject criteria, final T updated) {
        criteria.put(VString.EMPTY, Boolean.TRUE);                                                  // Unique Forced
        return this.workflow.inputQrJAsync(criteria).compose(normalized -> this.writer.updateAsync(normalized, updated));
    }

    public <T> Future<T> updateAsync(final JsonObject criteria, final JsonObject data) {
        return JqTool.joinAsync(criteria, data, this.workflow)
            .compose(response -> this.updateAsync(response.resultAt(VValue.IDX), (T) response.resultAt(VValue.ONE)));
    }

    public <T> Future<JsonObject> updateJAsync(final JsonObject criteria, final T updated) {
        return this.updateAsync(criteria, updated).compose(this.workflow::outputAsync);
    }

    public <T> Future<JsonObject> updateJAsync(final JsonObject criteria, final JsonObject data) {
        return JqTool.joinAsync(criteria, data, this.workflow)
            .compose(response -> this.updateAsync(response.resultAt(VValue.IDX), (T) response.resultAt(VValue.ONE)))
            .compose(this.workflow::outputAsync);
    }

    public <T> Future<T> updateAsync(final JsonObject criteria, final T updated, final String pojo) {
        criteria.put(VString.EMPTY, Boolean.TRUE);                                                  // Unique Forced
        return JqFlow.create(this.analyzer, pojo).inputQrJAsync(criteria).compose(normalized -> this.writer.updateAsync(normalized, updated));
    }

    public <T> Future<T> updateAsync(final JsonObject criteria, final JsonObject data, final String pojo) {
        return JqTool.joinAsync(criteria, data, JqFlow.create(this.analyzer, pojo))
            .compose(response -> this.updateAsync(response.resultAt(VValue.IDX), (T) response.resultAt(VValue.ONE), pojo));
    }

    public <T> Future<JsonObject> updateJAsync(final JsonObject criteria, final T updated, final String pojo) {
        return this.updateAsync(criteria, updated, pojo).compose(JqFlow.create(this.analyzer, pojo)::outputAsync);
    }

    public <T> Future<JsonObject> updateJAsync(final JsonObject criteria, final JsonObject data, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return JqTool.joinAsync(criteria, data, flow)
            .compose(response -> this.updateAsync(response.resultAt(VValue.IDX), (T) response.resultAt(VValue.ONE), pojo))
            .compose(flow::outputAsync);
    }

    // -------------------- Upsert Operation ( INSERT / UPDATE ) ---------

    /*
     * upsert(id, T)
     *      <-- upsert(id, JsonObject)
     *      <-- upsert(id, JsonObject, pojo)
     *      <-- upsertJ(id, T)
     *      <-- upsertJ(id, JsonObject)
     *      <-- upsertJ(id, JsonObject, pojo)
     */
    public <T> T upsert(final Object id, final T updated) {
        return this.writer.upsert(id, updated);
    }

    public <T> T upsert(final Object id, final JsonObject data) {
        return this.upsert(id, (T) this.workflow.input(data));
    }

    public <T> T upsert(final Object id, final JsonObject data, final String pojo) {
        return this.upsert(id, (T) JqFlow.create(this.analyzer, pojo).input(data));
    }

    public <T> JsonObject upsertJ(final Object id, final T updated) {
        return this.workflow.output(this.upsert(id, updated));
    }

    public <T> JsonObject upsertJ(final Object id, final JsonObject data) {
        return this.workflow.output(this.upsert(id, (T) this.workflow.input(data)));
    }

    public <T> JsonObject upsertJ(final Object id, final JsonObject data, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return flow.output(this.upsert(id, (T) flow.input(data)));
    }

    /*
     * upsertAsync(id, T)
     *      <-- upsertAsync(id, JsonObject)
     *      <-- upsertAsync(id, JsonObject, pojo)
     *      <-- upsertJAsync(id, T)
     *      <-- upsertJAsync(id, JsonObject)
     *      <-- upsertJAsync(id, JsonObject, pojo)
     */
    public <T> Future<T> upsertAsync(final Object id, final T updated) {
        return this.writer.upsertAsync(id, updated);
    }

    public <T> Future<T> upsertAsync(final Object id, final JsonObject data) {
        return this.workflow.<T>inputAsync(data).compose(updated -> this.upsertAsync(id, updated));
    }

    public <T> Future<T> upsertAsync(final Object id, final JsonObject data, final String pojo) {
        return JqFlow.create(this.analyzer, pojo).<T>inputAsync(data).compose(updated -> this.upsertAsync(id, updated));
    }

    public <T> Future<JsonObject> upsertJAsync(final Object id, final T updated) {
        return this.upsertAsync(id, updated).compose(this.workflow::outputAsync);
    }

    public <T> Future<JsonObject> upsertJAsync(final Object id, final JsonObject data) {
        return this.workflow.<T>inputAsync(data).compose(updated -> this.upsertAsync(id, updated)).compose(this.workflow::outputAsync);
    }

    public <T> Future<JsonObject> upsertJAsync(final Object id, final JsonObject data, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return flow.<T>inputAsync(data).compose(updated -> this.upsertAsync(id, updated)).compose(flow::outputAsync);
    }

    /*
     * upsert(criteria, T)
     *      <-- upsert(criteria, JsonObject)
     *      <-- upsertJ(criteria, T)
     *      <-- upsertJ(criteria, JsonObject)
     * upsert(criteria, T, pojo)
     *      <-- upsert(criteria, JsonObject, pojo)
     *      <-- upsertJ(criteria, T, pojo)
     *      <-- upsertJ(criteria, JsonObject, pojo)
     */
    public <T> T upsert(final JsonObject criteria, final T updated) {
        criteria.put(VString.EMPTY, Boolean.TRUE);                                                  // Unique Forced
        return this.writer.upsert(this.workflow.inputQrJ(criteria), updated);
    }

    public <T> T upsert(final JsonObject criteria, final JsonObject data) {
        return this.upsert(criteria, (T) this.workflow.input(data));
    }

    public <T> JsonObject upsertJ(final JsonObject criteria, final T updated) {
        return this.workflow.output(this.upsert(criteria, updated));
    }

    public <T> JsonObject upsertJ(final JsonObject criteria, final JsonObject data) {
        return this.workflow.output(this.upsert(criteria, (T) this.workflow.input(data)));
    }

    public <T> T upsert(final JsonObject criteria, final T updated, final String pojo) {
        criteria.put(VString.EMPTY, Boolean.TRUE);                                                  // Unique Forced
        return this.writer.upsert(JqFlow.create(this.analyzer, pojo).inputQrJ(criteria), updated);
    }

    public <T> T upsert(final JsonObject criteria, final JsonObject data, final String pojo) {
        return this.upsert(criteria, (T) JqFlow.create(this.analyzer, pojo).input(data), pojo);
    }

    public <T> JsonObject upsertJ(final JsonObject criteria, final T updated, final String pojo) {
        return this.workflow.output(this.upsert(criteria, updated, pojo));
    }

    public <T> JsonObject upsertJ(final JsonObject criteria, final JsonObject data, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return flow.output(this.upsert(criteria, (T) flow.input(data), pojo));
    }

    /*
     * upsertAsync(criteria, T)
     *      <-- upsertAsync(criteria, JsonObject)
     *      <-- upsertJAsync(criteria, T)
     *      <-- upsertJAsync(criteria, JsonObject)
     * upsertAsync(criteria, T, pojo)
     *      <-- upsertAsync(criteria, JsonObject, pojo)
     *      <-- upsertJAsync(criteria, T, pojo)
     *      <-- upsertJAsync(criteria, JsonObject, pojo)
     */
    public <T> Future<T> upsertAsync(final JsonObject criteria, final T updated) {
        criteria.put(VString.EMPTY, Boolean.TRUE);                                                  // Unique Forced
        return this.workflow.inputQrJAsync(criteria).compose(normalized -> this.writer.upsertAsync(normalized, updated));
    }

    public <T> Future<T> upsertAsync(final JsonObject criteria, final JsonObject data) {
        return JqTool.joinAsync(criteria, data, this.workflow)
            .compose(response -> this.upsertAsync(response.resultAt(VValue.IDX), (T) response.resultAt(VValue.ONE)));
    }

    public <T> Future<JsonObject> upsertJAsync(final JsonObject criteria, final T updated) {
        return this.upsertAsync(criteria, updated).compose(this.workflow::outputAsync);
    }

    public <T> Future<JsonObject> upsertJAsync(final JsonObject criteria, final JsonObject data) {
        return JqTool.joinAsync(criteria, data, this.workflow)
            .compose(response -> this.upsertAsync(response.resultAt(VValue.IDX), (T) response.resultAt(VValue.ONE)))
            .compose(this.workflow::outputAsync);
    }

    public <T> Future<T> upsertAsync(final JsonObject criteria, final T updated, final String pojo) {
        criteria.put(VString.EMPTY, Boolean.TRUE);                                                  // Unique Forced
        return JqFlow.create(this.analyzer, pojo).inputQrJAsync(criteria).compose(normalized -> this.writer.upsertAsync(normalized, updated));
    }

    public <T> Future<T> upsertAsync(final JsonObject criteria, final JsonObject data, final String pojo) {
        return JqTool.joinAsync(criteria, data, JqFlow.create(this.analyzer, pojo))
            .compose(response -> this.upsertAsync(response.resultAt(VValue.IDX), (T) response.resultAt(VValue.ONE), pojo));
    }

    public <T> Future<JsonObject> upsertJAsync(final JsonObject criteria, final T updated, final String pojo) {
        return this.upsertAsync(criteria, updated, pojo).compose(JqFlow.create(this.analyzer, pojo)::outputAsync);
    }

    public <T> Future<JsonObject> upsertJAsync(final JsonObject criteria, final JsonObject data, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return JqTool.joinAsync(criteria, data, flow)
            .compose(response -> this.upsertAsync(response.resultAt(VValue.IDX), (T) response.resultAt(VValue.ONE), pojo))
            .compose(flow::outputAsync);
    }

    /*
     * upsert(criteria, list, finder)
     *      <-- upsert(criteria, JsonArray, finder)
     *      <-- upsertJ(criteria, list, finder)
     *      <-- upsertJ(criteria, JsonArray, finder)
     * upsert(criteria, list, finder, pojo)
     *      <-- upsert(criteria, JsonArray, finder, pojo)
     *      <-- upsertJ(criteria, list, finder, pojo)
     *      <-- upsertJ(criteria, JsonArray, finder, pojo)
     */
    public <T> List<T> upsert(final JsonObject criteria, final List<T> list, final BiPredicate<T, T> finder) {
        return this.writer.upsert(this.workflow.inputQrJ(criteria), list, finder);
    }

    public <T> List<T> upsert(final JsonObject criteria, final JsonArray data, final BiPredicate<T, T> finder) {
        return this.upsert(criteria, this.workflow.input(data), finder);
    }

    public <T> JsonArray upsertJ(final JsonObject criteria, final List<T> list, final BiPredicate<T, T> finder) {
        return this.workflow.output(this.upsert(criteria, list, finder));
    }

    public <T> JsonArray upsertJ(final JsonObject criteria, final JsonArray data, final BiPredicate<T, T> finder) {
        return this.workflow.output(this.upsert(criteria, this.workflow.input(data), finder));
    }

    public <T> List<T> upsert(final JsonObject criteria, final List<T> list, final BiPredicate<T, T> finder, final String pojo) {
        return this.writer.upsert(JqFlow.create(this.analyzer, pojo).inputQrJ(criteria), list, finder);
    }

    public <T> List<T> upsert(final JsonObject criteria, final JsonArray data, final BiPredicate<T, T> finder, final String pojo) {
        return this.upsert(criteria, JqFlow.create(this.analyzer, pojo).input(data), finder, pojo);
    }

    public <T> JsonArray upsertJ(final JsonObject criteria, final List<T> list, final BiPredicate<T, T> finder, final String pojo) {
        return JqFlow.create(this.analyzer, pojo).output(this.upsert(criteria, list, finder, pojo));
    }

    public <T> JsonArray upsertJ(final JsonObject criteria, final JsonArray data, final BiPredicate<T, T> finder, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return flow.output(this.upsert(criteria, flow.input(data), finder, pojo));
    }

    /*
     * upsertAsync(criteria, list, finder)
     *      <-- upsertAsync(criteria, JsonArray, finder)
     *      <-- upsertJAsync(criteria, list, finder)
     *      <-- upsertJAsync(criteria, JsonArray, finder)
     * upsertAsync(criteria, list, finder, pojo)
     *      <-- upsertAsync(criteria, JsonArray, finder, pojo)
     *      <-- upsertJAsync(criteria, list, finder, pojo)
     *      <-- upsertJAsync(criteria, JsonArray, finder, pojo)
     */
    public <T> Future<List<T>> upsertAsync(final JsonObject criteria, final List<T> list, final BiPredicate<T, T> finder) {
        return this.workflow.inputQrJAsync(criteria).compose(normalized -> this.writer.upsertAsync(normalized, list, finder));
    }

    public <T> Future<List<T>> upsertAsync(final JsonObject criteria, final JsonArray data, final BiPredicate<T, T> finder) {
        return JqTool.joinAsync(criteria, data, this.workflow)
            .compose(response -> this.upsertAsync(response.resultAt(VValue.IDX), (List<T>) response.resultAt(VValue.ONE), finder));
    }

    public <T> Future<JsonArray> upsertJAsync(final JsonObject criteria, final List<T> list, final BiPredicate<T, T> finder) {
        return this.upsertAsync(criteria, list, finder).compose(this.workflow::outputAsync);
    }

    public <T> Future<JsonArray> upsertJAsync(final JsonObject criteria, final JsonArray data, final BiPredicate<T, T> finder) {
        return JqTool.joinAsync(criteria, data, this.workflow)
            .compose(response -> this.upsertAsync(response.resultAt(VValue.IDX), (List<T>) response.resultAt(VValue.ONE), finder))
            .compose(this.workflow::outputAsync);
    }

    public <T> Future<List<T>> upsertAsync(final JsonObject criteria, final List<T> list, final BiPredicate<T, T> finder, final String pojo) {
        return JqFlow.create(this.analyzer, pojo).inputQrJAsync(criteria).compose(normalized -> this.writer.upsertAsync(normalized, list, finder));
    }

    public <T> Future<List<T>> upsertAsync(final JsonObject criteria, final JsonArray data, final BiPredicate<T, T> finder, final String pojo) {
        return JqTool.joinAsync(criteria, data, JqFlow.create(this.analyzer, pojo))
            .compose(response -> this.upsertAsync(response.resultAt(VValue.IDX), (List<T>) response.resultAt(VValue.ONE), finder, pojo));
    }

    public <T> Future<JsonArray> upsertJAsync(final JsonObject criteria, final List<T> list, final BiPredicate<T, T> finder, final String pojo) {
        return this.upsertAsync(criteria, list, finder, pojo).compose(JqFlow.create(this.analyzer, pojo)::outputAsync);
    }

    public <T> Future<JsonArray> upsertJAsync(final JsonObject criteria, final JsonArray data, final BiPredicate<T, T> finder, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return JqTool.joinAsync(criteria, data, flow)
            .compose(response -> this.upsertAsync(response.resultAt(VValue.IDX), (List<T>) response.resultAt(VValue.ONE), finder, pojo))
            .compose(flow::outputAsync);
    }

    // -------------------- DELETE --------------------
    /*
     * delete(T)
     *      <-- delete(JsonObject)
     *      <-- delete(JsonObject, pojo)
     *      <-- deleteJ(T)
     *      <-- deleteJ(JsonObject)
     *      <-- deleteJ(JsonObject, pojo)
     */
    public <T> T delete(final T entity) {
        return this.writer.delete(entity);
    }

    public <T> T delete(final JsonObject data) {
        return this.delete((T) this.workflow.input(data));
    }

    public <T> T delete(final JsonObject data, final String pojo) {
        return this.delete((T) JqFlow.create(this.analyzer, pojo).input(data));
    }

    public <T> JsonObject deleteJ(final T entity) {
        return this.workflow.output(this.delete(entity));
    }

    public <T> JsonObject deleteJ(final JsonObject data) {
        return this.workflow.output(this.delete((T) this.workflow.input(data)));
    }

    public <T> JsonObject deleteJ(final JsonObject data, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return flow.output(this.delete((T) flow.input(data)));
    }

    /*
     * deleteAsync(T)
     *      <-- deleteAsync(JsonObject)
     *      <-- deleteAsync(JsonObject, pojo)
     *      <-- deleteJAsync(T)
     *      <-- deleteJAsync(JsonObject)
     *      <-- deleteJAsync(JsonObject, pojo)
     */
    public <T, ID> Future<T> deleteAsync(final T entity) {
        return this.writer.deleteAsync(entity);
    }

    public <T, ID> Future<T> deleteAsync(final JsonObject data) {
        return this.workflow.<T>inputAsync(data).compose(this::deleteAsync);
    }

    public <T, ID> Future<T> deleteAsync(final JsonObject data, final String pojo) {
        return JqFlow.create(this.analyzer, pojo).<T>inputAsync(data).compose(this::deleteAsync);
    }

    public <T, ID> Future<JsonObject> deleteJAsync(final T entity) {
        return this.deleteAsync(entity).compose(this.workflow::outputAsync);
    }

    public <T, ID> Future<JsonObject> deleteJAsync(final JsonObject data) {
        return this.workflow.<T>inputAsync(data).compose(this::deleteAsync).compose(this.workflow::outputAsync);
    }

    public <T, ID> Future<JsonObject> deleteJAsync(final JsonObject data, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return flow.<T>inputAsync(data).compose(this::deleteAsync).compose(flow::outputAsync);
    }


    /*
     * delete(List<T>)
     *      <-- delete(JsonArray)
     *      <-- delete(JsonArray, pojo)
     *      <-- deleteJ(List<T>)
     *      <-- deleteJ(JsonArray)
     *      <-- deleteJ(JsonArray, pojo)
     */
    public <T> List<T> delete(final List<T> entity) {
        return this.writer.delete(entity);
    }

    public <T> List<T> delete(final JsonArray data) {
        return this.delete(this.workflow.input(data));
    }

    public <T> List<T> delete(final JsonArray data, final String pojo) {
        return this.delete(JqFlow.create(this.analyzer, pojo).input(data));
    }

    public <T> JsonArray deleteJ(final List<T> entity) {
        return this.workflow.output(this.delete(entity));
    }

    public <T> JsonArray deleteJ(final JsonArray data) {
        return this.workflow.output(this.delete((List<T>) this.workflow.input(data)));
    }

    public <T> JsonArray deleteJ(final JsonArray data, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return flow.output(this.delete((List<T>) flow.input(data)));
    }

    /*
     * deleteAsync(List<T>)
     *      <-- deleteAsync(JsonArray)
     *      <-- deleteAsync(JsonArray, pojo)
     *      <-- deleteJAsync(List<T>)
     *      <-- deleteJAsync(JsonArray)
     *      <-- deleteJAsync(JsonArray, pojo)
     */
    public <T> Future<List<T>> deleteAsync(final List<T> entity) {
        return this.writer.deleteAsync(entity);
    }

    public <T> Future<List<T>> deleteAsync(final JsonArray data) {
        return this.workflow.<T>inputAsync(data).compose(this::deleteAsync);
    }

    public <T> Future<List<T>> deleteAsync(final JsonArray data, final String pojo) {
        return JqFlow.create(this.analyzer, pojo).<T>inputAsync(data).compose(this::deleteAsync);
    }

    public <T> Future<JsonArray> deleteJAsync(final List<T> entity) {
        return this.deleteAsync(entity).compose(this.workflow::outputAsync);
    }

    public <T> Future<JsonArray> deleteJAsync(final JsonArray data) {
        return this.workflow.<T>inputAsync(data).compose(this::deleteAsync).compose(this.workflow::outputAsync);
    }

    public <T> Future<JsonArray> deleteJAsync(final JsonArray data, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return flow.<T>inputAsync(data).compose(this::deleteAsync).compose(flow::outputAsync);
    }


    /*
     * deleteById(id)
     * deleteByIds(Collection<ID> ids)
     * deleteByIdAsync(id)
     * deleteByIdAsyncs(Collection<ID> ids)
     */
    public final Boolean deleteById(final Object id) {
        return this.writer.deleteById(Arrays.asList(id));
    }

    public Boolean deleteByIds(final Collection<Object> ids) {
        return this.writer.deleteById(ids);
    }

    public final Future<Boolean> deleteByIdAsync(final Object id) {
        return this.writer.deleteByIdAsync(Arrays.asList(id));
    }

    public Future<Boolean> deleteByIdsAsync(final Collection<Object> ids) {
        return this.writer.deleteByIdAsync(ids);
    }

    /*
     * deleteBy(JsonObject)
     * deleteBy(JsonObject, pojo)
     * deleteByAsync(JsonObject)
     * deleteByAsync(JsonObject, pojo)
     */
    /* (Async / Sync) Delete by Filters */
    public Future<Boolean> deleteByAsync(final JsonObject criteria) {                                                 // Unique Forced
        return this.workflow.inputQrJAsync(andOr(criteria)).compose(this.writer::deleteByAsync);
    }

    public Future<Boolean> deleteByAsync(final JsonObject criteria, final String pojo) {
        return JqFlow.create(this.analyzer, pojo).inputQrJAsync(andOr(criteria)).compose(this.writer::deleteByAsync);
    }

    public Boolean deleteBy(final JsonObject criteria) {                                          // Unique Forced
        return this.writer.deleteBy(this.workflow.inputQrJ(andOr(criteria)));
    }

    public Boolean deleteBy(final JsonObject criteria, final String pojo) {
        return this.writer.deleteBy(JqFlow.create(this.analyzer, pojo).inputQrJ(andOr(criteria)));
    }

    // -------------------- Exist Operation --------------------
    /*
     * existById(key)
     *      <-- missById(key)
     * existByIdAsync(key)
     *      <-- missByIdAsync(key)
     */

    public Boolean existById(final Object id) {
        return this.reader.existById(id);
    }

    public Future<Boolean> existByIdAsync(final Object id) {
        return this.reader.existByIdAsync(id);
    }

    public Boolean missById(final Object id) {
        return !this.existById(id);
    }

    public Future<Boolean> missByIdAsync(final Object id) {
        return this.existByIdAsync(id)
            .compose(result -> Future.succeededFuture(!result));
    }

    /*
     * exist(JsonObject)
     *      <-- miss(JsonObject)
     * exist(JsonObject, pojo)
     *      <-- miss(JsonObject, pojo)
     * existAsync(JsonObject)
     *      <-- missAsync(JsonObject)
     * existAsync(JsonObject, pojo)
     *      <-- missAsync(JsonObject, pojo)
     */

    public Boolean exist(final JsonObject criteria) {
        return this.reader.exist(this.workflow.inputQrJ(criteria));
    }

    public Boolean exist(final JsonObject criteria, final String pojo) {
        return this.reader.exist(JqFlow.create(this.analyzer, pojo).inputQrJ(criteria));
    }

    public Future<Boolean> existAsync(final JsonObject criteria) {
        return this.workflow.inputQrJAsync(criteria).compose(this.reader::existAsync);
    }

    public Future<Boolean> existAsync(final JsonObject criteria, final String pojo) {
        return JqFlow.create(this.analyzer, pojo).inputQrJAsync(criteria).compose(this.reader::existAsync);
    }

    public Boolean miss(final JsonObject criteria) {
        return !this.exist(criteria);
    }

    public Boolean miss(final JsonObject criteria, final String pojo) {
        return !this.exist(criteria, pojo);
    }

    public Future<Boolean> missAsync(final JsonObject criteria) {
        return this.existAsync(criteria).compose(existing -> Future.succeededFuture(!existing));
    }

    public Future<Boolean> missAsync(final JsonObject criteria, final String pojo) {
        return this.existAsync(criteria, pojo).compose(existing -> Future.succeededFuture(!existing));
    }

    // -------------------- Group Operation ------------
    /*
     * group(String)
     *      <-- groupJ(String)
     *      <-- groupAsync(String)
     *      <-- groupJAsync(String)
     * group(JsonObject, String)
     *      <-- groupAsync(JsonObject, String)
     *      <-- groupJ(JsonObject, String)
     *      <-- groupJAsync(JsonObject, String)
     */
    public <T> ConcurrentMap<String, List<T>> group(final String field) {
        return this.aggregator.group(field);
    }

    public <T> ConcurrentMap<String, JsonArray> groupJ(final String field) {
        return this.workflow.output(this.group(field));
    }

    public <T> Future<ConcurrentMap<String, List<T>>> groupAsync(final String field) {
        return Future.succeededFuture(this.group(field));  // Async Future
    }

    public <T> Future<ConcurrentMap<String, JsonArray>> groupJAsync(final String field) {
        return Future.succeededFuture(this.group(field)).compose(this.workflow::outputAsync);
    }

    public <T> ConcurrentMap<String, List<T>> group(final JsonObject criteria, final String field) {
        return this.aggregator.group(this.workflow.inputQrJ(criteria), field);
    }

    public <T> ConcurrentMap<String, JsonArray> groupJ(final JsonObject criteria, final String field) {
        return this.workflow.output(this.group(criteria, field));
    }

    public <T> Future<ConcurrentMap<String, List<T>>> groupAsync(final JsonObject criteria, final String field) {
        return Future.succeededFuture(this.group(criteria, field));  // Async Future
    }

    public <T> Future<ConcurrentMap<String, JsonArray>> groupJAsync(final JsonObject criteria, final String field) {
        return Future.succeededFuture(this.group(criteria, field)).compose(this.workflow::outputAsync); // Async Future
    }

    // -------------------- Count Operation ------------
    /*
     * countAll()
     * countAllAsync()
     */
    public Long countAll() {
        return this.aggregator.countAll();
    }

    public Future<Long> countAllAsync() {
        return this.aggregator.countAllAsync();
    }

    /*
     * count(JsonObject)
     * count(JsonObject, pojo)
     * countAsync(JsonObject)
     * countAsync(JsonObject, pojo)
     */

    public Long count(final JsonObject criteria) {
        return this.aggregator.count(this.workflow.inputQrJ(criteria));
    }

    public Long count(final JsonObject criteria, final String pojo) {
        return this.aggregator.count(JqFlow.create(this.analyzer, pojo).inputQrJ(criteria));
    }

    public Future<Long> countAsync(final JsonObject criteria) {
        return this.workflow.inputQrJAsync(criteria).compose(this.aggregator::countAsync);
    }

    public Future<Long> countAsync(final JsonObject criteria, final String pojo) {
        return JqFlow.create(this.analyzer, pojo).inputQrJAsync(criteria).compose(this.aggregator::countAsync);
    }


    /*
     * countBy(JsonObject, String)
     *      <-- countByAsync(JsonObject, String)
     *      <-- countBy(String)
     *      <-- countByAsync(String)
     * countBy(JsonObject, String...)
     *      <-- countByAsync(JsonObject, String...)
     *      <-- countBy(String...)
     *      <-- countByAsync(String...)
     */
    public ConcurrentMap<String, Integer> countBy(final JsonObject criteria, final String groupField) {
        return this.aggregator.countBy(this.workflow.inputQrJ(criteria), groupField);
    }

    public ConcurrentMap<String, Integer> countBy(final String groupField) {
        return this.countBy(new JsonObject(), groupField);
    }

    public Future<ConcurrentMap<String, Integer>> countByAsync(final JsonObject criteria, final String groupField) {
        return Future.succeededFuture(this.countBy(criteria, groupField));
    }

    public Future<ConcurrentMap<String, Integer>> countByAsync(final String groupField) {
        return Future.succeededFuture(this.countBy(new JsonObject(), groupField));
    }

    public JsonArray countBy(final JsonObject criteria, final String... groupFields) {
        return this.aggregator.countBy(this.workflow.inputQrJ(criteria), groupFields);
    }

    public JsonArray countBy(final String... groupFields) {
        return this.countBy(new JsonObject(), groupFields);
    }

    public Future<JsonArray> countByAsync(final String... groupFields) {
        return Future.succeededFuture(this.countBy(new JsonObject(), groupFields));
    }

    public Future<JsonArray> countByAsync(final JsonObject criteria, final String... groupFields) {
        return Future.succeededFuture(this.countBy(criteria, groupFields));
    }

    // -------------------- Sum Operation ------------
    /*
     * sum(String)
     * sum(String, JsonObject)
     * sum(String, JsonObject, pojo)
     * sumAsync(String)
     * sumAsync(String, JsonObject)
     * sumAsync(String, JsonObject, pojo)
     */
    public BigDecimal sum(final String field) {
        return this.aggregator.sum(field, null);
    }

    public Future<BigDecimal> sumAsync(final String field) {
        return Future.succeededFuture(this.aggregator.sum(field, null));
    }

    public BigDecimal sum(final String field, final JsonObject criteria) {
        return this.aggregator.sum(field, this.workflow.inputQrJ(criteria));
    }

    public Future<BigDecimal> sumAsync(final String field, final JsonObject criteria) {
        return this.workflow.inputQrJAsync(criteria)
            .compose(processed -> Future.succeededFuture(this.aggregator.sum(field, processed)));
    }

    public BigDecimal sum(final String field, final JsonObject criteria, final String pojo) {
        return this.aggregator.sum(field, JqFlow.create(this.analyzer, pojo).inputQrJ(criteria));
    }

    public Future<BigDecimal> sumAsync(final String field, final JsonObject criteria, final String pojo) {
        return JqFlow.create(this.analyzer, pojo).inputQrJAsync(criteria)
            .compose(processed -> Future.succeededFuture(this.aggregator.sum(field, processed)));
    }

    /*
     * sumBy(String, JsonObject, String)
     *      <-- sumBy(String, String)
     *      <-- sumByAsync(String, String)
     *      <-- sumByAsync(String, JsonObject, String)
     * sumBy(String, JsonObject, String...)
     *      <-- sumBy(String, String...)
     *      <-- sumByAsync(String, String...)
     *      <-- sumByAsync(String, JsonObject, String...)
     */

    public ConcurrentMap<String, BigDecimal> sumBy(final String field, final JsonObject criteria, final String groupField) {
        return this.aggregator.sum(field, this.workflow.inputQrJ(criteria), groupField);
    }

    public ConcurrentMap<String, BigDecimal> sumBy(final String field, final String groupField) {
        return this.sumBy(field, new JsonObject(), groupField);
    }

    public Future<ConcurrentMap<String, BigDecimal>> sumByAsync(final String field, final String groupField) {
        return Future.succeededFuture(this.sumBy(field, new JsonObject(), groupField));
    }

    public Future<ConcurrentMap<String, BigDecimal>> sumByAsync(final String field, final JsonObject criteria, final String groupField) {
        return Future.succeededFuture(this.sumBy(field, criteria, groupField));
    }

    public JsonArray sumBy(final String field, final JsonObject criteria, final String... groupFields) {
        return this.aggregator.sum(field, this.workflow.inputQrJ(criteria), groupFields);
    }

    public JsonArray sumBy(final String field, final String... groupFields) {
        return this.sumBy(field, new JsonObject(), groupFields);
    }

    public Future<JsonArray> sumByAsync(final String field, final JsonObject criteria, final String... groupFields) {
        return Future.succeededFuture(this.sumBy(field, criteria, groupFields));
    }

    public Future<JsonArray> sumByAsync(final String field, final String... groupFields) {
        return Future.succeededFuture(this.sumBy(field, new JsonObject(), groupFields));
    }

    // -------------------- Max Operation ------------
    /*
     * max(String)
     * max(String, JsonObject)
     * max(String, JsonObject, pojo)
     * maxAsync(String)
     * maxAsync(String, JsonObject)
     * maxAsync(String, JsonObject, pojo)
     */
    public BigDecimal max(final String field) {
        return this.aggregator.max(field, null);
    }

    public Future<BigDecimal> maxAsync(final String field) {
        return Future.succeededFuture(this.aggregator.max(field, null));
    }

    public BigDecimal max(final String field, final JsonObject criteria) {
        return this.aggregator.max(field, this.workflow.inputQrJ(criteria));
    }

    public Future<BigDecimal> maxAsync(final String field, final JsonObject criteria) {
        return this.workflow.inputQrJAsync(criteria)
            .compose(processed -> Future.succeededFuture(this.aggregator.max(field, processed)));
    }

    public BigDecimal max(final String field, final JsonObject criteria, final String pojo) {
        return this.aggregator.max(field, JqFlow.create(this.analyzer, pojo).inputQrJ(criteria));
    }

    public Future<BigDecimal> maxAsync(final String field, final JsonObject criteria, final String pojo) {
        return JqFlow.create(this.analyzer, pojo).inputQrJAsync(criteria)
            .compose(processed -> Future.succeededFuture(this.aggregator.max(field, processed)));
    }

    /*
     * maxBy(String, JsonObject, String)
     *      <-- maxBy(String, String)
     *      <-- maxByAsync(String, String)
     *      <-- maxByAsync(String, JsonObject, String)
     * maxBy(String, JsonObject, String...)
     *      <-- maxBy(String, String...)
     *      <-- maxByAsync(String, String...)
     *      <-- maxByAsync(String, JsonObject, String...)
     */

    public ConcurrentMap<String, BigDecimal> maxBy(final String field, final JsonObject criteria, final String groupField) {
        return this.aggregator.max(field, this.workflow.inputQrJ(criteria), groupField);
    }

    public ConcurrentMap<String, BigDecimal> maxBy(final String field, final String groupField) {
        return this.maxBy(field, new JsonObject(), groupField);
    }

    public Future<ConcurrentMap<String, BigDecimal>> maxByAsync(final String field, final String groupField) {
        return Future.succeededFuture(this.maxBy(field, new JsonObject(), groupField));
    }

    public Future<ConcurrentMap<String, BigDecimal>> maxByAsync(final String field, final JsonObject criteria, final String groupField) {
        return Future.succeededFuture(this.maxBy(field, criteria, groupField));
    }

    public JsonArray maxBy(final String field, final JsonObject criteria, final String... groupFields) {
        return this.aggregator.max(field, this.workflow.inputQrJ(criteria), groupFields);
    }

    public JsonArray maxBy(final String field, final String... groupFields) {
        return this.maxBy(field, new JsonObject(), groupFields);
    }

    public Future<JsonArray> maxByAsync(final String field, final JsonObject criteria, final String... groupFields) {
        return Future.succeededFuture(this.maxBy(field, criteria, groupFields));
    }

    public Future<JsonArray> maxByAsync(final String field, final String... groupFields) {
        return Future.succeededFuture(this.maxBy(field, new JsonObject(), groupFields));
    }

    // -------------------- Min Operation ------------
    /*
     * min(String)
     * min(String, JsonObject)
     * min(String, JsonObject, pojo)
     * minAsync(String)
     * minAsync(String, JsonObject)
     * minAsync(String, JsonObject, pojo)
     */
    public BigDecimal min(final String field) {
        return this.aggregator.min(field, null);
    }

    public Future<BigDecimal> minAsync(final String field) {
        return Future.succeededFuture(this.aggregator.min(field, null));
    }

    public BigDecimal min(final String field, final JsonObject criteria) {
        return this.aggregator.min(field, this.workflow.inputQrJ(criteria));
    }

    public Future<BigDecimal> minAsync(final String field, final JsonObject criteria) {
        return this.workflow.inputQrJAsync(criteria)
            .compose(processed -> Future.succeededFuture(this.aggregator.min(field, processed)));
    }

    public BigDecimal min(final String field, final JsonObject criteria, final String pojo) {
        return this.aggregator.min(field, JqFlow.create(this.analyzer, pojo).inputQrJ(criteria));
    }

    public Future<BigDecimal> minAsync(final String field, final JsonObject criteria, final String pojo) {
        return JqFlow.create(this.analyzer, pojo).inputQrJAsync(criteria)
            .compose(processed -> Future.succeededFuture(this.aggregator.min(field, processed)));
    }

    /*
     * minBy(String, JsonObject, String)
     *      <-- minBy(String, String)
     *      <-- minByAsync(String, String)
     *      <-- minByAsync(String, JsonObject, String)
     * minBy(String, JsonObject, String...)
     *      <-- minBy(String, String...)
     *      <-- minByAsync(String, String...)
     *      <-- minByAsync(String, JsonObject, String...)
     */

    public ConcurrentMap<String, BigDecimal> minBy(final String field, final JsonObject criteria, final String groupField) {
        return this.aggregator.min(field, this.workflow.inputQrJ(criteria), groupField);
    }

    public ConcurrentMap<String, BigDecimal> minBy(final String field, final String groupField) {
        return this.minBy(field, new JsonObject(), groupField);
    }

    public Future<ConcurrentMap<String, BigDecimal>> minByAsync(final String field, final String groupField) {
        return Future.succeededFuture(this.minBy(field, new JsonObject(), groupField));
    }

    public Future<ConcurrentMap<String, BigDecimal>> minByAsync(final String field, final JsonObject criteria, final String groupField) {
        return Future.succeededFuture(this.minBy(field, criteria, groupField));
    }

    public JsonArray minBy(final String field, final JsonObject criteria, final String... groupFields) {
        return this.aggregator.min(field, this.workflow.inputQrJ(criteria), groupFields);
    }

    public JsonArray minBy(final String field, final String... groupFields) {
        return this.minBy(field, new JsonObject(), groupFields);
    }

    public Future<JsonArray> minByAsync(final String field, final JsonObject criteria, final String... groupFields) {
        return Future.succeededFuture(this.minBy(field, criteria, groupFields));
    }

    public Future<JsonArray> minByAsync(final String field, final String... groupFields) {
        return Future.succeededFuture(this.minBy(field, new JsonObject(), groupFields));
    }


    // -------------------- Avg Operation ------------
    /*
     * avg(String)
     * avg(String, JsonObject)
     * avg(String, JsonObject, pojo)
     * avgAsync(String)
     * avgAsync(String, JsonObject)
     * avgAsync(String, JsonObject, pojo)
     */
    public BigDecimal avg(final String field) {
        return this.aggregator.avg(field, null);
    }

    public Future<BigDecimal> avgAsync(final String field) {
        return Future.succeededFuture(this.aggregator.avg(field, null));
    }

    public BigDecimal avg(final String field, final JsonObject criteria) {
        return this.aggregator.avg(field, this.workflow.inputQrJ(criteria));
    }

    public Future<BigDecimal> avgAsync(final String field, final JsonObject criteria) {
        return this.workflow.inputQrJAsync(criteria)
            .compose(processed -> Future.succeededFuture(this.aggregator.avg(field, processed)));
    }

    public BigDecimal avg(final String field, final JsonObject criteria, final String pojo) {
        return this.aggregator.avg(field, JqFlow.create(this.analyzer, pojo).inputQrJ(criteria));
    }

    public Future<BigDecimal> avgAsync(final String field, final JsonObject criteria, final String pojo) {
        return JqFlow.create(this.analyzer, pojo).inputQrJAsync(criteria)
            .compose(processed -> Future.succeededFuture(this.aggregator.avg(field, processed)));
    }

    /*
     * avgBy(String, JsonObject, String)
     *      <-- avgBy(String, String)
     *      <-- avgByAsync(String, String)
     *      <-- avgByAsync(String, JsonObject, String)
     * avgBy(String, JsonObject, String...)
     *      <-- avgBy(String, String...)
     *      <-- avgByAsync(String, String...)
     *      <-- avgByAsync(String, JsonObject, String...)
     */

    public ConcurrentMap<String, BigDecimal> avgBy(final String field, final JsonObject criteria, final String groupField) {
        return this.aggregator.avg(field, this.workflow.inputQrJ(criteria), groupField);
    }

    public ConcurrentMap<String, BigDecimal> avgBy(final String field, final String groupField) {
        return this.avgBy(field, new JsonObject(), groupField);
    }

    public Future<ConcurrentMap<String, BigDecimal>> avgByAsync(final String field, final String groupField) {
        return Future.succeededFuture(this.avgBy(field, new JsonObject(), groupField));
    }

    public Future<ConcurrentMap<String, BigDecimal>> avgByAsync(final String field, final JsonObject criteria, final String groupField) {
        return Future.succeededFuture(this.avgBy(field, criteria, groupField));
    }

    public JsonArray avgBy(final String field, final JsonObject criteria, final String... groupFields) {
        return this.aggregator.avg(field, this.workflow.inputQrJ(criteria), groupFields);
    }

    public JsonArray avgBy(final String field, final String... groupFields) {
        return this.avgBy(field, new JsonObject(), groupFields);
    }

    public Future<JsonArray> avgByAsync(final String field, final JsonObject criteria, final String... groupFields) {
        return Future.succeededFuture(this.avgBy(field, criteria, groupFields));
    }

    public Future<JsonArray> avgByAsync(final String field, final String... groupFields) {
        return Future.succeededFuture(this.avgBy(field, new JsonObject(), groupFields));
    }
}
