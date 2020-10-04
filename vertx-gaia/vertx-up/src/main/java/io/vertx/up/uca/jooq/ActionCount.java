package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * SQL Statement:
 * -- SELECT COUNT(*) FROM <TABLE_NAME>
 */
@SuppressWarnings("all")
class ActionCount extends AbstractAggregator {

    private static final String FIELD_COUNT = "COUNT";

    ActionCount(final JqAnalyzer analyzer) {
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

    ConcurrentMap<String, Integer> countBy(final JsonObject criteria, final String field) {
        /*
         * COUNT field
         */
        final Field countField = DSL.field("*").count().as(FIELD_COUNT);
        /*
         * COUNT by field
         */
        final List<Map<String, Object>> groupResult = this.fetchAggregation(criteria, new Field[]{countField}, field);
        /*
         * Process result
         */
        return toMap(FIELD_COUNT, groupResult, field);
    }

    JsonArray countBy(final JsonObject criteria, final String... fields) {
        /*
         * COUNT fields
         */
        final Field countField = DSL.field("*").count().as(FIELD_COUNT);
        /*
         * COUNT by fields
         */
        final List<Map<String, Object>> groupResult = this.fetchAggregation(criteria, new Field[]{countField}, fields);
        /*
         * Process result
         */
        return toArray(FIELD_COUNT, groupResult, fields);
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
