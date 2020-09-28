package io.vertx.up.uca.jooq.aggr;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.Inquiry;
import io.vertx.up.uca.jooq.JqAnalyzer;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@SuppressWarnings("all")
public class JqAggregator {

    private transient final AgCount counter;

    private JqAggregator(final JqAnalyzer analyzer) {
        this.counter = AgCount.create(analyzer);
    }

    public static JqAggregator create(final JqAnalyzer analyzer) {
        return new JqAggregator(analyzer);
    }

    // -------------------- Count Operation ------------
    /*
     * Internal Call and do not export this Programming API
     */
    public Long count(final Inquiry inquiry) {
        return this.count(null == inquiry.getCriteria() ? new JsonObject() : inquiry.getCriteria().toJson());
    }

    public <T> Future<Long> countAsync(final Inquiry inquiry) {
        return this.countAsync(null == inquiry.getCriteria() ? new JsonObject() : inquiry.getCriteria().toJson());
    }

    public Long count(final JsonObject params, final String pojo) {
        return null;
    }

    /*
     * AgCount class for count
     * 1) countAll / countAllAsync
     */
    public Long countAll() {
        return this.counter.count();
    }

    public Future<Long> countAllAsync() {
        return this.counter.countAsync();
    }

    public <T> Long count(final JsonObject filters) {
        return this.counter.count(filters);
    }

    /*
     * Basic Method for low tier search/count pair
     */

    public <T> Future<Long> countAsync(final JsonObject filters) {
        return this.counter.countAsync(filters);
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
    public <T> Future<ConcurrentMap<String, Integer>> countByAsync(final JsonObject filters, final String field) {
        return Future.succeededFuture(countBy(filters, field));
    }

    public <T> ConcurrentMap<String, Integer> countBy(final JsonObject filters, final String field) {
        return null;
    }

    // -------------------- Group Operation ------------
    /*
     * Group Fields here
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
    public Future<JsonArray> groupByAsync(final JsonObject filters, final String... fields) {
        return null;
    }

    public <T> ConcurrentMap<String, List<T>> group(final JsonObject filters, final String field) {
        return null;
    }
}
