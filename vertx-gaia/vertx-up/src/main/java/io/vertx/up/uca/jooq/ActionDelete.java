package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@SuppressWarnings("all")
class ActionDelete extends AbstractAction {

    private transient final ActionFetch fetch;

    ActionDelete(final JqAnalyzer analyzer) {
        super(analyzer);
        this.fetch = new ActionFetch(analyzer);
    }

    /* Future<T> */
    <T> Future<T> deleteAsync(final T entity) {
        return this.<T>successed(this.vertxDAO.deleteAsync(Arrays.asList(entity)), entity);
    }

    /* T */
    <T> T delete(final T entity) {
        this.vertxDAO.delete(entity);
        return entity;
    }

    /* Future<List<T>> */
    <T> Future<List<T>> deleteAsync(final List<T> entity) {
        return this.<List<T>>successed(this.vertxDAO.deleteAsync(entity), entity);
    }

    /* List<T> */
    <T> List<T> delete(final List<T> entity) {
        this.vertxDAO.delete(entity);
        return entity;
    }

    /* By Id */
    <T, ID> Boolean deleteById(final ID... id) {
        this.vertxDAO.deleteById(id);
        return Boolean.TRUE;
    }

    <T, ID> Boolean deleteById(final Collection<ID> ids) {
        this.vertxDAO.deleteById(ids);
        return Boolean.TRUE;
    }

    <T, ID> Future<Boolean> deleteByIdAsync(final ID... id) {
        final CompletableFuture<Void> future = this.vertxDAO.deleteByIdAsync(id);
        return this.successed(future, Boolean.TRUE);
    }

    <T, ID> Future<Boolean> deleteByIdAsync(final Collection<ID> ids) {
        final CompletableFuture<Void> future = this.vertxDAO.deleteByIdAsync(ids);
        return this.successed(future, Boolean.TRUE);
    }

    <T, ID> Future<Boolean> deleteByAsync(final JsonObject criteria) {
        return fetch.<T>fetchAsync(criteria).compose(list -> {
            /*
             * Extract primary key for eatch entities
             */
            final Set<ID> keySet = this.primaryKeys(list);
            return this.deleteByIdAsync(keySet);
        });
    }

    <T, ID> Boolean deleteBy(final JsonObject criteria) {
        final List<T> entities = this.fetch.<T>fetch(criteria);
        final Set<ID> keySet = this.primaryKeys(entities);
        return this.deleteById(keySet);
    }

    private <T, ID> Set<ID> primaryKeys(final List<T> list) {
        final Set<ID> keySet = new TreeSet<>();
        final String fieldName = this.analyzer.primary();
        list.forEach(entity -> {
            final Object value = Ut.field(entity, fieldName);
            if (Objects.nonNull(value)) {
                keySet.add((ID) value);
            }
        });
        return keySet;
    }
}
