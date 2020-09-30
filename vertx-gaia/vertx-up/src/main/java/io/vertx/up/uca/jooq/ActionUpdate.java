package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@SuppressWarnings("all")
class ActionUpdate extends AbstractAction {

    private transient ActionFetch fetch;

    ActionUpdate(final JqAnalyzer analyzer) {
        super(analyzer);
        // Qr
        this.fetch = new ActionFetch(analyzer);
    }

    // ============ UPSERT Operation =============

    /* Future<T> */
    <T> Future<T> updateAsync(final T entity) {
        return this.successed(this.vertxDAO.updateAsync(entity), entity);
    }

    /* T */
    <T> T update(final T entity) {
        this.vertxDAO.update(entity);
        return entity;
    }

    /* Future<List<T>> */
    <T> Future<List<T>> updateAsync(final List<T> list) {
        return this.successed(this.vertxDAO.updateAsync(list), list);
    }

    /* T */
    <T> List<T> update(final List<T> list) {
        this.vertxDAO.update(list);
        return list;
    }

    /* Future<T> */
    <T> Future<T> updateAsync(final Object id, final T updated) {
        return this.fetch.fetchByIdAsync(id).compose(previous -> {
            final T combine = this.analyzer.copyEntity((T) previous, updated);
            return this.updateAsync(combine);
        });
    }

    /* T */
    <T> T update(final Object id, final T updated) {
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

    // ============ MERGE Operation (Save) =============
    /*
     * Combine data for `Saving`, it could help Jooq to execute SAVE
     *
     * Insert / Update merged
     * 1 Existing: do updating
     * 2 Missing: do missing
     */
}
