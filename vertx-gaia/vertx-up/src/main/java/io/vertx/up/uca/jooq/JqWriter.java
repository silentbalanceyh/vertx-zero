package io.vertx.up.uca.jooq;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.jooq.condition.JooqCond;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.jooq.Condition;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Jooq Splitted Writter
 * Create
 * Update
 * Delete
 */
@SuppressWarnings("all")
class JqWriter {

    private transient final VertxDAO vertxDAO;
    private transient JqReader reader;
    private transient JqAnalyzer analyzer;

    private JqWriter(final VertxDAO vertxDAO, final JqAnalyzer analyzer) {
        this.vertxDAO = vertxDAO;
        this.analyzer = analyzer;
        this.reader = JqReader.create(vertxDAO, analyzer);
    }

    static JqWriter create(final VertxDAO vertxDAO, final JqAnalyzer analyzer) {
        return new JqWriter(vertxDAO, analyzer);
    }

    // ============ INSERT Operation =============
    <T> Future<T> upsertReturningPrimaryAsync(final JsonObject filters, final T updated, final Consumer<Long> consumer) {
        return this.reader.<T>fetchOneAndAsync(filters).compose(item -> Fn.match(
                Fn.fork(() -> this.<T>updateAsync(this.analyzer.copyEntity(item, updated))),
                Fn.branch(null == item, () -> this.insertReturningPrimaryAsync(updated, consumer))
        ));
    }

    /* Async insert operation with key returned: INSERT ( AUTO INCREAMENT ) */
    <T> Future<T> insertReturningPrimaryAsync(final T entity,
                                              final Consumer<Long> consumer) {
        final CompletableFuture<Long> future = this.vertxDAO.insertReturningPrimaryAsync(entity);
        return JqTool.future(future).compose(id -> {
            if (null != consumer) consumer.accept(id);
            return Future.succeededFuture(entity);
        });
    }

    /* Async insert operation: INSERT */
    <T> Future<T> insertAsync(final T entity) {
        final CompletableFuture<Void> future = this.vertxDAO.insertAsync(uuid(entity));
        return JqTool.future(future).compose(nil -> Future.succeededFuture(entity));
    }

    <T> Future<List<T>> insertAsync(final List<T> entities) {
        entities.forEach(this::uuid);
        final CompletableFuture<Void> future = this.vertxDAO.insertAsync(entities);
        return JqTool.future(future).compose(nil -> Future.succeededFuture(entities));
    }

    /* Sync insert operation: INSERT */
    <T> T insert(final T entity) {
        this.vertxDAO.insert(uuid(entity));
        return entity;
    }

    <T> List<T> insert(final List<T> entities) {
        entities.forEach(this::uuid);
        this.vertxDAO.insert(entities);
        return entities;
    }

    // ============ UPDATE Operation =============

    /* Async insert operation: UPDATE */
    <T> Future<T> updateAsync(final T entity) {
        final CompletableFuture<Void> future = this.vertxDAO.updateAsync(entity);
        return JqTool.future(future).compose(nil -> Future.succeededFuture(entity));
    }

    <T> Future<List<T>> updateAsync(final List<T> entities) {
        final CompletableFuture<Void> future = this.vertxDAO.updateAsync(entities);
        return JqTool.future(future).compose(nil -> Future.succeededFuture(entities));
    }

    /* Sync insert operation: UPDATE */
    <T> T update(final T entity) {
        this.vertxDAO.update(entity);
        return entity;
    }

    <T> List<T> update(final List<T> entities) {
        this.vertxDAO.update(entities);
        return entities;
    }

    // ============ DELETE Operation =============

    /* Async delete operation: DELETE */
    <T> Future<T> deleteAsync(final T entity) {
        final CompletableFuture<Void> future = this.vertxDAO.deleteAsync(Arrays.asList(entity));
        return JqTool.future(future).compose(nil -> Future.succeededFuture(entity));
    }

    <ID> Future<Boolean> deleteByIdAsync(final ID id) {
        final CompletableFuture<Void> future = this.vertxDAO.deleteByIdAsync(id);
        return JqTool.future(future).compose(nil -> Future.succeededFuture(Boolean.TRUE));
    }

    <ID> Future<Boolean> deleteByIdAsync(final Collection<ID> ids) {
        final CompletableFuture<Void> future = this.vertxDAO.deleteByIdAsync(ids);
        return JqTool.future(future).compose(nil -> Future.succeededFuture(Boolean.TRUE));
    }

    <T, ID> Future<Boolean> deleteAsync(final JsonObject filters, final String pojo) {
        final Condition condition = JooqCond.transform(filters, null, this.analyzer::column);
        final CompletableFuture<Integer> deleted = this.vertxDAO.deleteExecAsync(condition);
        return JqTool.future(deleted).compose(nil -> Future.succeededFuture(Boolean.TRUE));
    }

    /* Sync delete operation: DELETE */
    <T> T delete(final T entity) {
        this.vertxDAO.delete(entity);
        return entity;
    }

    <ID> Boolean deleteById(final ID id) {
        this.vertxDAO.deleteById(id);
        return Boolean.TRUE;
    }

