package io.vertx.tp.modular.reference;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.config.AoRule;
import io.vertx.tp.atom.modeling.reference.RDao;
import io.vertx.tp.atom.modeling.reference.RQuote;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.atom.Kv;
import io.vertx.up.commune.Record;

import java.util.Objects;
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
        /*
         * 换一种算法
         */
        final ConcurrentMap<String, Integer> fieldCodes = new ConcurrentHashMap<>();
        final ConcurrentMap<Integer, Kv<JsonObject, RDao>> execMap = new ConcurrentHashMap<>();
        this.quote.rules().forEach((field, rule) -> {
            /* 条件处理 */
            final JsonObject condition = supplier.apply(rule);
            if (Objects.nonNull(condition)) {
                final int hashCode = condition.hashCode();
                fieldCodes.put(field, hashCode);
                /* 横向压缩 */
                final RDao dao = this.quote.dao(field);
                execMap.put(hashCode, Kv.create(condition, dao));
            }
        });
        execMap.forEach((hashCode, kv) -> {
            final JsonObject condition = kv.getKey();
            final RDao dao = kv.getValue();
            final JsonArray queried = dao.fetchBy(condition);
            /* 反向运算 */
            Ao.infoUca(this.getClass(), "Batch condition building: {0}, size = {1}",
                    condition.encode(), String.valueOf(queried.size()));
            fieldCodes.forEach((field, codeKey) -> {
                if (Objects.equals(hashCode, codeKey)) {
                    data.put(field, queried);
                }
            });
        });
        return data;
    }
}
