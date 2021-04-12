package io.vertx.tp.modular.ray;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.reference.RDao;
import io.vertx.tp.atom.modeling.reference.RQuote;
import io.vertx.tp.atom.modeling.reference.RRule;
import io.vertx.tp.modular.dao.AoDao;
import io.vertx.up.commune.Record;
import io.vertx.up.util.Ut;

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

    private ConcurrentMap<String, JsonArray> fetchData(final Function<RRule, JsonObject> supplier) {
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
            if (dao.isStatic()) {
                /*
                 * Static
                 */
                final Function<JsonObject, JsonArray> executor = dao.executor();
                final JsonArray records = executor.apply(condition);
                data.put(field, records);
            } else {
                /*
                 * Dynamic
                 */
                final AoDao daoD = dao.daoD();
                final Record[] records = daoD.fetch(condition);
                data.put(field, Ut.toJArray(records));
            }
        });
        return data;
    }
}
