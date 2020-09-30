package io.vertx.up.uca.jooq;

import io.vertx.core.Future;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@SuppressWarnings("all")
class ActionDelete extends AbstractAction {

    ActionDelete(final JqAnalyzer analyzer) {
        super(analyzer);
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
}
