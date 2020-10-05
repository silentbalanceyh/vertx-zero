package io.vertx.up.uca.jooq;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class ActionSum extends AbstractAggregator {

    private static final String FIELD_SUM = "SUM";

    ActionSum(final JqAnalyzer analyzer) {
        super(analyzer);
    }

    BigDecimal sum(final String field, final JsonObject criteria) {
        return this.aggregateBy(field, criteria,
                column -> column.sum().as(FIELD_SUM), BigDecimal.ZERO);
    }

    ConcurrentMap<String, BigDecimal> sum(final String field, final JsonObject criteria, final String groupField) {
        return this.aggregateBy(field, criteria,
                column -> column.sum().as(FIELD_SUM), groupField);
    }

    JsonArray sum(final String field, final JsonObject criteria, final String... groupFields) {
        return this.aggregateBy(field, criteria,
                column -> column.sum().as(FIELD_SUM), groupFields);
    }
}
