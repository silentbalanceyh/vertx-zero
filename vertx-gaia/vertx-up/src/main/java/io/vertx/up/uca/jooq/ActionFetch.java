package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.jooq.condition.JooqCond;
import org.jooq.Condition;
import org.jooq.Operator;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
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
        return this.dsl.fetchAllAsync();
    }

    /* List<T> */
    <T> List<T> fetchAll() {
        return this.dsl.fetchAll();
    }

    /* Future<List<T>> */
    <T> Future<List<T>> fetchAsync(final String field, final Object value) {
        return this.dsl.fetchAsync(this.condition(field, value));
    }

    /* List<T> */
    <T> List<T> fetch(final String field, final Object value) {
        return this.dsl.fetch(this.condition(field, value));
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
    <T, ID> Future<T> fetchByIdAsync(final ID id) {
        return this.dsl.fetchByIdAsync(id);
    }

    /* T */
    <T, ID> T fetchById(final ID id) {
        return (T) this.dsl.fetchById(id);
    }

    /* Future<T> */
    <T> Future<T> fetchOneAsync(final String field, final Object value) {
        return this.dsl.fetchOneAsync(this.condition(field, value));
    }

    /* T */
    <T> T fetchOne(final String field, final Object value) {
        return this.dsl.fetchOne(this.condition(field, value));
    }

    /* Future<T> */
    <T> Future<T> fetchOneAsync(final JsonObject criteria) {
        final Condition condition = JooqCond.transform(criteria, Operator.AND, this.analyzer::column);
        return this.dsl.fetchOneAsync(condition);
    }

    /* T */
    <T> T fetchOne(final JsonObject criteria) {
        final Condition condition = JooqCond.transform(criteria, Operator.AND, this.analyzer::column);
        return this.dsl.fetchOne(condition);
    }
}
