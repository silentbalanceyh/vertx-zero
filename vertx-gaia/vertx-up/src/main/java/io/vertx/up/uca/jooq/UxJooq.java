package io.vertx.up.uca.jooq;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.Inquiry;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.em.Format;
import io.vertx.up.exception.zero.JooqClassInvalidException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.jooq.util.JqFlow;
import io.vertx.up.uca.jooq.util.JqTool;
import io.vertx.up.util.Ut;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiPredicate;

@SuppressWarnings("all")
public class UxJooq {

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
    private transient Format format = Format.JSON;

    public <T> UxJooq(final Class<T> clazz, final VertxDAO vertxDAO) {
        /* New exception to avoid programming missing */
        Fn.out(!Ut.isImplement(clazz, VertxDAO.class), JooqClassInvalidException.class, UxJooq.class, clazz.getName());
        this.clazz = clazz;

        /* Analyzing column for Jooq */
        this.analyzer = JqAnalyzer.create(vertxDAO);
        this.aggregator = JqAggregator.create(analyzer);

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
        return this;
    }

    public UxJooq on(final Format format) {
        this.format = format;
        return this;
    }

    public Set<String> columns() {
        return this.analyzer.columns().keySet();
    }

    public String table() {
        return this.analyzer.table();
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
     *      <-- insertAsyncJ(T)
     *      <-- insertAsyncJ(JsonObject, pojo)
     *      <-- insertAsyncJ(JsonObject)
     * */
    public <T> Future<T> insertAsync(final T entity) {
        return this.writer.insertAsync(entity);
    }

    public <T> Future<T> insertAsync(final JsonObject data) {
        return workflow.<T>inputAsync(data).compose(this::insertAsync);
    }

    public <T> Future<T> insertAsync(final JsonObject data, final String pojo) {
        return JqFlow.create(this.analyzer, pojo).<T>inputAsync(data).compose(this::insertAsync);
    }

    public <T> Future<JsonObject> insertAsyncJ(final T entity) {
        return this.insertAsync(entity).compose(workflow::outputAsync);
    }

    public <T> Future<JsonObject> insertAsyncJ(final JsonObject data) {
        return workflow.<T>inputAsync(data).compose(this::insertAsync).compose(workflow::outputAsync);
    }

    public <T> Future<JsonObject> insertAsyncJ(final JsonObject data, final String pojo) {
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
        return this.insert((T) workflow.input(data));
    }

    public <T> T insert(final JsonObject data, final String pojo) {
        return this.insert((T) JqFlow.create(this.analyzer, pojo).input(data));
    }

    public <T> JsonObject insertJ(final T entity) {
        return workflow.output(this.insert(entity));
    }

    public <T> JsonObject insertJ(final JsonObject data) {
        return workflow.output(this.insert((T) workflow.input(data)));  // T & List<T> Diff
    }

    public <T> JsonObject insertJ(final JsonObject data, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return flow.output(this.insert((T) flow.input(data)));          // T & List<T> Diff
    }

    /*
     * insertAsync(List<T>)
     *  <-- insertAsync(JsonArray)
     *  <-- insertAsync(JsonArray,pojo)
     *  <-- insertAsyncJ(List<T>)
     *  <-- insertAsyncJ(JsonArray)
     *  <-- insertAsyncJ(JsonArray, pojo)
     */
    public <T> Future<List<T>> insertAsync(final List<T> entities) {
        return this.writer.insertAsync(entities);
    }

    public <T> Future<List<T>> insertAsync(final JsonArray input) {
        return workflow.<T>inputAsync(input).compose(this::insertAsync);          // --> `insertAsync(List<T>)`
    }

    public <T> Future<List<T>> insertAsync(final JsonArray input, final String pojo) {
        return JqFlow.create(this.analyzer, pojo).<T>inputAsync(input).compose(this::insertAsync);         // --> `insertAsync(List<T>)`
    }

    public <T> Future<JsonArray> insertAsyncJ(final List<T> list) {
        return this.insertAsync(list).compose(workflow::outputAsync);
    }

    public <T> Future<JsonArray> insertAsyncJ(final JsonArray input) {
        return workflow.<T>inputAsync(input).compose(this::insertAsync).compose(workflow::outputAsync);
    }

    public <T> Future<JsonArray> insertAsyncJ(final JsonArray input, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return flow.<T>inputAsync(input).compose(this::insertAsync).compose(flow::outputAsync);
    }


    /*
     * insert(List<T>)
     *  <-- insert(JsonArray)
     *  <-- insert(JsonArray,pojo)
     *  <-- insertJ(List<T>)
     *  <-- insertJ(JsonArray)
     *  <-- insertJ(JsonArray, pojo)
     */
    public <T> List<T> insert(final List<T> entities) {
        return null;
    }

    public <T> List<T> insert(final JsonArray data) {
        return this.insert(workflow.input(data));
    }

    public <T> List<T> insert(final JsonArray data, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return this.insert(flow.input(data));
    }

    public <T> JsonArray insertJ(final List<T> list) {
        return workflow.output(this.insert(list));
    }

    public <T> JsonArray insertJ(final JsonArray data) {
        return workflow.output(this.insert(workflow.input(data)));
    }

    public <T> JsonArray insertJ(final JsonArray data, final String pojo) {
        final JqFlow flow = JqFlow.create(this.analyzer, pojo);
        return flow.<T>output(this.insert(flow.input(data)));
    }

    // -------------------- Search Operation -----------
    /* (Async / Sync) Sort, Projection, Criteria, Pager Search Operations */
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
    // -------------------- Fetch Operation -----------

    // -------------------- Fetch List -------------------
    /* (Async / Sync) Find All */
    public <T> Future<List<T>> fetchAllAsync() {
        return this.reader.fetchAllAsync();
    }

    public <T> List<T> fetchAll() {
        return this.reader.fetchAll();
    }

    public <T> List<T> fetch(final String field, final Object value) {
        return this.reader.fetch(field, value);
    }

    public <T> Future<List<T>> fetchAsync(final String field, final Object value) {
        return this.reader.fetchAsync(field, value);
    }

    public <T> Future<List<T>> fetchAsync(final JsonObject criteria) {
        return this.reader.fetchAsync(criteria, this.workflow);
    }

    public <T> Future<List<T>> fetchAsync(final JsonObject criteria, final String pojo) {
        return this.reader.fetchAsync(criteria, JqFlow.create(this.analyzer, pojo));
    }

    public <T> List<T> fetch(final JsonObject criteria) {
        return this.reader.fetch(criteria, this.workflow);
    }

    public <T> List<T> fetch(final JsonObject criteria, final String pojo) {
        return this.reader.fetch(criteria, JqFlow.create(this.analyzer, pojo));
    }

    public <T> Future<List<T>> fetchAndAsync(final JsonObject filters) {
        filters.put(Strings.EMPTY, Boolean.TRUE);
        return this.fetchAsync(filters);
    }

    public <T> List<T> fetchAnd(final JsonObject filters) {
        filters.put(Strings.EMPTY, Boolean.TRUE);
        return this.fetch(filters);
    }

    public <T> Future<List<T>> fetchOrAsync(final JsonObject orFilters) {
        orFilters.put(Strings.EMPTY, Boolean.FALSE);
        return this.fetchAsync(orFilters);
    }

    public <T> List<T> fetchOr(final JsonObject orFilters) {
        orFilters.put(Strings.EMPTY, Boolean.FALSE);
        return this.fetch(orFilters);
    }

    public <T> Future<List<T>> fetchInAsync(final String field, final Object... value) {
        return this.fetchAsync(field, Arrays.asList(value));
    }

    public <T> Future<List<T>> fetchInAsync(final String field, final JsonArray values) {
        return this.fetchAsync(field, values.getList());
    }

    public <T> List<T> fetchIn(final String field, final Object... values) {
        return this.fetch(field, Arrays.asList(values));
    }

    public <T> List<T> fetchIn(final String field, final JsonArray values) {
        return this.fetch(field, values.getList());
    }

    /* (Async / Sync) Find / Exist / Missing By Filters Operation */
    // -------------------- UPDATE --------------------
    /* Async Only
     * Disabled increament primary key processing, this method is not used in our system once,
     * In distributed system environment, it's no usage.
     *
     * public <T> Future<T> upsertReturningPrimaryAsync(final JsonObject filters, final T updated, final Consumer<Long> consumer) {
     *    return this.writer.upsertReturningPrimaryAsync(filters, updated, consumer);
     * }
     */

    /* (Async / Sync) Entity Update */
    public <T> Future<T> updateAsync(final T entity) {
        return this.writer.updateAsync(entity);
    }

    public <T> T update(final T entity) {
        return this.writer.update(entity);
    }


    /* (Async / Sync) Collection Update */
    public <T> Future<List<T>> updateAsync(final List<T> entities) {
        return this.writer.updateAsync(entities);
    }

    public <T> List<T> update(final List<T> entities) {
        return this.writer.update(entities);
    }

    /* (Async / Sync) Save Operations */
    public <T> Future<T> updateAsync(final Object id, final T updated) {
        return this.writer.updateAsync(id, updated);
    }

    public <T> T update(final Object id, final T updated) {
        return this.writer.update(id, updated);
    }


    // -------------------- DELETE --------------------
    /* (Async / Sync) Delete By ( ID / IDs ) */
    public <ID> Future<Boolean> deleteByIdAsync(final ID id) {
        return this.writer.<ID>deleteByIdAsync(id);
    }

    public <ID> Future<Boolean> deleteByIdAsync(final ID... ids) {
        return this.writer.<ID>deleteByIdAsync(Arrays.asList(ids));
    }

    public <ID> Boolean deleteById(final ID id) {
        return this.writer.<ID>deleteById(id);
    }

    public <ID> Boolean deleteById(final ID... ids) {
        return this.writer.<ID>deleteById(Arrays.asList(ids));
    }


    /* (Async / Sync) Delete Entity */
    public <T> Future<T> deleteAsync(final T entity) {
        return this.writer.<T>deleteAsync(entity);
    }

    public <T> Future<List<T>> deleteAsync(final List<T> entity) {
        return this.writer.<T>deleteAsync(entity);
    }

    public <T> T delete(final T entity) {
        return this.writer.<T>delete(entity);
    }

    public <T> List<T> delete(final List<T> entity) {
        return this.writer.<T>delete(entity);
    }


    /* (Async / Sync) Delete by Filters */
    public <T, ID> Future<Boolean> deleteAsync(final JsonObject filters) {
        return this.writer.<T, ID>deleteAsync(filters, this.analyzer.pojoFile());
    }

    public <T, ID> Boolean delete(final JsonObject filters) {

        return this.writer.<T, ID>delete(filters, this.analyzer.pojoFile());
    }

    // -------------------- Upsert ---------
    public <T> Future<List<T>> upsertAsync(final JsonObject filters, final List<T> list, final BiPredicate<T, T> fnCombine) {
        return this.writer.upsertAsync(filters, list, fnCombine);
    }

    public <T> Future<T> upsertAsync(final JsonObject filters, final T updated) {
        return this.writer.upsertAsync(filters, updated);
    }

    public <T> Future<T> upsertAsync(final String key, final T updated) {
        return this.writer.upsertAsync(key, updated);
    }

    public <T> List<T> upsert(final JsonObject filters, final List<T> list, final BiPredicate<T, T> fnCombine) {
        return this.writer.upsert(filters, list, fnCombine);
    }

    public <T> T upsert(final JsonObject filters, final T updated) {
        return this.writer.upsert(filters, updated);
    }

    public <T> T upsert(final String key, final T updated) {
        return this.writer.upsert(key, updated);
    }

    // -------------------- Fetch One/All --------------------

    /* (Async / Sync) Fetch One */
    public <T> Future<T> fetchOneAsync(final String field, final Object value) {
        return this.reader.fetchOneAsync(field, value);
    }

    public <T> T fetchOne(final String field, final Object value) {
        return this.reader.fetchOne(field, value);
    }

    public <T> Future<T> fetchOneAsync(final JsonObject filters) {
        return this.reader.fetchOneAsync(filters);
    }

    public <T> T fetchOne(final JsonObject filters) {
        return this.reader.fetchOne(filters);
    }

    /* (Async / Sync) Find By ID */
    public <T> Future<T> fetchByIdAsync(final Object id) {
        return this.reader.fetchByIdAsync(id);
    }

    public <T> T fetchById(final Object id) {
        return this.reader.fetchById(id);
    }

    // -------------------- Exist Operation --------------------
    /* (Async / Sync) Exist By ID Operation */
    public Future<Boolean> existByIdAsync(final Object id) {
        return this.reader.existsByIdAsync(id);
    }

    public Boolean existById(final Object id) {
        return this.reader.existsById(id);
    }

    public Future<Boolean> missByIdAsync(final Object id) {
        return this.reader.existsByIdAsync(id).compose(result -> Future.succeededFuture(!result));
    }

    public Boolean missById(final Object id) {
        return !this.reader.existsById(id);
    }

    public <T> Future<Boolean> missAsync(final JsonObject filters) {
        return this.<T>fetchAsync(filters)
                .compose(item -> Future.succeededFuture(0 == item.size()));
    }

    public <T> Future<Boolean> existAsync(final JsonObject filters) {
        return this.<T>fetchAsync(filters)
                .compose(item -> Future.succeededFuture(0 < item.size()));
    }

    public <T> Boolean exist(final JsonObject filters) {
        final List<T> list = fetch(filters);
        return 0 < list.size();
    }

    public <T> Boolean miss(final JsonObject filters) {
        final List<T> list = fetch(filters);
        return 0 == list.size();
    }

    // -------------------- Count Operation ------------
    /* (Async / Sync) Count Operation */
    public Future<Long> countAsync(final JsonObject params, final String pojo) {
        final Inquiry inquiry = JqTool.getInquiry(params, pojo);
        return this.aggregator.countAsync(inquiry);
    }

    public Future<Long> countAsync(final JsonObject params) {
        return countAsync(params, this.analyzer.pojoFile());
    }

    public Long count(final JsonObject params, final String pojo) {
        final Inquiry inquiry = JqTool.getInquiry(params, pojo);
        return this.aggregator.count(inquiry);
    }

    public Long count(final JsonObject params) {
        return count(params, this.analyzer.pojoFile());
    }

    /* (Async / Sync) Count By Operation */
    public Future<ConcurrentMap<String, Integer>> countByAsync(final String groupField) {
        return this.aggregator.countByAsync(new JsonObject(), groupField);
    }

    public ConcurrentMap<String, Integer> countBy(final String groupField) {
        return this.aggregator.countBy(new JsonObject(), groupField);
    }

    public Future<ConcurrentMap<String, Integer>> countByAsync(final JsonObject params, final String groupField) {
        return this.aggregator.countByAsync(params, groupField);
    }

    public ConcurrentMap<String, Integer> countBy(final JsonObject params, final String groupField) {
        return this.aggregator.countBy(params, groupField);
    }

    // -------------------- Group Operation ------------
    public <T> ConcurrentMap<String, List<T>> group(final String field) {
        return this.aggregator.group(new JsonObject(), field);
    }

    public Future<JsonArray> groupAsync(final JsonObject params, final String... groupFields) {
        return this.groupAsync(this.analyzer.pojoFile(), params, groupFields);
    }

    public Future<JsonArray> groupAsync(final String pojo, final JsonObject params, final String... groupFields) {
        final Inquiry inquiry = JqTool.getInquiry(params, pojo);
        return this.aggregator.groupByAsync(inquiry.toJson(), groupFields);
    }
}
