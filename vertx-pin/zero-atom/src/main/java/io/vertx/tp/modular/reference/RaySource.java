package io.vertx.tp.modular.reference;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.config.AoRule;
import io.vertx.tp.atom.modeling.reference.RDao;
import io.vertx.tp.atom.modeling.reference.RQuote;
import io.vertx.up.commune.Record;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * ## Data Fetcher
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class RaySource {
    private transient final RQuote quote;

    private RaySource(final RQuote quote) {
        this.quote = quote;
    }

    static RaySource create(final RQuote quote) {
        return new RaySource(quote);
    }

    /*
     * RaySource
     * field1 -> DataQRule
     * field2 -> DataQRule
     */
    public ConcurrentMap<String, JsonArray> fetchSingle(final Record record) {
        return this.fetchData(rule -> rule.condition(record));
    }

    /*
     * 批量运算
     */
    public ConcurrentMap<String, JsonArray> fetchBatch(final Record[] records) {
        return this.fetchData(rule -> rule.condition(records));
    }

    private ConcurrentMap<String, JsonArray> fetchData(final Function<AoRule, JsonObject> supplier) {
        final ConcurrentMap<String, JsonArray> data = new ConcurrentHashMap<>();
        this.quote.rules().forEach((field, rule) -> {
            /*
             * 条件处理
             */
            final JsonObject condition = supplier.apply(rule);
            /*
             * RDao
             */
            final RDao dao = this.quote.dao(field);
            final JsonArray records = dao.fetchBy(condition);
            data.put(field, records);
        });
        return data;
    }
}
