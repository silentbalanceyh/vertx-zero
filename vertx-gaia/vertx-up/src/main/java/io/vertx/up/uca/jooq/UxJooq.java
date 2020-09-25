package io.vertx.up.uca.jooq;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.jooq.condition.JooqCond;
import io.vertx.up.atom.query.Inquiry;
import io.vertx.up.eon.em.Format;
import io.vertx.up.exception.zero.JooqClassInvalidException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;
import org.jooq.Operator;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

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
    }

    // -------------------- Bind Config --------------------
    /*
     * Bind configuration range here
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

    /* Async Only */
    public <T> Future<T> insertReturningPrimaryAsync(final T entity, final Consumer<Long> consumer) {
        return this.writer.insertReturningPrimaryAsync(entity, consumer);
    }

    /* (Async / Sync) Entity Insert */
    public <T> Future<T> insertAsync(final T entity) {
        return this.writer.insertAsync(entity);
    }

    public <T> T insert(final T entity) {
        return this.writer.insert(entity);
    }


    /* (Async / Sync) Collection Insert */
    public <T> Future<List<T>> insertAsync(final List<T> entities) {
        return this.writer.insertAsync(entities);
    }

    public <T> List<T> insert(final List<T> entities) {
        return this.writer.insert(entities);
    }

    // -------------------- UPDATE --------------------
    /* Async Only */
    public <T> Future<T> upsertReturningPrimaryAsync(final JsonObject filters, final T updated, final Consumer<Long> consumer) {
        return this.writer.upsertReturningPrimaryAsync(filters, updated, consumer);
    }

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

    public <T> T delete(final T entity) {
        return this.writer.<T>delete(entity);
    }


    /* (Async / Sync) Delete by Filters */
    public <T, ID> Future<Boolean> deleteAsync(final JsonObject filters) {
        return this.writer.<T, ID>deleteAsync(filters, "");
    }

    public <T, ID> Boolean delete(final JsonObject filters, final String pojo) {
        return this.writer.<T, ID>delete(filters, pojo);
    }

    public <T, ID> Future<Boolean> deleteAsync(final JsonObject filters, final String pojo) {
        return this.writer.<T, ID>deleteAsync(filters, pojo);
    }

    public <T, ID> Boolean delete(final JsonObject filters) {

        return this.writer.<T, ID>delete(filters, "");
    }


    // -------------------- Save Operation --------------------
    /* (Async / Sync) Save Operations */
    public <T> Future<T> saveAsync(final Object id, final T updated) {
        return this.writer.saveAsync(id, updated);
    }

    public <T> T save(final Object id, final T updated) {
        return this.writer.save(id, updated);
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
        return this.reader.fetchOneAndAsync(filters);
    }

    public <T> T fetchOne(final JsonObject filters) {
        return this.reader.fetchOneAnd(filters);
    }

    /* (Async / Sync) Find By ID */
    public <T> Future<T> findByIdAsync(final Object id) {
        return this.reader.findByIdAsync(id);
    }

    public <T> T findById(final Object id) {
        return this.reader.findById(id);
    }


    /* (Async / Sync) Find All */
    public <T> Future<List<T>> findAllAsync() {
        return this.reader.findAllAsync();
    }

    public <T> List<T> findAll() {
        return this.reader.findAll();
    }

    // -------------------- Exist Operation --------------------
    /* (Async / Sync) Exist By ID Operation */
    public Future<Boolean> existsByIdAsync(final Object id) {
        return this.reader.existsByIdAsync(id);
    }

    public Boolean existsById(final Object id) {
        return this.reader.existsById(id);
    }


    /* (Async / Sync) Exist By Filters Operation */
    public <T> Future<Boolean> existsOneAsync(final JsonObject filters) {
        return this.<T>fetchOneAsync(filters)
                .compose(item -> Future.succeededFuture(null != item));
    }

    public <T> Boolean existsOne(final JsonObject filters) {
        final T result = this.<T>fetchOne(filters);
        return null != result;
    }

    // -------------------- Fetch List Operation ---------------
    /* (Async / Sync) Fetch to List<T> Operation */
    public <T> Future<List<T>> fetchAsync(final String field, final Object value) {
        return this.reader.fetchAsync(field, value);
    }

    public <T> Future<List<T>> fetchInAsync(final String field, final Object... value) {
        return this.reader.fetchInAsync(field, Arrays.asList(value));
    }

    public <T> Future<List<T>> fetchInAsync(final String field, final JsonArray values) {
        return this.reader.fetchInAsync(field, values.getList());
    }

    public <T> List<T> fetch(final String field, final Object value) {
        return this.reader.fetch(field, value);
    }

    public <T> List<T> fetchIn(final String field, final Object... values) {
        return this.reader.fetchIn(field, Arrays.asList(values));
    }

    public <T> List<T> fetchIn(final String field, final JsonArray values) {
        return this.reader.fetchIn(field, values.getList());
    }

    // -------------------- Search Operation -----------------
    /* (Async / Sync) Find / Exist / Missing By Filters Operation */
    public <T> Future<List<T>> findAsync(final JsonObject filters) {
        return this.reader.searchAsync(filters);
    }

    public <T> Future<Boolean> findExistingAsync(final JsonObject filters) {
        return this.<T>findAsync(filters)
                .compose(item -> Future.succeededFuture(0 < item.size()));
    }

    public <T> Future<Boolean> findMissingAsync(final JsonObject filters) {
        return this.<T>findAsync(filters)
                .compose(item -> Future.succeededFuture(0 == item.size()));
    }

    public <T> List<T> find(final JsonObject filters) {
        return this.reader.search(filters);
    }

    public <T> Boolean findExisting(final JsonObject filters) {
        final List<T> list = find(filters);
        return 0 < list.size();
    }

    public <T> Boolean findMissing(final JsonObject filters) {
        final List<T> list = find(filters);
        return 0 == list.size();
    }

    // -------------------- Count Operation ------------
    /* (Async / Sync) Count Operation */
    public Future<Integer> countAsync(final JsonObject params, final String pojo) {
        final Inquiry inquiry = JqTool.getInquiry(params, pojo);
        return this.aggregator.countAsync(inquiry);
    }

    public Future<Integer> countAsync(final JsonObject params) {
        return countAsync(params, this.analyzer.pojoFile());
    }

    public Future<ConcurrentMap<String, Integer>> countByAsync(final JsonObject params, final String groupField) {
        return this.aggregator.countByAsync(params, groupField);
    }

    public Integer count(final JsonObject params, final String pojo) {
        final Inquiry inquiry = JqTool.getInquiry(params, pojo);
        return this.aggregator.count(inquiry);
    }

    public Integer count(final JsonObject params) {
        return count(params, this.analyzer.pojoFile());
    }

    // -------------------- Group Operation ------------
    public Future<JsonArray> groupAsync(final JsonObject params, final String... groupFields) {
        return this.groupAsync(this.analyzer.pojoFile(), params, groupFields);
    }

    public Future<JsonArray> groupAsync(final String pojo, final JsonObject params, final String... groupFields) {
        final Inquiry inquiry = JqTool.getInquiry(params, pojo);
        return this.aggregator.groupByAsync(inquiry.toJson(), groupFields);
    }

    // -------------------- Search Operation -----------
    /* (Async / Sync) Sort, Projection, Criteria, Pager Search Operations */
    public Future<JsonObject> searchAsync(final JsonObject params, final String pojo) {
        final Inquiry inquiry = JqTool.getInquiry(params, pojo);
        return searchAsync(inquiry, pojo);
    }

    public Future<JsonObject> searchAsync(final JsonObject params) {
        return searchAsync(params, this.analyzer.pojoFile());
    }

    public Future<JsonObject> searchAsync(final Inquiry inquiry, final String pojo) {
        return this.reader.searchPaginationAsync(inquiry, pojo);
    }

    public JsonObject search(final JsonObject params, final String pojo) {
        final Inquiry inquiry = JqTool.getInquiry(params, pojo);
        return search(inquiry, pojo);
    }

    public JsonObject search(final JsonObject params) {
        return search(params, this.analyzer.pojoFile());
    }

    public JsonObject search(final Inquiry inquiry, final String pojo) {
        return this.reader.searchPagination(inquiry, pojo);
    }

    // -------------------- Fetch List -------------------
    public <T> Future<List<T>> fetchAndAsync(final JsonObject filters) {
        return this.reader.fetchAsync(JooqCond.transform(filters, Operator.AND, this.analyzer::column));
    }

    public <T> List<T> fetchAnd(final JsonObject filters) {
        return this.reader.fetch(JooqCond.transform(filters, Operator.AND, this.analyzer::column));
    }

    public <T> Future<List<T>> fetchOrAsync(final JsonObject orFilters) {
        return this.reader.fetchAsync(JooqCond.transform(orFilters, Operator.OR, this.analyzer::column));
    }

    public <T> List<T> fetchOr(final JsonObject orFilters) {
        return this.reader.fetch(JooqCond.transform(orFilters, Operator.OR, this.analyzer::column));
    }
}
