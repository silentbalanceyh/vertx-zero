package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@SuppressWarnings("all")
class ActionFetch extends AbstractAction {

    private transient final ActionQr qr;

    ActionFetch(final JqAnalyzer analyzer) {
        super(analyzer);
        // Qr
        this.qr = new ActionQr(analyzer);
    }

    /* Future<List<T>> */
    <T> Future<List<T>> fetchAllAsync() {
        return this.<List<T>>successed(this.vertxDAO.findAllAsync());
    }

    /* List<T> */
    <T> List<T> fetchAll() {
        return this.vertxDAO.findAll();
    }

    /* Future<List<T>> */
    <T> Future<List<T>> fetchAsync(final String field, final Object value) {
        return this.<List<T>>successed(this.vertxDAO.fetchAsync(this.analyzer.column(field), parameters(value)));
    }

    /* List<T> */
    <T> List<T> fetch(final String field, final Object value) {
        return this.vertxDAO.fetch(this.analyzer.column(field), parameters(value));
    }

    /* Future<List<T>> */
    <T> Future<List<T>> fetchAsync(final JsonObject criteria) {
        return this.qr.searchAsync(criteria);
    }

    /* List<T> */
    <T> List<T> fetch(final JsonObject criteria) {
        return this.qr.search(criteria);
    }
}
