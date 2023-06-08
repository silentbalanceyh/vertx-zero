package io.modello.dynamic.modular.phantom;

import cn.vertxup.atom.domain.tables.daos.MEntityDao;
import cn.vertxup.atom.domain.tables.pojos.MEntity;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.function.Function;

class EntityModeler implements AoModeler {
    private static final Annal LOGGER = Annal.get(AttributeModeler.class);

    @Override
    public Function<JsonObject, Future<JsonObject>> apply() {
        return modelJson -> {
            LOGGER.debug("[ Ox ] 4. AoModeler.entity() ：{0}", modelJson.encode());
            final JsonObject filters = modelJson.getJsonObject("entityFilters");
            return Ux.Jooq.on(MEntityDao.class)
                .<MEntity>fetchAndAsync(filters)
                .compose(Ux::futureA)
                .compose(list -> Ux.future(this.onResult(modelJson, list)));
        };
    }

    @Override
    public JsonObject executor(final JsonObject modelJson) {
        LOGGER.debug("[ Ox ] (Sync) 4. AoModeler.entity() ：{0}", modelJson.encode());
        final JsonObject filters = modelJson.getJsonObject("entityFilters");
        // List
        final List<MEntity> entities = Ux.Jooq.on(MEntityDao.class)
            .fetchAnd(filters);
        // Array
        final JsonArray entityArr = Ux.toJson(entities);
        // JsonObject
        return this.onResult(modelJson, entityArr);
    }

    private JsonObject onResult(final JsonObject modelJson,
                                final JsonArray entities) {
        // 处理Schema信息
        if (!modelJson.containsKey(KName.Modeling.SCHEMATA)) {
            final JsonArray schemata = new JsonArray();
            modelJson.put(KName.Modeling.SCHEMATA, schemata);
        }
        // 处理 schemata 信息
        final JsonArray schemata = modelJson.getJsonArray(KName.Modeling.SCHEMATA);
        Ut.itJArray(entities).forEach(schemata::add);
        modelJson.put(KName.Modeling.SCHEMATA, schemata);
        return modelJson;
    }
}