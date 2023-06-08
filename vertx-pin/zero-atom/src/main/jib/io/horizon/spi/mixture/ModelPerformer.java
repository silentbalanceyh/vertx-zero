package io.horizon.spi.mixture;

import cn.vertxup.atom.domain.tables.daos.MModelDao;
import cn.vertxup.atom.domain.tables.pojos.MModel;
import io.horizon.uca.cache.Cc;
import io.modello.dynamic.modular.phantom.AoModeler;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.Model;
import io.vertx.mod.atom.refine.Ao;
import io.vertx.up.exception.web._404ModelNotFoundException;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModelPerformer implements AoPerformer {
    private static final Cc<String, ModelInternal> CC_TOOL = Cc.open();
    private final transient String appName;
    private final transient ModelInternal tool;

    ModelPerformer(final String appName) {
        this.appName = appName;
        this.tool = CC_TOOL.pick(() -> new ModelInternal(this.appName), this.appName);
    }

    @Override
    public Future<Model> fetchAsync(final String identifier) {
        return Ux.Jooq.on(MModelDao.class)
            // 从系统中选取唯一的 MModel
            .<MModel>fetchOneAsync(this.tool.onCriteria(identifier))
            // 先转换
            .compose(this.tool::execute)
            // 6. 将实体合并，处理实体中的细节
            .compose(item -> Ux.future(Ao.toModel(this.appName, item)));
    }


    @Override
    public Model fetch(final String identifier) {
        final MModel model = Ux.Jooq.on(MModelDao.class)
            .fetchOne(this.tool.onCriteria(identifier));
        final String namespace = Ao.toNS(this.appName);
        Fn.outWeb(null == model, _404ModelNotFoundException.class, this.getClass(), namespace, identifier);
        JsonObject json = Ut.serializeJson(model);
        // 1. 初始化
        json = AoModeler.init().executor(json);
        // 2. 属性处理
        json = AoModeler.attribute().executor(json);
        // 3. 关系处理
        json = AoModeler.join().executor(json);
        // 4. 实体处理
        json = AoModeler.entity().executor(json);
        // 5. Scatter处理（分模型）
        json = AoModeler.scatter().executor(json);
        // 6. 返回最终结果
        return Ao.toModel(this.appName, json); // Model.instance(this.namespace, json);
    }

    @Override
    public Future<Set<Model>> fetchAsync() {
        final String namespace = Ao.toNS(this.appName);
        return Ux.Jooq.on(MModelDao.class)
            .<MModel>fetchAsync("namespace", namespace)
            .compose(this.tool::startList)
            .compose(this.tool::combine);
    }

    /*
     * 「Private Class」
     * Private Model Tool, it will be used inner
     * `ModelPerformer` and could not be exposed to outer
     */
    private static class ModelInternal {
        private final transient String appName;

        private ModelInternal(final String appName) {
            this.appName = appName;
        }

        private Future<Set<Model>> combine(final JsonArray models) {
            final Set<Model> modelSet = new HashSet<>();
            Ut.itJArray(models).map(data -> Ao.toModel(this.appName, data))
                .forEach(modelSet::add);
            return Ux.future(modelSet);
        }

        private Future<JsonArray> startList(final List<MModel> modelList) {
            final List<Future<JsonObject>> futures = new ArrayList<>();
            modelList.stream().map(this::execute).forEach(futures::add);
            return Fn.combineA(futures);
        }

        private Future<JsonObject> execute(final MModel model) {
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

        private JsonObject onCriteria(final String identifier) {
            final String namespace = Ao.toNS(this.appName);
            final JsonObject filters = new JsonObject();
            filters.put("namespace", namespace);
            filters.put("identifier", identifier);
            return filters;
        }
    }
}
