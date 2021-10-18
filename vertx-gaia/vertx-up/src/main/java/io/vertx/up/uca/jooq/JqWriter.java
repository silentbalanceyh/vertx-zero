package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * Jooq Splitted Writter
 * Create
 * Update
 * Delete
 */
@SuppressWarnings("all")
class JqWriter {

    private transient ActionInsert insert;
    private transient ActionUpdate update;
    private transient ActionUpsert upsert;
    private transient ActionDelete delete;

    private JqWriter(final JqAnalyzer analyzer) {
        /*
         * New Structure for more details
         */
        this.insert = new ActionInsert(analyzer);
        this.update = new ActionUpdate(analyzer);
        this.upsert = new ActionUpsert(analyzer);
        this.delete = new ActionDelete(analyzer);
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

    <T, ID> T update(final ID id, final T updated) {
        return this.update.update(id, updated);
    }

    <T, ID> Future<T> updateAsync(final ID id, final T updated) {
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

    <T> Future<T> deleteAsync(final T entity) {
        return this.delete.deleteAsync(entity);
    }

    <T> Future<List<T>> deleteAsync(final List<T> entity) {
        return this.delete.deleteAsync(entity);
    }

    <T> T delete(final T entity) {
        return this.delete.delete(entity);
    }

    <T> List<T> delete(final List<T> entity) {
        return this.delete.delete(entity);
    }

    <ID> Future<Boolean> deleteByIdAsync(final Collection<ID> ids) {
        return this.delete.deleteByIdAsync(ids);
    }

    <ID> Boolean deleteById(final Collection<ID> ids) {
        return this.delete.deleteById(ids);
    }

    <T, ID> Boolean deleteBy(final JsonObject criteria) {
        return this.delete.deleteBy(criteria);
    }

    <T, ID> Future<Boolean> deleteByAsync(final JsonObject criteria) {
        return this.delete.deleteByAsync(criteria);
    }

    // ============ UPSERT Operation (Save) =============

    public <T> Future<T> upsertAsync(final JsonObject criteria, final T updated) {
        return this.upsert.upsertAsync(criteria, updated);
    }

    public <T, ID> Future<T> upsertAsync(final ID id, final T updated) {
        return this.upsert.upsertAsync(id, updated);
    }

    public <T> T upsert(final JsonObject criteria, final T updated) {
        return this.upsert.upsert(criteria, updated);
    }

    public <T, ID> T upsert(final ID id, final T updated) {
        return this.upsert.upsert(id, updated);
    }

    <T> List<T> upsert(final JsonObject criteria, final List<T> list, final BiPredicate<T, T> finder) {
        return this.upsert.upsert(criteria, list, finder);
    }

    <T> Future<List<T>> upsertAsync(final JsonObject criteria, final List<T> list, final BiPredicate<T, T> finder) {
        return this.upsert.upsertAsync(criteria, list, finder);
    }
}
