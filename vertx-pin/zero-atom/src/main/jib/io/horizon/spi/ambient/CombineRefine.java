package io.horizon.spi.ambient;

import io.horizon.spi.mixture.AoPerformer;
import io.modello.dynamic.modular.file.AoFile;
import io.modello.dynamic.modular.file.FileReader;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.Model;
import io.vertx.mod.atom.modeling.Schema;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Set;
import java.util.function.Function;

import static io.vertx.mod.atom.refine.Ao.LOG;

class CombineRefine implements AoRefine {

    private final transient AoFile marshal = Ut.singleton(FileReader.class);

    @Override
    public Function<JsonObject, Future<JsonObject>> apply() {
        return appJson -> {
            /* UCA日志 */
            LOG.Uca.info(this.getClass(), "1. AoRefine.combine(): {0}", appJson.encode());
            // 从响应信息中读取应用程序名称
            final String name = appJson.getString(KName.NAME);
            final AoPerformer performer = AoPerformer.getInstance(name);
            return performer.fetchAsync().compose(storedSet -> {
                // 读取文件中的
                final String outPath = Ut.valueString(appJson, KName.OUTPUT);
                final Set<Model> models = this.marshal.readModels(name, outPath);
                // 两边查找对比，然后更新原始引用
                models.stream().filter(storedSet::contains)
                    .forEach(jsonRef -> storedSet
                        .stream().filter(jsonRef::equals).findFirst()
                        .ifPresent(stored -> this.updateRelation(stored, jsonRef)));
                // 返回合并结果
                return Ux.future(models);
            }).compose(models -> this.onResult(appJson, models));
        };
    }

    private Future<JsonObject> onResult(final JsonObject appJson,
                                        final Set<Model> models) {
        final JsonArray modelArray = new JsonArray();
        models.stream().map(Model::toJson)
            .map(this::onAttribute)
            .forEach(modelArray::add);
        appJson.put(KName.Modeling.MODELS, modelArray);
        return Ux.future(appJson);
    }

    private JsonObject onAttribute(final JsonObject model) {
        model.put(KName.Modeling.ATTRIBUTES, model.getJsonArray(KName.Modeling.ATTRIBUTES));
        return model;
    }

    private void updateRelation(final Model stored, final Model json) {
        /*
         * 检查新的MModel的主键是否相同
         * 不相同则表示数据库中的主键需要同步到json中
         */
        if (!stored.dbModel().getKey().equals(json.dbModel().getKey())) {
            json.connect(stored.dbModel().getKey());
        }
        /*
         * 除了检查MModel以外还要检查 Schema的内容
         */
        final Set<Schema> storedSchemata = stored.schema();
        // 两边查找对比
        final Set<Schema> jsonSchemata = json.schema();
        jsonSchemata.stream().filter(storedSchemata::contains).forEach(jsonRef -> storedSchemata
            // 先查找相匹配的Schema
            .stream().filter(jsonRef::equals).findFirst()
            // 再过滤发生了主键变化的Schema
            .filter(storedRef -> !storedRef.getEntity().getKey().equals(jsonRef.getEntity().getKey()))
            // 如果找到就执行关联关系的重新设置
            .ifPresent(storedRef -> jsonRef.connect(storedRef.getEntity().getKey())));
    }
}
