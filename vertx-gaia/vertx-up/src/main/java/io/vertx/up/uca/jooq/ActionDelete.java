package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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

    <T, ID> Boolean deleteById(final Collection<ID> ids) {
        this.vertxDAO.deleteById(ids);
        return Boolean.TRUE;
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
            return this.deleteAsync(list).compose(nil -> Future.succeededFuture(Boolean.TRUE));
        });
    }

    <T, ID> Boolean deleteBy(final JsonObject criteria) {
        final List<T> entities = this.fetch.<T>fetch(criteria);
        this.delete(entities);
        return Boolean.TRUE;
    }
}
