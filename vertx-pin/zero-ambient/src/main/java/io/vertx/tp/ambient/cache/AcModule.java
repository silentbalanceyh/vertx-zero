package io.vertx.tp.ambient.cache;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class AcModule {
    /*
     * Module cache for each application
     * Normalize standard module here with `Ex.yiStandard` method to fast module configuration fetching
     */
    private static final ConcurrentMap<String, JsonObject> CACHE_MODULE = new ConcurrentHashMap<>();

    static Future<JsonObject> getModule(final JsonObject condition, final Supplier<Future<JsonObject>> executor) {
        final String appId = condition.getString("appId");
        final String entry = condition.getString("entry");
        if (Ut.isNilOr(appId, entry)) {
            return Ux.futureJObject();
        } else {
            final String cacheKey = appId + ":" + entry;
            final JsonObject cachedData = CACHE_MODULE.getOrDefault(cacheKey, null);
            if (Objects.isNull(cachedData)) {
                return Ux.future(new JsonObject());
            } else {
                return executor.get().compose(dataData -> {
                    if (Objects.nonNull(dataData)) {
                        CACHE_MODULE.put(cacheKey, dataData);
                    }
                    return Ux.future(dataData);
                });
            }
        }
    }
}
