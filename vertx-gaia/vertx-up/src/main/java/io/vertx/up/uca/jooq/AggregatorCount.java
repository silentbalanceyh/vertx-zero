package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
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
        return this.dsl.count();
    }

    Future<Long> countAsync() {
        return this.dsl.countAsync();
    }

    /*
     * Count by criteria
     */
    Long count(final JsonObject criteria) {
        final Condition condition = this.condition(criteria);
        return this.dsl.count(condition);
    }

    <T> Future<Long> countAsync(final JsonObject criteria) {
        final Condition condition = this.condition(criteria);
        return this.dsl.countAsync(condition);
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

}
