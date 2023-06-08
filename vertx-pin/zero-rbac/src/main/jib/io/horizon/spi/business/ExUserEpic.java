package io.horizon.spi.business;

import cn.vertxup.rbac.domain.tables.daos.SUserDao;
import cn.vertxup.rbac.domain.tables.pojos.SUser;
import io.horizon.atom.common.Refer;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.acl.relation.Junc;
import io.vertx.mod.rbac.cv.AuthKey;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class ExUserEpic implements ExUser {


    // ------------------ Model Id / Model Key --------------------
    @Override
    public Future<JsonObject> rapport(final JsonObject condition) {
        return Junc.refModel().identAsync(condition);
    }

    @Override
    public Future<JsonObject> rapport(final String key, final JsonObject params) {
        return Junc.refModel().identAsync(key, params);
    }

    @Override
    public Future<JsonArray> rapport(final Set<String> keys) {
        return Junc.refModel().identAsync(keys);
    }

    @Override
    public Future<ConcurrentMap<String, String>> mapAuditor(final Set<String> keys) {
        return this.fetchList(keys).compose(results -> {
            final ConcurrentMap<String, String> map = Ut.elementMap(results, SUser::getKey, SUser::getRealname);
            return Ux.future(map);
        });
    }

    @Override
    public Future<JsonArray> userGroup(final String key) {
        return Junc.group().identAsync(key).compose(relations -> {
            final JsonArray groupKeys = new JsonArray();
            Ut.itJArray(relations).forEach(item -> groupKeys.add(item.getValue(AuthKey.F_GROUP_ID)));
            return Ux.future(groupKeys);
        });
    }

    @Override
    public Future<ConcurrentMap<String, JsonObject>> mapUser(final Set<String> keys, final boolean extension) {
        final Refer userRef = new Refer();
        return this.fetchList(keys)
            .compose(userRef::future)
            .compose(queried -> {
                if (extension) {
                    return Junc.refExtension().identAsync(queried);
                } else {
                    return Ux.futureA();
                }
            })
            .compose(employeeA -> {
                final List<SUser> users = userRef.get();
                final JsonArray userA = Ux.toJson(users);
                final ConcurrentMap<String, JsonObject> mapUser = Ut.elementMap(userA, KName.KEY);
                return Ux.future(this.userMap(mapUser, employeeA));
            });
    }

    @Override
    public Future<JsonArray> searchUser(final String keyword) {
        final JsonObject condition = new JsonObject();
        condition.put(KName.REAL_NAME + ",c", keyword);
        return Ux.Jooq.on(SUserDao.class).<SUser>fetchAsync(condition).compose(users -> {
            final List<String> keys = users.stream().map(SUser::getKey).collect(Collectors.toList());
            return Ux.future(Ut.toJArray(keys));
        });
    }

    // ====================== User Map ===========================

    private ConcurrentMap<String, JsonObject> userMap(final ConcurrentMap<String, JsonObject> mapUser,
                                                      final JsonArray data) {
        final ConcurrentMap<String, JsonObject> mapData = Ut.elementMap(data, KName.KEY);
        final ConcurrentMap<String, JsonObject> response = new ConcurrentHashMap<>();
        mapUser.forEach((key, json) -> {
            final String modelKey = json.getString(KName.MODEL_KEY);
            /*
             * Fix Issue of `null` modelKey in workflow
             */
            if (Ut.isNotNil(modelKey)) {
                if (mapData.containsKey(modelKey)) {
                    JsonObject objRef = mapData.get(modelKey);
                    objRef = objRef.copy();
                    objRef.mergeIn(json, true);
                    response.put(key, objRef);
                } else {
                    response.put(key, json.copy());
                }
            }
        });
        return response;
    }

    private Future<List<SUser>> fetchList(final Set<String> keys) {
        final JsonObject condition = new JsonObject();
        condition.put(KName.KEY + ",i", Ut.toJArray(keys));
        return Ux.Jooq.on(SUserDao.class).fetchAsync(condition);
    }

}
