package io.vertx.tp.optic.business;

import cn.vertxup.rbac.domain.tables.daos.SUserDao;
import cn.vertxup.rbac.domain.tables.pojos.SUser;
import cn.vertxup.rbac.service.business.UserExtension;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.Refer;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class ExUserEpic implements ExUser {

    @Override
    public Future<JsonObject> fetchByReference(final JsonObject filters) {
        final JsonObject condition = this.dataReference(filters);
        condition.put(Strings.EMPTY, Boolean.TRUE);
        condition.put(KName.SIGMA, filters.getString(KName.SIGMA));
        return Ux.Jooq.on(SUserDao.class).fetchJOneAsync(condition);
    }

    @Override
    public Future<JsonObject> updateReference(final String key, final JsonObject params) {
        final JsonObject updatedData = this.dataReference(params);
        final UxJooq jq = Ux.Jooq.on(SUserDao.class);
        return jq.fetchJByIdAsync(key).compose(original -> {
            original.mergeIn(updatedData);
            final SUser user = Ut.deserialize(original, SUser.class);
            return jq.updateAsync(user).compose(Ux::futureJ);
        });
    }

    @Override
    public Future<JsonArray> fetchByReference(final Set<String> keys) {
        final JsonArray keyArray = Ut.toJArray(keys);
        return Ux.Jooq.on(SUserDao.class).fetchJInAsync(KName.MODEL_KEY, keyArray);
    }

    @Override
    public Future<ConcurrentMap<String, String>> mapAuditor(final Set<String> keys) {
        return this.fetchList(keys).compose(results -> {
            final ConcurrentMap<String, String> map = Ut.elementMap(results, SUser::getKey, SUser::getRealname);
            return Ux.future(map);
        });
    }

    @Override
    public Future<ConcurrentMap<String, JsonObject>> mapUser(final Set<String> keys, final boolean extension) {
        final Refer userRef = new Refer();
        return this.fetchList(keys)
            .compose(userRef::future)
            .compose(queried -> {
                if (extension) {
                    return UserExtension.fetchAsync(queried);
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
    /*
     * {
     *      "identifier": "modelId",
     *      "key": "modelKey"
     * }
     */
    private JsonObject dataReference(final JsonObject json) {
        final JsonObject conditionJ = new JsonObject();
        conditionJ.put(KName.MODEL_ID, json.getString(KName.IDENTIFIER));
        conditionJ.put(KName.MODEL_KEY, json.getString(KName.KEY));
        return conditionJ;
    }

    private ConcurrentMap<String, JsonObject> userMap(final ConcurrentMap<String, JsonObject> mapUser,
                                                      final JsonArray data) {
        final ConcurrentMap<String, JsonObject> mapData = Ut.elementMap(data, KName.KEY);
        final ConcurrentMap<String, JsonObject> response = new ConcurrentHashMap<>();
        mapUser.forEach((key, json) -> {
            final String modelKey = json.getString(KName.MODEL_KEY);
            /*
             * Fix Issue of `null` modelKey in workflow
             */
            if (Ut.notNil(modelKey)) {
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
