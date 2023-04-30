package cn.vertxup.ambient.service;

import cn.vertxup.ambient.domain.tables.daos.XModuleDao;
import io.horizon.spi.environment.Modeling;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.log.DevEnv;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public class ModelService implements ModelStub {
    /*
     * Module cache for each application
     * Normalize standard module here with `Ex.yiStandard` method to fast module configuration fetching
     */
    private static final ConcurrentMap<String, JsonObject> CACHE_MODULE = new ConcurrentHashMap<>();

    @Override
    public Future<JsonObject> fetchModule(final String appId, final String entry) {
        final JsonObject filters = new JsonObject()
            .put("", Boolean.TRUE)
            .put("entry", entry)
            .put("appId", appId);
        /*
         * Cache Module for future usage
         */
        return this.fetchModule(filters, () -> Ux.Jooq.on(XModuleDao.class)
            .fetchOneAsync(filters)
            .compose(Ux::futureJ)
            /* Metadata field usage */
            .compose(Fn.ofJObject(KName.METADATA)));
    }

    @Override
    public Future<JsonArray> fetchModels(final String sigma) {
        return Ux.channel(Modeling.class, JsonArray::new,
            model -> model.fetchAsync(sigma));
    }

    @Override
    public Future<JsonArray> fetchAttrs(final String identifier, final String sigma) {
        return Ux.channel(Modeling.class, JsonArray::new,
            model -> model.fetchAttrs(identifier, sigma));
    }

    private Future<JsonObject> fetchModule(final JsonObject condition, final Supplier<Future<JsonObject>> executor) {
        final String appId = condition.getString("appId");
        final String entry = condition.getString("entry");
        if (Ut.isNil(appId, entry)) {
            return Ux.futureJ();
        } else {
            final String cacheKey = appId + ":" + entry;
            /*
             * Ui Cache Enabled for this processing
             */
            if (DevEnv.cacheUi()) {
                // Cache enabled
                final JsonObject cachedData = CACHE_MODULE.getOrDefault(cacheKey, null);
                if (Objects.isNull(cachedData)) {
                    return executor.get().compose(dataData -> {
                        if (Objects.nonNull(dataData)) {
                            CACHE_MODULE.put(cacheKey, dataData);
                        }
                        return Ux.future(dataData);
                    });
                } else {
                    return Ux.future(cachedData);
                }
            } else {
                // Cache disabled
                return executor.get();
            }
        }
    }
}
