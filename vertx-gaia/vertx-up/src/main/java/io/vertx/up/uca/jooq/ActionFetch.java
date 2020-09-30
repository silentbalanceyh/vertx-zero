package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.jooq.condition.JooqCond;
import org.jooq.Condition;
import org.jooq.Operator;

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

    /* Future<T> */
    <T> Future<T> fetchByIdAsync(final Object id) {
        return this.successed(this.vertxDAO.findByIdAsync(id));
    }

    /* T */
    <T> T fetchById(final Object id) {
        return (T) this.vertxDAO.findById(id);
    }

    /* Future<T> */
    <T> Future<T> fetchOneAsync(final String field, final Object value) {
        return this.successed(this.vertxDAO.fetchOneAsync(this.analyzer.column(field), value));
    }

    /* T */
    <T> T fetchOne(final String field, final Object value) {
        return (T) this.vertxDAO.fetchOne(this.analyzer.column(field), value);
    }

    /* Future<T> */
    <T> Future<T> fetchOneAsync(final JsonObject criteria) {
        final Condition condition = JooqCond.transform(criteria, Operator.AND, this.analyzer::column);
        return this.successed(this.vertxDAO.fetchOneAsync(condition));
    }

    /* T */
    <T> T fetchOne(final JsonObject criteria) {
        final Condition condition = JooqCond.transform(criteria, Operator.AND, this.analyzer::column);
        return (T) this.context().selectFrom(this.vertxDAO.getTable()).where(condition).fetchOne(this.vertxDAO.mapper());
    }
}
