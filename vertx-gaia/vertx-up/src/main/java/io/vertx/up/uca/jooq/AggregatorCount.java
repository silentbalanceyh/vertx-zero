package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * SQL Statement:
 * -- SELECT COUNT(*) FROM <TABLE_NAME>
 */
@SuppressWarnings("all")
class AggregatorCount extends AbstractAggregator {

    private static final String FIELD_COUNT = "COUNT";

    AggregatorCount(final JqAnalyzer analyzer) {
        super(analyzer);
    }

    // ------------- Count Operation --------------
    /*
     * All count
     */
    Long count() {
        return this.vertxDAO.count();
    }

    Future<Long> countAsync() {
        return this.successed(this.vertxDAO.countAsync());
    }

    /*
     * Count by criteria
     */
    Long count(final JsonObject criteria) {
        return this.countInternal(this.context(), criteria);
    }

    <T> Future<Long> countAsync(final JsonObject criteria) {
        final Function<DSLContext, Long> executor = context -> countInternal(context, criteria);
        return this.successed(this.vertxDAO.executeAsync(executor));
    }

    /*
     * Single group
     */
    ConcurrentMap<String, Integer> countBy(final JsonObject criteria, final String groupField) {
        final Field countField = DSL.field("*").count().as(FIELD_COUNT);
        return this.aggregateBy(countField, criteria, groupField);
    }

    /*
     * Multi group
     */
    JsonArray countBy(final JsonObject criteria, final String... groupFields) {
        final Field countField = DSL.field("*").count().as(FIELD_COUNT);
        return this.aggregateBy(countField, criteria, groupFields);
    }

    /*
     * Count Grouped
     */

    // ---------------- Private Operation -----------
    /*
     * 「Sync method」
     * Simple count
     */
    private long countInternal(final DSLContext context, final JsonObject criteria) {
        final Condition condition = this.condition(criteria);
        if (Objects.isNull(condition)) {
            return context.fetchCount(this.vertxDAO.getTable());
        } else {
            return context.fetchCount(this.vertxDAO.getTable(), condition);
        }
    }

}
