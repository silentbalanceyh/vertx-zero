package io.modello.dynamic.modular.phantom;

import cn.vertxup.atom.domain.tables.daos.MFieldDao;
import cn.vertxup.atom.domain.tables.pojos.MField;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

import java.util.List;
import java.util.function.Function;

class FieldModeler implements AoModeler {

    private static final Annal LOGGER = Annal.get(FieldModeler.class);

    @Override
    public Function<JsonObject, Future<JsonObject>> apply() {
        return schemaJson -> {
            LOGGER.debug("[ Ox ] 6.1. AoModeler.field() ：{0}", schemaJson.encode());
            // 读取所有的Fields
            final JsonObject entityJson = AoModeler.getEntity(schemaJson);
            return Ux.Jooq.on(MFieldDao.class)
                .<MField>fetchAndAsync(this.onCriteria(entityJson))
                .compose(Ux::futureA)
                .compose(fields -> Ux.future(this.onResult(schemaJson, fields)));
        };
    }

    @Override
    public JsonObject executor(final JsonObject schemaJson) {
        LOGGER.debug("[ Ox ] (Sync) 6.1. AoModeler.field() ：{0}", schemaJson.encode());
        final JsonObject entityJson = AoModeler.getEntity(schemaJson);
        // List
        final List<MField> fields = Ux.Jooq.on(MFieldDao.class)
            .fetchAnd(this.onCriteria(entityJson));
        // JsonArray
        final JsonArray fieldArr = Ux.toJson(fields);
        return this.onResult(schemaJson, fieldArr);
    }

    private JsonObject onResult(final JsonObject schemaJson,
                                final JsonArray fields) {
        return schemaJson.put(KName.Modeling.FIELDS, fields);
    }

    private JsonObject onCriteria(final JsonObject entityJson) {
        final JsonObject filters = new JsonObject();
        filters.put("entityId", entityJson.getString(KName.KEY));
        return filters;
    }
}