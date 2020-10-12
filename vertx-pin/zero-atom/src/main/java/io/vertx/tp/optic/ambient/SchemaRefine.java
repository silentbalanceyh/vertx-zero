package io.vertx.tp.optic.ambient;

import cn.vertxup.atom.domain.tables.daos.MEntityDao;
import cn.vertxup.atom.domain.tables.daos.MFieldDao;
import cn.vertxup.atom.domain.tables.daos.MKeyDao;
import cn.vertxup.atom.domain.tables.pojos.MEntity;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.modeling.Schema;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.ke.booter.Bt;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.modular.jdbc.Pin;
import io.vertx.tp.modular.metadata.AoBuilder;
import io.vertx.up.commune.config.Database;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.function.Function;

class SchemaRefine implements AoRefine {

    @Override
    public Function<JsonObject, Future<JsonObject>> apply() {
        return appJson -> {

            // 读取上一个流程中处理完成的 models
            final JsonArray models = appJson.getJsonArray(KeField.Modeling.MODELS);
            final String name = appJson.getString(KeField.NAME);
            final Set<Schema> schemata = this.toSchemata(models, Model.namespace(name));
            Ao.infoUca(this.getClass(), "2. AoRefine.schema(): {0}", String.valueOf(schemata.size()));

            // 1. 处理 Schema 的同步
            final JsonObject source = appJson.getJsonObject(KeField.SOURCE);
            this.syncDatabase(source, schemata);

            // 2. 更新 MEntity 相关内容
            final List<Future<JsonObject>> futures = new ArrayList<>();
            schemata.stream().map(this::saveSchema).forEach(futures::add);
            return Ux.thenCombine(futures)
                    .compose(nil -> Ux.future(appJson));
        };
    }

    private void syncDatabase(final JsonObject source, final Set<Schema> schemata) {
        final Database database = new Database();
        database.fromJson(source);
        Ao.infoUca(this.getClass(), "Database Extraction: {0}", database.toJson());
        final Pin pin = Pin.getInstance();
        final AoBuilder builder = pin.getBuilder(database);

        schemata.forEach(builder::synchron);
    }

    private Set<Schema> toSchemata(final JsonArray models, final String namespace) {
        final Set<Schema> schemata = new HashSet<>();
        Ut.itJArray(models)
                .map(data -> Model.instance(namespace, data))
                .map(Model::getSchemata)
                .forEach(schemata::addAll);
        return schemata;
    }

    private JsonObject onCriteria(final String name, final MEntity entity) {
        final JsonObject filters = new JsonObject();
        filters.put(KeField.NAME, name);
        filters.put(KeField.ENTITY_ID, entity.getKey());
        return filters;
    }

    private JsonObject onCriteria(final MEntity entity) {
        final JsonObject filters = new JsonObject();
        filters.put(KeField.NAMESPACE, entity.getNamespace());
        filters.put(KeField.IDENTIFIER, entity.getIdentifier());
        return filters;
    }

    /*
     * 暂留异步导入
     */
    private Future<JsonObject> saveSchemaAsync(final Schema schema) {
        final Promise<JsonObject> promise = Promise.promise();
        final WorkerExecutor executor = Bt.getWorker("schema - " + schema.identifier());
        executor.<JsonObject>executeBlocking(
                pre -> pre.handle(this.saveSchema(schema)),
                post -> promise.complete(post.result())
        );
        return promise.future();
    }

    private Future<JsonObject> saveSchema(final Schema schema) {
        /*
         * 这里有问题，需要先针对 schema 执行 upsert 动作
         * 这样才能同步 entity 的内容，而 Field 以及 Key 和 Index都是依赖 upsert 过后拿到的
         * 主键，所以老代码需要处理掉
         */
        /* 旧代码
        // Schema -> Field
        final MEntity entity = schema.getEntity();
        final List<Future<JsonObject>> futures = new ArrayList<>();
        Arrays.stream(schema.getFields()).map(field -> Ux.Jooq.on(MFieldDao.class)
                .upsertAsync(this.onCriteria(field.name(), entity), field)
                .compose(Ux::fnJObject))
                .forEach(futures::add);

        // Schema -> Key
        Arrays.stream(schema.getKeys()).map(key -> Ux.Jooq.on(MKeyDao.class)
                .upsertAsync(this.onCriteria(key.name(), entity), key)
                .compose(Ux::fnJObject))
                .forEach(futures::add);

        return Ux.thenComposite(futures)
                .compose(nil -> Ux.Jooq.on(MEntityDao.class)
                        .upsertAsync(this.onCriteria(entity), entity))
                .compose(Ux::fnJObject);
         */
        final MEntity updated = schema.getEntity();
        return Ux.Jooq.on(MEntityDao.class)
                .upsertAsync(this.onCriteria(updated), updated)
                .compose(entity -> {
                    // 设置关系信息重建
                    schema.setRelation(entity.getKey());
                    final List<Future<JsonObject>> futures = new ArrayList<>();
                    // Schema -> Field
                    Arrays.stream(schema.getFields()).map(field -> Ux.Jooq.on(MFieldDao.class)
                            .upsertAsync(this.onCriteria(field.getName(), entity), field)
                            .compose(Ux::futureJ))
                            .forEach(futures::add);

                    // Schema -> Key
                    Arrays.stream(schema.getKeys()).map(key -> Ux.Jooq.on(MKeyDao.class)
                            .upsertAsync(this.onCriteria(key.getName(), entity), key)
                            .compose(Ux::futureJ))
                            .forEach(futures::add);
                    // Schema -> Index
                    return Ux.thenCombine(futures)
                            .compose(nil -> Ux.future(entity))
                            .compose(Ux::futureJ);
                });
    }
}
