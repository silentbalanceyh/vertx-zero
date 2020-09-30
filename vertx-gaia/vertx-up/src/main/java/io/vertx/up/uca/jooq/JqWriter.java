package io.vertx.up.uca.jooq;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.util.JqTool;
import io.vertx.up.unity.Ux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiPredicate;

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

    private transient ActionInsert insert;

    private JqWriter(final JqAnalyzer analyzer) {
        this.analyzer = analyzer;
        this.vertxDAO = analyzer.vertxDAO();
        this.reader = JqReader.create(analyzer);

        /*
         * New Structure for more details
         */
        this.insert = new ActionInsert(analyzer);
    }

    static JqWriter create(final JqAnalyzer analyzer) {
        return new JqWriter(analyzer);
    }

    // ============ INSERT Operation =============


    /* Async insert operation: INSERT */
    <T> Future<T> insertAsync(final T entity) {
        return this.insert.insertAsync(entity);
    }

    <T> Future<List<T>> insertAsync(final List<T> entities) {
        return this.insert.insertAsync(entities);
    }

    <T> T insert(final T entity) {
        return this.insert.insert(entity);
    }

    <T> List<T> insert(final List<T> entities) {
        return this.insert.insert(entities);
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

    <T> Future<List<T>> deleteAsync(final List<T> entity) {
        final CompletableFuture<Void> future = this.vertxDAO.deleteAsync(entity);
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
        /*final Condition condition = JooqCond.transform(filters, null, this.analyzer::column);
        final CompletableFuture<Integer> deleted = this.vertxDAO.deleteExecAsync(condition);
        return JqTool.future(deleted).compose(nil -> Future.succeededFuture(Boolean.TRUE));*/
        return null;
    }

    /* Sync delete operation: DELETE */
    <T> T delete(final T entity) {
        this.vertxDAO.delete(entity);
        return entity;
    }

    <T> List<T> delete(final List<T> entity) {
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
        final List<T> result = this.reader.search(filters);
        result.stream().map(item -> this.delete(item));
        return Boolean.TRUE;
    }

    // ============ UPDATE Operation (Save) =============

    <T> T update(final Object id, final T updated) {
        final T old = this.reader.<T>fetchById(id);
        final T combine = this.analyzer.copyEntity(old, updated);
        return this.update(combine);
    }

    <T> Future<T> updateAsync(final Object id, final T updated) {
        return this.reader.<T>fetchByIdAsync(id).compose(old -> {
            final T combine = this.analyzer.copyEntity(old, updated);
            return this.<T>updateAsync(combine);
        });
    }
    // ============ UPSERT Operation (Save) =============

    public <T> Future<T> upsertAsync(final JsonObject filters, final T updated) {
        return combineAsync(this.reader.<T>fetchOneAsync(filters), updated);
    }

    public <T> Future<T> upsertAsync(final String key, final T updated) {
        return combineAsync(this.reader.<T>fetchByIdAsync(key), updated);
    }

    public <T> T upsert(final JsonObject filters, final T updated) {
        return this.combine(this.reader.fetchOne(filters), updated);
    }

    public <T> T upsert(final String key, final T updated) {
        return this.combine(this.reader.fetchById(key), updated);
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
}