    <ID> Boolean deleteById(final Collection<ID> ids) {
        this.vertxDAO.deleteById(ids);
        return Boolean.TRUE;
    }

    <T, ID> Boolean delete(final JsonObject filters, final String pojo) {
        final Condition condition = JooqCond.transform(filters, null, this.analyzer::column);
        final List<T> result = this.reader.fetch(condition);
        result.stream().map(item -> this.delete(item));
        return Boolean.TRUE;
    }

    // ============ UPDATE Operation (Save) =============

    <T> T save(final Object id, final T updated) {
        return this.save(id, target -> this.analyzer.copyEntity(target, updated));
    }

    <T> Future<T> saveAsync(final Object id, final T updated) {
        return this.saveAsync(id, target -> this.analyzer.copyEntity(target, updated));
    }
    // ============ UPSERT Operation (Save) =============

    public <T> Future<T> upsertAsync(final JsonObject filters, final T updated) {
        return combineAsync(this.reader.<T>fetchOneAndAsync(filters), updated);
    }

    public <T> Future<T> upsertAsync(final String key, final T updated) {
        return combineAsync(this.reader.<T>findByIdAsync(key), updated);
    }

    public <T> T upsert(final JsonObject filters, final T updated) {
        return this.combine(this.reader.fetchOneAnd(filters), updated);
    }

    public <T> T upsert(final String key, final T updated) {
        return this.combine(this.reader.findById(key), updated);
    }

    <T> List<T> upsert(final JsonObject filters, final List<T> list, final BiPredicate<T, T> fnCombine) {
        final List<T> original = this.reader.<T>search(filters);
        /*
         * Query data by filters
         */
        final ConcurrentMap<ChangeFlag, List<T>> queueMap =
                JqTool.compared(list, original, fnCombine, this.analyzer::copyEntity);
        /*
         * Insert & Update
         */
        final List<T> resultList = new ArrayList<>();
        resultList.addAll(this.insert(queueMap.get(ChangeFlag.ADD)));
        resultList.addAll(this.update(queueMap.get(ChangeFlag.UPDATE)));
        return resultList;
    }

    <T> Future<List<T>> upsertAsync(final JsonObject filters, final List<T> list, final BiPredicate<T, T> fnCombine) {
        /*
         * Query data by filters ( This filters should fetch all condition list as List<T> )
         * original
         */
        return this.reader.<T>searchAsync(filters).compose(original -> {
            /*
             * Combine original / and last list
             */
            final ConcurrentMap<ChangeFlag, List<T>> queueMap =
                    JqTool.compared(list, original, fnCombine, this.analyzer::copyEntity);
            /*
             * Insert & Update
             */
            final List<Future<List<T>>> futures = new ArrayList<>();
            futures.add(this.insertAsync(queueMap.get(ChangeFlag.ADD)));
            futures.add(this.updateAsync(queueMap.get(ChangeFlag.UPDATE)));
            return Ux.thenCombineArrayT(futures);
        });
    }

    // ------------ Private Method inner Writer ---------

    /*
     * Saving operation
     */
    private <T> Future<T> saveAsync(final Object id, final Function<T, T> copyFun) {
        return this.reader.<T>findByIdAsync(id).compose(old -> this.<T>updateAsync(copyFun.apply(old)));
    }

    private <T> T save(final Object id, final Function<T, T> copyFun) {
        final T old = this.reader.<T>findById(id);
        return copyFun.apply(old);
    }

    /*
     * Combine for `Saving` here, it could help Jooq to execute
     * Saving operation.
     *
     * Insert / Update merged
     * 1 Existing: do updating
     * 2 Missing: do missing
     */
    private <T> Future<T> combineAsync(final Future<T> queried, final T updated) {
        return queried.compose(item -> Fn.match(
                // null != item, updated to existing item.
                Fn.fork(() -> this.<T>updateAsync(this.analyzer.copyEntity(item, updated))),
                // null == item, insert data
                Fn.branch(null == item, () -> this.insertAsync(updated))
        ));
    }

    private <T> T combine(final T queried, final T updated) {
        if (null == queried) {
            return this.insert(updated);
        } else {
            return this.update(this.analyzer.copyEntity(queried, updated));
        }
    }

    /*
     * When primary key is String and missed `KEY` field in zero database,
     * Here generate default uuid key, otherwise the `key` field will be ignored.
     * This feature does not support pojo mapping
     */
    private <T> T uuid(final T input) {
        if (Objects.nonNull(input)) {
            final Class<?> clazz = input.getClass();
            try {
                final Field field = clazz.getDeclaredField("key");
                if (Objects.nonNull(field)) {
                    final Object keyValue = Ut.field(input, "key");
                    if (Objects.isNull(keyValue)) {
                        Ut.field(input, "key", UUID.randomUUID().toString());
                    }
                }
            } catch (Throwable ex) {
                /*
                 * To avoid java.lang.NoSuchFieldException: key
                 * Some relation tables do not contain `key` as primaryKey such as
                 * R_USER_ROLE
                 * - userId
                 * - roleId
                 * Instead of other kind of ids here
                 */
            }
        }
        return input;
    }
}
