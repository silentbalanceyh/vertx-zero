package cn.vertxup.ambient.service;

import cn.vertxup.ambient.domain.tables.daos.XModuleDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cache.AcCache;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.business.ExModel;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

public class ModelService implements ModelStub {
    @Override
    public Future<JsonObject> fetchModule(final String appId, final String entry) {
        final JsonObject filters = new JsonObject()
            .put("", Boolean.TRUE)
            .put("entry", entry)
            .put("appId", appId);
        /*
         * Cache Module for future usage
         */
        return AcCache.getModule(filters, () -> Ux.Jooq.on(XModuleDao.class)
            .fetchOneAsync(filters)
            .compose(Ux::futureJ)
            /* Metadata field usage */
            .compose(Ut.ifJObject(KName.METADATA)));
    }

    @Override
    public Future<JsonArray> fetchModels(final String sigma) {
        return Ke.channel(ExModel.class, JsonArray::new,
            model -> model.fetchAsync(sigma));
    }

    @Override
    public Future<JsonArray> fetchAttrs(final String identifier, final String sigma) {
        return Ke.channel(ExModel.class, JsonArray::new,
            model -> model.fetchAttrs(identifier, sigma));
    }
}
