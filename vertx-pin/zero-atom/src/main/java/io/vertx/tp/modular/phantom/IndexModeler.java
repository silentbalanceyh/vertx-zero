package io.vertx.tp.modular.phantom;

import cn.vertxup.atom.domain.tables.daos.MIndexDao;
import cn.vertxup.atom.domain.tables.pojos.MIndex;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;

import java.util.List;
import java.util.function.Function;

class IndexModeler implements AoModeler {
    private static final Annal LOGGER = Annal.get(FieldModeler.class);

    @Override
    public Function<JsonObject, Future<JsonObject>> apply() {
        return schemaJson -> {
            LOGGER.debug("[ Ox ] 6.3. AoModeler.index() ：{0}", schemaJson.encode());
            final JsonObject entityJson = AoModeler.getEntity(schemaJson);
            // 读取所有的indexes
            return Ux.Jooq.on(MIndexDao.class)
                    .<MIndex>fetchAndAsync(this.onCriteria(entityJson))
                    .compose(Ux::futureA)
                    .compose(indexes -> Ux.future(this.onResult(schemaJson, indexes)));
        };
    }

    @Override
    public JsonObject executor(final JsonObject schemaJson) {
        LOGGER.debug("[ Ox ] (Sync) 6.3. AoModeler.index() ：{0}", schemaJson.encode());
        final JsonObject entityJson = AoModeler.getEntity(schemaJson);
        // List
        final List<MIndex> indexList = Ux.Jooq.on(MIndexDao.class)
                .fetchAnd(this.onCriteria(entityJson));
        // Array
        final JsonArray indexes = Ux.toJson(indexList);

        return this.onResult(schemaJson, indexes);
    }

    private JsonObject onResult(final JsonObject schemaJson,
                                final JsonArray indexes) {
        return schemaJson.put(KeField.Modeling.INDEXES, indexes);
    }

    private JsonObject onCriteria(final JsonObject entityJson) {
        final JsonObject filters = new JsonObject();
        filters.put(KeField.ENTITY_ID, entityJson.getString(KeField.KEY));
        return filters;
    }
}
