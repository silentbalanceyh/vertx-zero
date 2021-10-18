package io.vertx.up.uca.jooq;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("unchecked")
class AggregatorMax extends AbstractAggregator {
    private static final String FIELD_MAX = "MAX";

    AggregatorMax(final JqAnalyzer analyzer) {
        super(analyzer);
    }

    BigDecimal max(final String field, final JsonObject criteria) {
        return this.aggregateBy(field, criteria,
            column -> DSL.max(column).as(FIELD_MAX), BigDecimal.ZERO);
    }

    ConcurrentMap<String, BigDecimal> max(final String field, final JsonObject criteria, final String groupField) {
        return this.aggregateBy(field, criteria,
            column -> DSL.max(column).as(FIELD_MAX), groupField);
    }

    JsonArray max(final String field, final JsonObject criteria, final String... groupFields) {
        return this.aggregateBy(field, criteria,
            column -> DSL.max(column).as(FIELD_MAX), groupFields);
    }
}
