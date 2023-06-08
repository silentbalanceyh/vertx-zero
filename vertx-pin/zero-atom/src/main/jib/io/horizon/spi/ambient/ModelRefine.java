package io.horizon.spi.ambient;

import cn.vertxup.atom.domain.tables.daos.MAttributeDao;
import cn.vertxup.atom.domain.tables.daos.MJoinDao;
import cn.vertxup.atom.domain.tables.daos.MModelDao;
import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import cn.vertxup.atom.domain.tables.pojos.MJoin;
import cn.vertxup.atom.domain.tables.pojos.MModel;
import io.horizon.eon.VString;
import io.horizon.eon.em.typed.ChangeFlag;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.Model;
import io.vertx.mod.atom.refine.Ao;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import static io.vertx.mod.atom.refine.Ao.LOG;

class ModelRefine implements AoRefine {

    @Override
    public Function<JsonObject, Future<JsonObject>> apply() {
        return appJson -> {
            // 读取上一个流程中处理完成的 models
            final JsonArray modelJson = appJson.getJsonArray(KName.Modeling.MODELS);
            final String name = appJson.getString(KName.NAME);
            final Set<Model> models = new HashSet<>();
            Ut.itJArray(modelJson).map(data -> Ao.toModel(name, data))
                .forEach(models::add);
            LOG.Uca.info(this.getClass(), "3. AoRefine.model(): {0}", String.valueOf(models.size()));
            // 1. 更新某一个模型
            final List<Future<JsonObject>> futures = new ArrayList<>();
            models.stream().map(this::saveModelAsync).forEach(futures::add);
            return Fn.combineA(futures)
                .compose(nil -> Ux.future(appJson));
        };
    }

    /*
     * 异步导入Model
     */
    private Future<JsonObject> saveModelAsync(final Model model) {
        return Ux.nativeWorker("model-" + model.identifier(), pre -> {
            // Model -> Attributes 批量执行
            final List<Future<JsonArray>> futures = new ArrayList<>();
            futures.add(this.saveAttrAsync(model));
            // Model -> 插入 Model
            futures.add(this.saveJoinAsync(model));
            // Entity
            final MModel entity = model.dbModel();
            final JsonObject criteria = new JsonObject();
            criteria.put(KName.NAMESPACE, entity.getNamespace());
            criteria.put(KName.IDENTIFIER, entity.getIdentifier());
            LOG.Uca.info(this.getClass(), "3. AoRefine.model(): Model `{0}`, Upsert Criteria = `{1}`",
                entity.getIdentifier(), criteria.encode());
            final Future<JsonObject> execute = Fn.compressA(futures)
                .compose(nil -> Ux.Jooq.on(MModelDao.class).upsertAsync(criteria, model.dbModel()))
                .compose(Ux::futureJ);
            execute.onSuccess(pre::complete);
        });
    }

    /*
     * 异步导入MAttribute
     */
    private Future<JsonArray> saveAttrAsync(final Model model) {
        // 0. 条件专用函数
        final Function<MAttribute, JsonObject> fnQuery = attribute -> {
            final JsonObject filters = new JsonObject();
            filters.put(KName.NAME, attribute.getName());
            filters.put(KName.MODEL_ID, attribute.getModelId());
            filters.put(VString.EMPTY, Boolean.TRUE);
            return filters;
        };
        // 1. 构造属性专用条件
        final JsonObject criteria = new JsonObject();
        model.dbAttributes().stream().map(fnQuery)
            .forEach(each -> criteria.put("$" + each.hashCode(), each));
        criteria.put(VString.EMPTY, Boolean.FALSE);
        // 2. 从数据库中读取原始属性
        final UxJooq jooqAttr = Ux.Jooq.on(MAttributeDao.class);
        return jooqAttr.<MAttribute>fetchAsync(criteria).compose(original -> {
            // 3. 唯一业务属性
            final Set<String> uniqueSet = new HashSet<>();
            uniqueSet.add(KName.MODEL_ID);
            uniqueSet.add(KName.NAME);
            // 4. 比对结果处理
            final ConcurrentMap<ChangeFlag, List<MAttribute>> compared = Ux.compare(original, new ArrayList<>(model.dbAttributes()), uniqueSet);
            return Ux.future(compared);
        }).compose(compared -> Ux.compareRun(compared, jooqAttr::insertAsync, jooqAttr::updateAsync));
    }

    /*
     * 异步导入MJoin
     */
    private Future<JsonArray> saveJoinAsync(final Model model) {
        // 0. 条件专用函数
        final Function<MJoin, JsonObject> fnQuery = nexus -> {
            final JsonObject filters = new JsonObject();
            filters.put(KName.NAMESPACE, nexus.getNamespace());
            filters.put(KName.MODEL, nexus.getModel());
            return filters;
        };
        // 1. 构造链接条件
        final JsonObject criteria = new JsonObject();
        model.dbJoins().stream().map(fnQuery)
            .forEach(each -> criteria.put("$" + each.hashCode(), each));
        // 2. 从数据库中读取初始属性
        final UxJooq jooqJoin = Ux.Jooq.on(MJoinDao.class);
        return jooqJoin.<MJoin>fetchAsync(criteria).compose(original -> {
            // 3. 唯一性业务
            final Set<String> uniqueSet = new HashSet<>();
            uniqueSet.add(KName.NAMESPACE);
            uniqueSet.add(KName.MODEL);
            // 4. 比对结果处理
            final ConcurrentMap<ChangeFlag, List<MJoin>> compared = Ux.compare(original, new ArrayList<>(model.dbJoins()), uniqueSet);
            return Ux.future(compared);
        }).compose(compared -> Ux.compareRun(compared, jooqJoin::insertAsync, jooqJoin::updateAsync));
    }
}
