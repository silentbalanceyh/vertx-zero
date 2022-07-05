package io.vertx.tp.modular.reference;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.atom.Kv;
import io.vertx.up.commune.Record;
import io.vertx.up.eon.Constants;
import io.vertx.up.experiment.mixture.HRule;
import io.vertx.up.experiment.reference.RDao;
import io.vertx.up.experiment.reference.RQuote;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.cache.Rapid;
import io.vertx.up.uca.cache.RapidKey;
import io.vertx.up.unity.Ux;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
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
    public ConcurrentMap<String, JsonArray> single(final Record record) {
        return this.data(rule -> rule.condition(record));
    }

    public Future<ConcurrentMap<String, JsonArray>> singleAsync(final Record record) {
        return this.dataAsync(rule -> rule.condition(record));
    }

    /*
     * 批量运算
     */
    public ConcurrentMap<String, JsonArray> batch(final Record[] records) {
        return this.data(rule -> rule.condition(records));
    }

    public Future<ConcurrentMap<String, JsonArray>> batchAsync(final Record[] records) {
        return this.dataAsync(rule -> rule.condition(records));
    }


    private Future<ConcurrentMap<String, JsonArray>> dataAsync(final Function<HRule, JsonObject> supplier) {
        return this.ready(supplier, (fieldCodes, execMap) -> {
            final ConcurrentMap<Integer, Future<JsonArray>> futureMap = new ConcurrentHashMap<>();
            execMap.forEach((hashCode, kv) -> {
                final JsonObject condition = kv.getKey();
                final RDao dao = kv.getValue();
                futureMap.put(hashCode,
                    Rapid.<String, JsonArray>t(RapidKey.REFERENCE, Constants.DEFAULT_EXPIRED_DATA)
                        .cached(String.valueOf(hashCode), () -> {
                            Ao.infoUca(this.getClass(), "Async Batch condition building: {0}", condition.encode());
                            return dao.fetchByAsync(condition);
                        }));
            });
            return Fn.arrangeM(futureMap).compose(queriedMap -> {
                final ConcurrentMap<String, JsonArray> data = new ConcurrentHashMap<>();
                queriedMap.forEach((hashCode, dataArray) -> {
                    fieldCodes.forEach((field, codeKey) -> {
                        if (Objects.equals(hashCode, codeKey)) {
                            data.put(field, dataArray);
                        }
                    });
                });
                return Ux.future(data);
            });
        });
    }

    private ConcurrentMap<String, JsonArray> data(final Function<HRule, JsonObject> supplier) {
        return this.ready(supplier, (fieldCodes, execMap) -> {
            final ConcurrentMap<String, JsonArray> data = new ConcurrentHashMap<>();
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
        });
    }

    private <T> T ready(
        final Function<HRule, JsonObject> supplier,
        final BiFunction<ConcurrentMap<String, Integer>, ConcurrentMap<Integer, Kv<JsonObject, RDao>>, T> executor) {
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
        return executor.apply(fieldCodes, execMap);
    }
}
