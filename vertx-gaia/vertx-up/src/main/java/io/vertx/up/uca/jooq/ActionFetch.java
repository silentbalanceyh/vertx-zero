package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.jooq.condition.JooqCond;
import org.jooq.*;

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
        return ((Future<List<T>>) this.dao().findAll()).compose(list -> {
            this.logger().info("[ Jq ] fetchAllAsync() queried rows: {0}", String.valueOf(list.size()));
            return Future.succeededFuture(list);
        });
    }

    /* List<T> */
    <T> List<T> fetchAll() {
        final SelectWhereStep selectStep = this.context().selectFrom(this.dsl.getTable());
        final List<T> list = ((ResultQuery) selectStep).fetchInto(this.dsl.getType());
        this.logger().info("[ Jq ] fetchAll() queried rows: {0}", String.valueOf(list.size()));
        return list;
    }

    /* Future<T> */
    <T, ID> Future<T> fetchByIdAsync(final ID id) {
        return ((Future<T>) this.dao().findOneById(id)).compose(queried -> {
            this.logger().info("[ Jq ] fetchByIdAsync(ID) by id: {1}, queried record: {0}", queried, id);
            return Future.succeededFuture(queried);
        });
    }

    /* T */
    <T, ID> T fetchById(final ID id) {
        final SelectConditionStep selectStep = this.context().selectFrom(this.dsl.getTable())
            .where(this.analyzer.conditionKey(id));
        final T queried = (T) ((ResultQuery) selectStep).fetchOneInto(this.dsl.getType());
        this.logger().info("[ Jq ] fetchByIdAsync(ID) by id: {1}, queried record: {0}", queried, id);
        return queried;
    }

    /* Future<List<T>> */
    <T> Future<List<T>> fetchAsync(final String field, final Object value) {
        final Condition condition = this.analyzer.conditionField(field, value);
        return ((Future<List<T>>) this.dao().findManyByCondition(condition)).compose(list -> {
            this.logger().info("[ Jq ] fetchAsync(String, Object) condition: \"{1}\", queried rows: {0}",
                String.valueOf(list.size()), condition);
            return Future.succeededFuture(list);
        });
    }

    /* List<T> */
    <T> List<T> fetch(final String field, final Object value) {
        final Condition condition = this.analyzer.conditionField(field, value);
        final SelectConditionStep selectStep = this.context().selectFrom(this.dsl.getTable())
            .where(condition);
        final List<T> list = (List<T>) ((ResultQuery) selectStep).fetchInto(this.dsl.getType());
        this.logger().info("[ Jq ] fetch(String, Object) condition: \"{1}\", queried rows: {0}",
            String.valueOf(list.size()), condition);
        return list;
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
    <T> Future<T> fetchOneAsync(final String field, final Object value) {
        final Condition condition = this.analyzer.conditionField(field, value);
        return ((Future<T>) this.dao().findOneByCondition(condition)).compose(queried -> {
            this.logger().info("[ Jq ] fetchOneAsync(String, Object) condition: \"{1}\", queried record: {0}", queried, condition);
            return Future.succeededFuture(queried);
        });
    }

    /* T */
    <T> T fetchOne(final String field, final Object value) {
        final Condition condition = this.analyzer.conditionField(field, value);
        final SelectConditionStep selectStep = this.context().selectFrom(this.dsl.getTable())
            .where(condition);
        final T queried = (T) ((ResultQuery) selectStep).fetchOneInto(this.dsl.getType());
        this.logger().info("[ Jq ] fetchOne(String, Object) condition: \"{1}\", queried record: {0}", queried, condition);
        return queried;
    }

    /* Future<T> */
    <T> Future<T> fetchOneAsync(final JsonObject criteria) {
        final Condition condition = JooqCond.transform(criteria, Operator.AND, this.analyzer::column);
        return ((Future<T>) this.dao().findOneByCondition(condition)).compose(queried -> {
            this.logger().info("[ Jq ] fetchOneAsync(JsonObject) condition: \"{1}\", queried record: {0}", queried, condition);
            return Future.succeededFuture(queried);
        });
    }

    /* T */
    <T> T fetchOne(final JsonObject criteria) {
        final Condition condition = JooqCond.transform(criteria, Operator.AND, this.analyzer::column);
        final SelectConditionStep selectStep = this.context().selectFrom(this.dsl.getTable())
            .where(condition);
        final T queried = (T) ((ResultQuery) selectStep).fetchOneInto(this.dsl.getType());
        this.logger().info("[ Jq ] fetchOne(JsonObject) condition: \"{1}\", queried record: {0}", queried, condition);
        return queried;
    }
}
