package io.modello.dynamic.modular.phantom;

import cn.vertxup.atom.domain.tables.daos.MJoinDao;
import cn.vertxup.atom.domain.tables.pojos.MJoin;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

class JoinModeler implements AoModeler {

    private static final Annal LOGGER = Annal.get(JoinModeler.class);

    @Override
    public Function<JsonObject, Future<JsonObject>> apply() {
        return modelJson -> {
            LOGGER.debug("[ Ox ] 3. AoModeler.join() ：{0}", modelJson.encode());
            final JsonObject model = modelJson.getJsonObject(KName.MODEL);
            // 查询 join
            return Ux.Jooq.on(MJoinDao.class)
                .<MJoin>fetchAndAsync(this.onCriteria(model))
                .compose(nexuses -> Ux.future(this.onResult(modelJson, nexuses)))
                .compose(nexuses -> Ux.future(this.onNext(modelJson, nexuses)));
        };
    }

    @Override
    public JsonObject executor(final JsonObject modelJson) {
        LOGGER.debug("[ Ox ] (Sync) 3. AoModeler.join() ：{0}", modelJson.encode());
        final JsonObject model = modelJson.getJsonObject(KName.MODEL);
        // List
        final List<MJoin> joins = Ux.Jooq.on(MJoinDao.class)
            .fetchAnd(this.onCriteria(model));
        // JsonArray
        this.onResult(modelJson, joins);
        // JsonObject
        return this.onNext(modelJson, joins);
    }

    private List<MJoin> onResult(final JsonObject modelJson,
                                 final List<MJoin> nexusList) {
        final JsonArray joins = new JsonArray();
        nexusList.stream()
            .map(join -> ((JsonObject) Ut.serializeJson(join)))
            .forEach(joins::add);
        modelJson.put(KName.Modeling.JOINS, joins);
        return nexusList;
    }

    private JsonObject onNext(final JsonObject modelJson,
                              final List<MJoin> nexusList) {
        // 填充joins节点
        final JsonObject filters = new JsonObject();
        final JsonObject model = modelJson.getJsonObject(KName.MODEL);
        filters.put(KName.NAMESPACE, model.getValue(KName.NAMESPACE));
        // 设置entity的Id集合
        final JsonArray identifiers = new JsonArray();
        nexusList.stream().filter(Objects::nonNull).map(MJoin::getEntity)
            .forEach(identifiers::add);
        // 使用identifier查询
        filters.put("identifier,i", identifiers);
        modelJson.put("entityFilters", filters);
        return modelJson;
    }

    private JsonObject onCriteria(final JsonObject model) {
        final JsonObject filters = new JsonObject();
        filters.put(KName.NAMESPACE, model.getValue(KName.NAMESPACE));
        filters.put(KName.MODEL, model.getValue(KName.IDENTIFIER));
        return filters;
    }
}
