package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Collection;
import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
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
        return this.dsl.deleteAsync(entity);
    }

    /* T */
    <T> T delete(final T entity) {
        this.dsl.delete(entity);
        return entity;
    }

    /* Future<List<T>> */
    <T> Future<List<T>> deleteAsync(final List<T> entity) {
        return this.dsl.deleteAsync(entity);
    }

    /* List<T> */
    <T> List<T> delete(final List<T> entity) {
        this.dsl.delete(entity);
        return entity;
    }

    /* By Id */

    <T, ID> Boolean deleteById(final Collection<ID> ids) {
        this.dsl.deleteById(ids);
        return Boolean.TRUE;
    }

    <T, ID> Future<Boolean> deleteByIdAsync(final Collection<ID> ids) {
        return this.dsl.deleteByIdAsync(ids);
    }

    <T, ID> Future<Boolean> deleteByAsync(final JsonObject criteria) {
        if (Ut.isNil(criteria)) {
            /*
             * To avoid deleting all records
             */
            return Ux.future(Boolean.TRUE);
        } else {
            return fetch.<T>fetchAsync(criteria).compose(list -> {
                /*
                 * Extract primary key for eatch entities
                 */
                return this.deleteAsync(list).compose(nil -> Future.succeededFuture(Boolean.TRUE));
            });
        }
    }

    <T, ID> Boolean deleteBy(final JsonObject criteria) {
        if (Ut.isNil(criteria)) {
            /*
             * To avoid deleting all records
             */
            return Boolean.TRUE;
        } else {
            final List<T> entities = this.fetch.<T>fetch(criteria);
            this.delete(entities);
            return Boolean.TRUE;
        }
    }
}
