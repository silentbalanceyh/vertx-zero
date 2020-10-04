package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.Inquiry;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@SuppressWarnings("all")
class JqAggregator {

    private transient final ActionCount counter;
    private transient final ActionGroup group;

    private JqAggregator(final JqAnalyzer analyzer) {
        this.counter = new ActionCount(analyzer);
        this.group = new ActionGroup(analyzer);
    }

    public static JqAggregator create(final JqAnalyzer analyzer) {
        return new JqAggregator(analyzer);
    }

    // -------------------- Count Operation ------------
    /*
     * Internal Call and do not export this Programming API
     */
    Long count(final Inquiry inquiry) {
        return this.count(null == inquiry.getCriteria() ? new JsonObject() : inquiry.getCriteria().toJson());
    }

    <T> Future<Long> countAsync(final Inquiry inquiry) {
        return this.countAsync(null == inquiry.getCriteria() ? new JsonObject() : inquiry.getCriteria().toJson());
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
    <T> Future<ConcurrentMap<String, Integer>> countByAsync(final JsonObject criteria, final String field) {
        return Future.succeededFuture(countBy(criteria, field));
    }

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

    Future<JsonArray> countByAsync(final JsonObject criteria, final String... fields) {
        return Future.succeededFuture(countBy(criteria, fields));
    }

    // -------------------- Group Operation ------------
    <T> ConcurrentMap<String, List<T>> group(final String field) {
        return this.group.group(field);
    }

    <T> Future<ConcurrentMap<String, List<T>>> groupAsync(final String field) {
        return Future.succeededFuture(this.group(field));
    }

    <T> ConcurrentMap<String, List<T>> group(final JsonObject criteria, final String field) {
        return this.group.group(criteria, field);
    }

    <T> Future<ConcurrentMap<String, List<T>>> groupAsync(final JsonObject criteria, final String field) {
        return Future.succeededFuture(this.group(criteria, field));
    }
}
