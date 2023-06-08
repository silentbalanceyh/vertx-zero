package io.vertx.up.uca.jooq;

import io.horizon.uca.qr.syntax.Ir;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
class JqAggregator {

    private transient final AggregatorCount counter;
    private transient final AggregatorSum sum;
    private transient final AggregatorMax max;
    private transient final AggregatorMin min;
    private transient final AggregatorAvg avg;

    private transient final ActionGroup group;

    private JqAggregator(final JqAnalyzer analyzer) {
        this.group = new ActionGroup(analyzer);
        /*
         * Aggr
         */
        this.counter = new AggregatorCount(analyzer);
        this.sum = new AggregatorSum(analyzer);
        this.min = new AggregatorMin(analyzer);
        this.max = new AggregatorMax(analyzer);
        this.avg = new AggregatorAvg(analyzer);
    }

    public static JqAggregator create(final JqAnalyzer analyzer) {
        return new JqAggregator(analyzer);
    }

    // -------------------- Count Operation ------------
    /*
     * Internal Call and do not export this Programming API
     */
    Long count(final Ir qr) {
        return this.count(null == qr.getCriteria() ? new JsonObject() : qr.getCriteria().toJson());
    }

    <T> Future<Long> countAsync(final Ir qr) {
        return this.countAsync(null == qr.getCriteria() ? new JsonObject() : qr.getCriteria().toJson());
    }

    /*
     * AgCount class for count
     * 1) countAll / countAllAsync
     */
    Long countAll() {
        return this.counter.count();
    }

    Future<Long> countAllAsync() {
        return this.counter.countAsync();
    }

    <T> Long count(final JsonObject criteria) {
        return this.counter.count(criteria);
    }

    <T> Future<Long> countAsync(final JsonObject criteria) {
        return this.counter.countAsync(criteria);
    }

    /*
     * Count Function by group field here
     * The aggregation result is List<Map<String,Object>> reference here,
     * Here the result should be:
     *
     * Map Data Structure:
     *  - Group1 = Group1's Counter
     *  - Group2 = Group2's Counter
     *  - ......
     *  - GroupN = GroupN's Counter
     *
     * The limitation is that the grouped field should be only one
     */

    <T> ConcurrentMap<String, Integer> countBy(final JsonObject criteria, final String field) {
        return this.counter.countBy(criteria, field);
    }

    /*
     * Count function by group Fields here
     * The aggregation result is List<Map<String,Object>> reference here,
     * Here the result should be:
     *
     * List<JsonArray> Data Structure, here each element shouldd be:
     * {
     *     "field1": "value1",
     *     "field2": "value2",
     *     ......
     *     "fieldN": "valueN",
     *     "count": "COUNT"
     * }
     */
    <T> JsonArray countBy(final JsonObject criteria, final String... fields) {
        return this.counter.countBy(criteria, fields);
    }

    // -------------------- Group Operation ------------
    <T> ConcurrentMap<String, List<T>> group(final String field) {
        return this.group.group(field);
    }

    <T> ConcurrentMap<String, List<T>> group(final JsonObject criteria, final String field) {
        return this.group.group(criteria, field);
    }

    // -------------------- Sum Operation ------------
    BigDecimal sum(final String field, final JsonObject criteria) {
        return this.sum.sum(field, criteria);
    }

    ConcurrentMap<String, BigDecimal> sum(final String field, final JsonObject criteria, final String groupField) {
        return this.sum.sum(field, criteria, groupField);
    }

    JsonArray sum(final String field, final JsonObject criteria, final String... groupFields) {
        return this.sum.sum(field, criteria, groupFields);
    }

    // ---------------------- Max Operation -------------
    BigDecimal max(final String field, final JsonObject criteria) {
        return this.max.max(field, criteria);
    }

    ConcurrentMap<String, BigDecimal> max(final String field, final JsonObject criteria, final String groupField) {
        return this.max.max(field, criteria, groupField);
    }

    JsonArray max(final String field, final JsonObject criteria, final String... groupFields) {
        return this.max.max(field, criteria, groupFields);
    }

    // ---------------------- Min Operation -------------
    BigDecimal min(final String field, final JsonObject criteria) {
        return this.min.min(field, criteria);
    }

    ConcurrentMap<String, BigDecimal> min(final String field, final JsonObject criteria, final String groupField) {
        return this.min.min(field, criteria, groupField);
    }

    JsonArray min(final String field, final JsonObject criteria, final String... groupFields) {
        return this.min.min(field, criteria, groupFields);
    }

    // ---------------------- Avg Operation -------------
    BigDecimal avg(final String field, final JsonObject criteria) {
        return this.avg.avg(field, criteria);
    }

    ConcurrentMap<String, BigDecimal> avg(final String field, final JsonObject criteria, final String groupField) {
        return this.avg.avg(field, criteria, groupField);
    }

    JsonArray avg(final String field, final JsonObject criteria, final String... groupFields) {
        return this.avg.avg(field, criteria, groupFields);
    }
}
