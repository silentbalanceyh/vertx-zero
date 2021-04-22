package io.vertx.tp.modular.phantom;

import cn.vertxup.atom.domain.tables.daos.MModelDao;
import cn.vertxup.atom.domain.tables.pojos.MModel;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.error._404ModelNotFoundException;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ModelPerformer implements AoPerformer {
    private static final ConcurrentMap<String, ModelTool> POOL_TOOL =
            new ConcurrentHashMap<>();
    private final transient String namespace;
    private final transient ModelTool tool;

    ModelPerformer(final String appName) {
        this.namespace = Model.namespace(appName);
        this.tool = Fn.pool(POOL_TOOL, this.namespace,
                () -> new ModelTool(this.namespace));
    }

    @Override
    public Future<Model> fetchModelAsync(final String identifier) {
        return Ux.Jooq.on(MModelDao.class)
                // 从系统中选取唯一的 MModel
                .<MModel>fetchOneAsync(this.tool.onCriteria(identifier))
                // 先转换
                .compose(this.tool::execute)
                // 6. 将实体合并，处理实体中的细节
                .compose(item -> Ux.future(Model.instance(this.namespace, item)));
    }


    @Override
    public Model fetchModel(final String identifier) {
        final MModel model = Ux.Jooq.on(MModelDao.class)
                .fetchOne(this.tool.onCriteria(identifier));
        Fn.outWeb(null == model, _404ModelNotFoundException.class, this.getClass(), this.namespace, identifier);
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
        return Model.instance(this.namespace, json);
    }

    @Override
    public Future<Set<Model>> fetchModelsAsync() {
        return Ux.Jooq.on(MModelDao.class)
                .<MModel>fetchAsync("namespace", this.namespace)
                .compose(this.tool::startList)
                .compose(this.tool::combine);
    }
}
