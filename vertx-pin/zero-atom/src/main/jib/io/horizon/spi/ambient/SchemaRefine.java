package io.horizon.spi.ambient;

import cn.vertxup.atom.domain.tables.daos.MEntityDao;
import cn.vertxup.atom.domain.tables.daos.MFieldDao;
import cn.vertxup.atom.domain.tables.daos.MKeyDao;
import cn.vertxup.atom.domain.tables.pojos.MEntity;
import cn.vertxup.atom.domain.tables.pojos.MField;
import cn.vertxup.atom.domain.tables.pojos.MKey;
import io.horizon.eon.VString;
import io.horizon.eon.em.typed.ChangeFlag;
import io.modello.dynamic.modular.jdbc.Pin;
import io.modello.dynamic.modular.metadata.AoBuilder;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.Model;
import io.vertx.mod.atom.modeling.Schema;
import io.vertx.mod.atom.refine.Ao;
import io.vertx.up.commune.config.Database;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import static io.vertx.mod.atom.refine.Ao.LOG;

class SchemaRefine implements AoRefine {

    @Override
    public Function<JsonObject, Future<JsonObject>> apply() {
        return appJson -> {

            // 读取上一个流程中处理完成的 models
            final JsonArray models = appJson.getJsonArray(KName.Modeling.MODELS);
            final String name = appJson.getString(KName.NAME);
            final Set<Schema> schemata = this.toSchemata(models, name);
            LOG.Uca.info(this.getClass(), "2. AoRefine.schema(): {0}", String.valueOf(schemata.size()));

            // 1. 处理 Schema 的同步
            final JsonObject source = appJson.getJsonObject(KName.SOURCE);
            this.syncDatabase(source, schemata);

            // 2. 更新 MEntity 相关内容
            final List<Future<JsonObject>> futures = new ArrayList<>();
            schemata.stream().map(this::saveSchema).forEach(futures::add);
            return Fn.combineA(futures)
                .compose(nil -> Ux.future(appJson))
                .otherwise(Ux.otherwise(() -> null));
        };
    }

    private void syncDatabase(final JsonObject source, final Set<Schema> schemata) {
        final Database database = new Database();
        database.fromJson(source);
        LOG.Uca.info(this.getClass(), "Database Extraction: {0}", database.toJson());
        final Pin pin = Pin.getInstance();
        final AoBuilder builder = pin.getBuilder(database);

        schemata.forEach(builder::synchron);
    }

    private Set<Schema> toSchemata(final JsonArray models, final String appName) {
        final Set<Schema> schemata = new HashSet<>();
        Ut.itJArray(models)
            .map(data -> Ao.toModel(appName, data))
            .map(Model::schema)
            .forEach(schemata::addAll);
        return schemata;
    }

    private JsonObject criteria(final String name, final MEntity entity) {
        final JsonObject filters = new JsonObject();
        filters.put(KName.NAME, name);
        filters.put(KName.ENTITY_ID, entity.getKey());
        filters.put(VString.EMPTY, Boolean.TRUE);
        return filters;
    }

    private Set<String> uniqueSet() {
        return new HashSet<>() {
            {
                this.add(KName.NAME);
                this.add(KName.ENTITY_ID);
            }
        };
    }

    private JsonObject criteria(final MEntity entity) {
        final JsonObject filters = new JsonObject();
        filters.put(KName.NAMESPACE, entity.getNamespace());
        filters.put(KName.IDENTIFIER, entity.getIdentifier());
        filters.put(VString.EMPTY, Boolean.TRUE);
        return filters;
    }

    private Future<JsonObject> saveSchema(final Schema schema) {
        final MEntity updated = schema.getEntity();
        return Ux.Jooq.on(MEntityDao.class)
            .upsertAsync(this.criteria(updated), updated)
            .compose(entity -> {
                // 设置关系信息重建
                schema.connect(entity.getKey());
                final List<Future<JsonArray>> combine = new ArrayList<>();
                // Schema -> Field
                combine.add(this.saveField(schema, entity));
                // Schema -> Key
                combine.add(this.saveKey(schema, entity));
                return Fn.compressA(combine)
                    .compose(nil -> Ux.future(entity))
                    .compose(Ux::futureJ)
                    .otherwise(Ux.otherwise(() -> null));
            });
    }

    private Future<JsonArray> saveKey(final Schema schema, final MEntity entity) {
        final JsonObject condition = new JsonObject();
        // Schema -> Field
        final MKey[] keys = schema.getKeys();
        for (int idx = 0; idx < keys.length; idx++) {
            final MKey field = keys[idx];
            condition.put("$" + idx, this.criteria(field.getName(), entity));
        }
        final UxJooq jq = Ux.Jooq.on(MKeyDao.class);
        return jq.<MKey>fetchAsync(condition).compose(queried -> {
            final List<MKey> fieldList = Arrays.asList(keys);
            final ConcurrentMap<ChangeFlag, List<MKey>> compared = Ux.compare(queried, fieldList, this.uniqueSet());
            return Ux.compareRun(compared, jq::insertAsync, jq::updateAsync);
        });
    }

    private Future<JsonArray> saveField(final Schema schema, final MEntity entity) {
        final JsonObject condition = new JsonObject();
        // Schema -> Field
        final MField[] fields = schema.getFields();
        for (int idx = 0; idx < fields.length; idx++) {
            final MField field = fields[idx];
            condition.put("$" + idx, this.criteria(field.getName(), entity));
        }
        final UxJooq jq = Ux.Jooq.on(MFieldDao.class);
        return jq.<MField>fetchAsync(condition).compose(queried -> {
            final List<MField> fieldList = Arrays.asList(fields);
            final ConcurrentMap<ChangeFlag, List<MField>> compared = Ux.compare(queried, fieldList, this.uniqueSet());
            return Ux.compareRun(compared, jq::insertAsync, jq::updateAsync);
        });
    }
}
