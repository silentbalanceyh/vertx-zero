package cn.vertxup.ambient.service;

import cn.vertxup.ambient.domain.tables.daos.XModuleDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.business.ExModel;
import io.vertx.up.unity.Ux;

public class ModelService implements ModelStub {
    @Override
    public Future<JsonObject> fetchModule(final String appId, final String entry) {
        final JsonObject filters = new JsonObject()
                .put("", Boolean.TRUE)
                .put("entry", entry)
                .put("appId", appId);
        return Ux.Jooq.on(XModuleDao.class)
                .fetchOneAsync(filters)
                .compose(Ux::fnJObject)
                /* Metadata field usage */
                .compose(Ke.mount(KeField.METADATA));
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
