package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
class ActionUpdate extends AbstractAction {

    private transient ActionFetch fetch;

    ActionUpdate(final JqAnalyzer analyzer) {
        super(analyzer);
        // Qr
        this.fetch = new ActionFetch(analyzer);
    }

    // ============ UPDATE Operation =============

    /* Future<T> */
    <T> Future<T> updateAsync(final T entity) {
        return this.dsl.updateAsync(entity);
    }

    /* T */
    <T> T update(final T entity) {
        this.dsl.update(entity);
        return entity;
    }

    /* Future<List<T>> */
    <T> Future<List<T>> updateAsync(final List<T> list) {
        return this.dsl.updateAsync(list);
    }

    /* T */
    <T> List<T> update(final List<T> list) {
        this.dsl.update(list);
        return list;
    }

    /* Future<T> */
    <T, ID> Future<T> updateAsync(final ID id, final T updated) {
        return this.fetch.fetchByIdAsync(id).compose(previous -> {
            final T combine = this.analyzer.copyEntity((T) previous, updated);
            return this.updateAsync(combine);
        });
    }

    /* T */
    <T, ID> T update(final ID id, final T updated) {
        final T previous = this.fetch.fetchById(id);
        final T combine = this.analyzer.copyEntity((T) previous, updated);
        return this.update(combine);
    }

    /* Future<T> */
    <T> Future<T> updateAsync(final JsonObject criteria, final T updated) {
        return this.fetch.fetchOneAsync(criteria).compose(previous -> {
            final T combine = this.analyzer.copyEntity((T) previous, updated);
            return this.updateAsync(combine);
        });
    }

    /* T */
    <T> T update(final JsonObject criteria, final T updated) {
        final T previous = this.fetch.fetchOne(criteria);
        final T combine = this.analyzer.copyEntity((T) previous, updated);
        return this.update(combine);
    }
}
