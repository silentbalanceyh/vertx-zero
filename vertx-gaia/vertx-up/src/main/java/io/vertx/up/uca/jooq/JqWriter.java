package io.vertx.up.uca.jooq;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.uca.jooq.util.JqTool;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
    private transient ActionUpdate update;
    private transient ActionUpsert upsert;

    private JqWriter(final JqAnalyzer analyzer) {
        this.analyzer = analyzer;
        this.vertxDAO = analyzer.vertxDAO();
        this.reader = JqReader.create(analyzer);

        /*
         * New Structure for more details
         */
        this.insert = new ActionInsert(analyzer);
        this.update = new ActionUpdate(analyzer);
        this.upsert = new ActionUpsert(analyzer);
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
        return this.update.updateAsync(entity);
    }

    /* Sync insert operation: UPDATE */
    <T> T update(final T entity) {
        return this.update.update(entity);
    }

    <T> T update(final Object id, final T updated) {
        return this.update.update(id, updated);
    }

    <T> Future<T> updateAsync(final Object id, final T updated) {
        return this.update.updateAsync(id, updated);
    }

    <T> T update(final JsonObject criteria, final T updated) {
        return this.update.update(criteria, updated);
    }

    <T> Future<T> updateAsync(final JsonObject criteria, final T updated) {
        return this.update.updateAsync(criteria, updated);
    }

    <T> Future<List<T>> updateAsync(final List<T> entities) {
        return this.update.updateAsync(entities);
    }

    <T> List<T> update(final List<T> entities) {
        return this.update.update(entities);
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

    // ============ UPSERT Operation (Save) =============

    public <T> Future<T> upsertAsync(final JsonObject criteria, final T updated) {
        return this.upsert.upsertAsync(criteria, updated);
    }

    public <T> Future<T> upsertAsync(final Object id, final T updated) {
        return this.upsert.upsertAsync(id, updated);
    }

    public <T> T upsert(final JsonObject criteria, final T updated) {
        return this.upsert.upsert(criteria, updated);
    }

    public <T> T upsert(final Object id, final T updated) {
        return this.upsert.upsert(id, updated);
    }

    <T> List<T> upsert(final JsonObject criteria, final List<T> list, final BiPredicate<T, T> finder) {
        return this.upsert.upsert(criteria, list, finder);
    }

    <T> Future<List<T>> upsertAsync(final JsonObject criteria, final List<T> list, final BiPredicate<T, T> finder) {
        return this.upsert.upsertAsync(criteria, list, finder);
    }
}
