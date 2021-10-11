package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jooq.Condition;
import org.jooq.Field;

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
        Long rows = Long.valueOf(this.context().fetchCount(this.analyzer.table()));
        this.logging("[ Jq ] count() rows: {0}", String.valueOf(rows));
        return rows;
    }

    Future<Long> countAsync() {
        return this.dsl.executeBlocking(h -> h.complete(this.count()));
    }

    /*
     * Count by criteria
     */
    Long count(final JsonObject criteria) {
        final Condition condition = this.analyzer.condition(criteria);
        Long rows = Long.valueOf(this.context().fetchCount(this.analyzer.table(), condition));
        this.logging("[ Jq ] count(JsonObject) rows: {0}", String.valueOf(rows));
        return rows;
    }

    <T> Future<Long> countAsync(final JsonObject criteria) {
        return this.dsl.executeBlocking(h -> h.complete(this.count(criteria)));
    }

    /*
     * Single group
     */
    ConcurrentMap<String, Integer> countBy(final JsonObject criteria, final String groupField) {
        final String primary = this.analyzer.primary();
        final Field countField = this.analyzer.column(primary).count().as(FIELD_COUNT);
        return this.aggregateBy(countField, criteria, groupField);
    }

    /*
     * Multi group
     */
    JsonArray countBy(final JsonObject criteria, final String... groupFields) {
        final String primary = this.analyzer.primary();
        final Field countField = this.analyzer.column(primary).count().as(FIELD_COUNT);
        return this.aggregateBy(countField, criteria, groupFields);
    }

}
