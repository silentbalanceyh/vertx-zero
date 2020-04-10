package io.vertx.tp.optic.ambient;

import cn.vertxup.atom.domain.tables.daos.MAttributeDao;
import cn.vertxup.atom.domain.tables.daos.MJoinDao;
import cn.vertxup.atom.domain.tables.daos.MModelDao;
import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import cn.vertxup.atom.domain.tables.pojos.MJoin;
import cn.vertxup.atom.domain.tables.pojos.MModel;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.ke.booter.Bt;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

class ModelRefine implements AoRefine {

    @Override
    public Function<JsonObject, Future<JsonObject>> apply() {
        return appJson -> {
            Ao.infoUca(this.getClass(), "3. AoRefine.model(): {0}", appJson.encode());
            // 读取上一个流程中处理完成的 models
            final JsonArray modelJson = appJson.getJsonArray(KeField.Modeling.MODELS);
            final String name = appJson.getString(KeField.NAME);
            final Set<Model> models = this.toModels(modelJson, Model.namespace(name));
            // 1. 更新某一个模型
            final List<Future<JsonObject>> futures = new ArrayList<>();
            models.stream().map(this::saveModel).forEach(futures::add);
            return Ux.thenCombine(futures)
                    .compose(nil -> Ux.future(appJson));
        };
    }

    private Set<Model> toModels(final JsonArray modelJson, final String namespace) {
        final Set<Model> models = new HashSet<>();
        Ut.itJArray(modelJson).map(data -> Model.instance(namespace, data))
                .forEach(models::add);
        return models;
    }

    private JsonObject onCriteria(final MAttribute attribute) {
        final JsonObject filters = new JsonObject();
        filters.put(KeField.NAME, attribute.getName());
        filters.put(KeField.MODEL_ID, attribute.getModelId());
        return filters;
    }

    private JsonObject onCriteria(final MModel entity) {
        final JsonObject filters = new JsonObject();
        filters.put(KeField.NAMESPACE, entity.getNamespace());
        filters.put(KeField.IDENTIFIER, entity.getIdentifier());
        return filters;
    }

    private JsonObject onCriteria(final MJoin nexus) {
        final JsonObject filters = new JsonObject();
        filters.put(KeField.NAMESPACE, nexus.getNamespace());
        filters.put(KeField.MODEL, nexus.getModel());
        return filters;
    }

    /*
     * 暂留异步导入
     */
    private Future<JsonObject> saveModelAsync(final Model model) {
        final Promise<JsonObject> promise = Promise.promise();
        final WorkerExecutor executor = Bt.getWorker("model - " + model.identifier());
        executor.<JsonObject>executeBlocking(
                pre -> pre.handle(this.saveModel(model)),
                post -> promise.complete(post.result())
        );
        return promise.future();
    }

    private Future<JsonObject> saveModel(final Model model) {
        // Model -> Attributes下边的属性
        final List<Future<JsonObject>> futures = new ArrayList<>();
        model.getAttributes().stream().map(attr -> Ux.Jooq.on(MAttributeDao.class)
                .upsertAsync(this.onCriteria(attr), attr)
                .compose(Ux::fnJObject))
                .forEach(futures::add);
        // Model -> 插入 Model
        /*
         * 旧代码：final List<Future<JsonObject>> schemata = new ArrayList<>();
         * 旧代码：model.getSchemata().stream().map(OxInit::saveSchema).forEach(schemata::add);
         */
        // Model -> Nexus处理关系
        model.getJoins().stream().map(nexus -> Ux.Jooq.on(MJoinDao.class)
                // 先删除原始关系
                .<MJoin, String>deleteAsync(this.onCriteria(nexus))
                // 再插入新关系
                .compose(nil -> Ux.Jooq.on(MJoinDao.class)
                        .insertAsync(nexus))
                .compose(Ux::fnJObject))
                .forEach(futures::add);
        /*
         * 同时插入
         */
        return Ux.thenCombine(futures)
                /*
                 * 旧代码：.compose(nil -> Ux.thenComposite(schemata))
                 */
                .compose(nil -> Ux.Jooq.on(MModelDao.class)
                        .upsertAsync(this.onCriteria(model.getModel()), model.getModel()))
                .compose(Ux::fnJObject);
    }
}
