package io.vertx.up.uca.jooq;

import io.horizon.uca.qr.Sorter;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.plugin.jooq.condition.JooqCond;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.jooq.*;

import java.util.*;

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
            this.logging("[ Jq ] fetchAllAsync() queried rows: {0}", String.valueOf(list.size()));
            return Future.succeededFuture(list);
        });
    }

    /* List<T> */
    <T> List<T> fetchAll() {
        final SelectWhereStep selectStep = this.context().selectFrom(this.analyzer.table());
        final List<T> list = ((ResultQuery) selectStep).fetchInto(this.analyzer.type());
        this.logging("[ Jq ] fetchAll() queried rows: {0}", String.valueOf(list.size()));
        return list;
    }

    /* Future<T> */
    <T, ID> Future<T> fetchByIdAsync(final ID id) {
        if (Objects.isNull(id)) {
            return Ux.future();
        }
        return ((Future<T>) this.dao().findOneById(id)).compose(queried -> {
            this.logging("[ Jq ] fetchByIdAsync(ID) by id: {1}, queried record: {0}", queried, id);
            return Future.succeededFuture(queried);
        });
    }

    /* T */
    <T, ID> T fetchById(final ID id) {
        if (Objects.isNull(id)) {
            return null;
        }
        final SelectConditionStep selectStep = this.context().selectFrom(this.analyzer.table())
            .where(this.analyzer.conditionId(id));
        final T queried = (T) ((ResultQuery) selectStep).fetchOneInto(this.analyzer.type());
        this.logging("[ Jq ] fetchByIdAsync(ID) by id: {1}, queried record: {0}", queried, id);
        return queried;
    }

    /* Future<List<T>> */
    <T> Future<List<T>> fetchAsync(final String field, final Object value) {
        if (Objects.isNull(value) || Ut.isNil(field)) {
            return Ux.futureL();
        }
        // Fix issue of in ()
        if (value instanceof final Collection values && values.isEmpty()) {
            return Ux.futureL();
        }
        final Condition condition = this.analyzer.conditionField(field, value);
        return ((Future<List<T>>) this.dao().findManyByCondition(condition)).compose(list -> {
            this.logging("[ Jq ] fetchAsync(String, Object) condition: \"{1}\", queried rows: {0}",
                String.valueOf(list.size()), condition);
            return Future.succeededFuture(list);
        });
    }

    /* List<T> */
    <T> List<T> fetch(final String field, final Object value) {
        if (Objects.isNull(value) || Ut.isNil(field)) {
            return new ArrayList<>();
        }
        // Fix issue of in ()
        if (value instanceof final Collection values && values.isEmpty()) {
            return new ArrayList<>();
        }
        final Condition condition = this.analyzer.conditionField(field, value);
        final SelectConditionStep selectStep = this.context().selectFrom(this.analyzer.table())
            .where(condition);
        final List<T> list = (List<T>) ((ResultQuery) selectStep).fetchInto(this.analyzer.type());
        this.logging("[ Jq ] fetch(String, Object) condition: \"{1}\", queried rows: {0}",
            String.valueOf(list.size()), condition);
        // Fix issue: java.lang.NullPointerException: Cannot invoke "java.util.List.isEmpty()" because "qKeys" is null
        return Optional.ofNullable(list).orElse(new ArrayList<>());
    }

    /* Future<List<T>> */
    <T> Future<List<T>> fetchAsync(final JsonObject criteria) {
        return this.qr.<T>searchAsync(criteria, null).compose(list -> {
            this.logging("[ Jq ] fetchAsync(JsonObject) condition json: \"{1}\", queried rows: {0}",
                String.valueOf(list.size()), criteria);
            return Future.succeededFuture(list);
        });
    }

    <T> Future<List<T>> fetchAsync(final JsonObject criteria, final Sorter sorter) {
        return this.qr.<T>searchAsync(criteria, sorter).compose(list -> {
            this.logging("[ Jq ] fetchAsync(JsonObject, Sorter) condition json: \"{1}\" and sorter: \"{2}\", queried rows: {0}",
                String.valueOf(list.size()), criteria, sorter);
            return Future.succeededFuture(list);
        });
    }

    /* List<T> */
    <T> List<T> fetch(final JsonObject criteria) {
        final List<T> list = this.qr.search(criteria, null);
        this.logging("[ Jq ] fetch(JsonObject) condition json: \"{1}\", queried rows: {0}",
            String.valueOf(list.size()), criteria);
        return list;
    }

    <T> List<T> fetch(final JsonObject criteria, final Sorter sorter) {
        final List<T> list = this.qr.search(criteria, sorter);
        this.logging("[ Jq ] fetch(JsonObject, Sorter) condition json: \"{1}\"  and sorter: \"{2}\", queried rows: {0}",
            String.valueOf(list.size()), criteria, sorter);
        return list;
    }


    /* Future<T> */
    <T> Future<T> fetchOneAsync(final String field, final Object value) {
        if (Objects.isNull(value) || Ut.isNil(field)) {
            return Ux.future();
        }
        final Condition condition = this.analyzer.conditionField(field, value);
        return ((Future<T>) this.dao().findOneByCondition(condition)).compose(queried -> {
            this.logging("[ Jq ] fetchOneAsync(String, Object) condition: \"{1}\", queried record: {0}", queried, condition);
            return Future.succeededFuture(queried);
        });
    }

    /* T */
    <T> T fetchOne(final String field, final Object value) {
        if (Objects.isNull(value) || Ut.isNil(field)) {
            return null;
        }
        final Condition condition = this.analyzer.conditionField(field, value);
        final SelectConditionStep selectStep = this.context().selectFrom(this.analyzer.table())
            .where(condition);
        final T queried = (T) ((ResultQuery) selectStep).fetchOneInto(this.analyzer.type());
        this.logging("[ Jq ] fetchOne(String, Object) condition: \"{1}\", queried record: {0}", queried, condition);
        return queried;
    }

    /* Future<T> */
    <T> Future<T> fetchOneAsync(final JsonObject criteria) {
        final Condition condition = JooqCond.transform(criteria, Operator.AND, this.analyzer::column);
        return ((Future<T>) this.dao().findOneByCondition(condition)).compose(queried -> {
            this.logging("[ Jq ] fetchOneAsync(JsonObject) condition: \"{1}\", queried record: {0}", queried, condition);
            return Future.succeededFuture(queried);
        });
    }

    /* T */
    <T> T fetchOne(final JsonObject criteria) {
        final Condition condition = JooqCond.transform(criteria, Operator.AND, this.analyzer::column);
        final SelectConditionStep selectStep = this.context().selectFrom(this.analyzer.table())
            .where(condition);
        final T queried = (T) ((ResultQuery) selectStep).fetchOneInto(this.analyzer.type());
        this.logging("[ Jq ] fetchOne(JsonObject) condition: \"{1}\", queried record: {0}", queried, condition);
        return queried;
    }
}
