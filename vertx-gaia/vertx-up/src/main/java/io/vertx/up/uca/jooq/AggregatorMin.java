package io.vertx.up.uca.jooq;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
class AggregatorMin extends AbstractAggregator {
    private static final String FIELD_MIN = "MIN";

    AggregatorMin(final JqAnalyzer analyzer) {
        super(analyzer);
    }

    BigDecimal min(final String field, final JsonObject criteria) {
        return this.aggregateBy(field, criteria,
            column -> DSL.min(column).as(FIELD_MIN), BigDecimal.ZERO);
    }

    ConcurrentMap<String, BigDecimal> min(final String field, final JsonObject criteria, final String groupField) {
        return this.aggregateBy(field, criteria,
            column -> DSL.min(column).as(FIELD_MIN), groupField);
    }

    JsonArray min(final String field, final JsonObject criteria, final String... groupFields) {
        return this.aggregateBy(field, criteria,
            column -> DSL.min(column).as(FIELD_MIN), groupFields);
    }
}
