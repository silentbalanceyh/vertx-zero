package io.vertx.tp.modular.phantom;

import cn.vertxup.atom.domain.tables.pojos.MModel;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class ModelTool {
    private final transient String appName;

    ModelTool(final String appName) {
        this.appName = appName;
    }

    Future<Set<Model>> combine(final JsonArray models) {
        final Set<Model> modelSet = new HashSet<>();
        Ut.itJArray(models).map(data -> Ao.toModel(this.appName, data))
            .forEach(modelSet::add);
        return Ux.future(modelSet);
    }

    Future<JsonArray> startList(final List<MModel> modelList) {
        final List<Future<JsonObject>> futures = new ArrayList<>();
        modelList.stream().map(this::execute).forEach(futures::add);
        return Ux.thenCombine(futures);
    }

    Future<JsonObject> execute(final MModel model) {
        return Ux.future(this.onResult(model))
            // 1. 先初始化一个Model
            .compose(AoModeler.init().apply())
            // 2. 属性读取
            .compose(AoModeler.attribute().apply())
            // 3. 关系拉取
            .compose(AoModeler.join().apply())
            // 4. 读取实体
            .compose(AoModeler.entity().apply())
            // 5. 分散处理
            .compose(AoModeler.scatter().apply());
    }

    private JsonObject onResult(final MModel model) {
        return null == model ? new JsonObject() : Ut.serializeJson(model);
    }

    JsonObject onCriteria(final String identifier) {
        final String namespace = Ao.toNS(this.appName);
        final JsonObject filters = new JsonObject();
        filters.put("namespace", namespace);
        filters.put("identifier", identifier);
        return filters;
    }
}
