package io.vertx.tp.plugin.elasticsearch;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.fn.Fn;

import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface EsAmbit {

    static EsAmbit create(final ChangeFlag flag, final String index, final JsonObject options) {
        final ConcurrentMap<String, EsAmbit> pool = Pool.ES_CACHE.get(flag);
        return Fn.pool(pool, index, () -> Pool.ES_FUN.get(flag).apply(index, options));
    }

    JsonObject process(String documentId, JsonObject body);

    Boolean process(JsonArray documents, String idField);
}
