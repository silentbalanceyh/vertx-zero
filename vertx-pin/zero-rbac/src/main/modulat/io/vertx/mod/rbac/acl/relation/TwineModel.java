package io.vertx.mod.rbac.acl.relation;

import cn.vertxup.rbac.domain.tables.daos.SUserDao;
import cn.vertxup.rbac.domain.tables.pojos.SUser;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ke.secure.Twine;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Collection;

/**
 * modelId / modelKey 连接
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class TwineModel implements Twine<String> {
    /*
     * 参数转换成查询条件
     * {
     *     "identifier": "modelId",
     *     "key":        "modelKey",
     *     "sigma":      "sigma"
     * }
     */
    @Override
    public Future<JsonObject> identAsync(final JsonObject condition) {
        final JsonObject conditionJ = this.mappedJ(condition);
        // sigma 在查询过程中需追加
        Ut.valueCopy(conditionJ, condition,
            KName.SIGMA
        );
        return Ux.Jooq.on(SUserDao.class).fetchJOneAsync(conditionJ);
    }

    @Override
    public Future<JsonObject> identAsync(final String key) {
        return Ux.Jooq.on(SUserDao.class).fetchJByIdAsync(key);
    }

    @Override
    public Future<JsonObject> identAsync(final String key, final JsonObject updatedData) {
        return this.identAsync(key).compose(original -> {
            if (Ut.isNotNil(original)) {
                /*
                 * 只更新引用，不更新数据，主要更新 S_USER 中的两个核心字段
                 * modelId, modelKey
                 */
                final JsonObject updatedJ = this.mappedJ(updatedData);
                original.mergeIn(updatedJ);
                final SUser user = Ux.fromJson(original, SUser.class);
                return Ux.Jooq.on(SUserDao.class).updateAsync(user)
                    .compose(Ux::futureJ);
            } else {
                return Ux.futureJ();
            }
        });
    }

    @Override
    public Future<JsonArray> identAsync(final Collection<String> keys) {
        final JsonArray keysA = Ut.toJArray(keys);
        return Ux.Jooq.on(SUserDao.class).fetchJInAsync(KName.MODEL_KEY, keysA);
    }

    /*
     * {
     *     "identifier": "modelId",
     *     "key":        "modelKey"
     * }
     */
    private JsonObject mappedJ(final JsonObject input) {
        final JsonObject inputJ = Ut.valueJObject(input);
        final JsonObject dataJ = new JsonObject();
        dataJ.put(KName.MODEL_ID, inputJ.getValue(KName.IDENTIFIER));
        dataJ.put(KName.MODEL_KEY, inputJ.getValue(KName.KEY));
        return dataJ;
    }
}
