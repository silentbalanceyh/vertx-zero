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
class AggregatorAvg extends AbstractAggregator {

    private static final String FIELD_AVG = "AVG";

    AggregatorAvg(final JqAnalyzer analyzer) {
        super(analyzer);
    }

    BigDecimal avg(final String field, final JsonObject criteria) {
        return this.aggregateBy(field, criteria,
            column -> DSL.avg(column).as(FIELD_AVG), BigDecimal.ZERO);
    }

    ConcurrentMap<String, BigDecimal> avg(final String field, final JsonObject criteria, final String groupField) {
        return this.aggregateBy(field, criteria,
            column -> DSL.avg(column).as(FIELD_AVG), groupField);
    }

    JsonArray avg(final String field, final JsonObject criteria, final String... groupFields) {
        return this.aggregateBy(field, criteria,
            column -> DSL.avg(column).as(FIELD_AVG), groupFields);
    }
}
