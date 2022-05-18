package io.vertx.tp.optic.business;

import cn.vertxup.rbac.domain.tables.daos.SUserDao;
import cn.vertxup.rbac.domain.tables.pojos.SUser;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.Refer;
import io.vertx.up.eon.KName;
import io.vertx.up.experiment.reference.Ref;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class ExUserEpic implements ExUser {
    @Override
    public Future<JsonObject> fetch(final JsonObject filters) {
        return Ref.modeling(SUser.class)
            .on(Ux.Jooq.on(SUserDao.class))
            .fetchJ(filters);
    }

    @Override
    public Future<JsonObject> update(final String key, final JsonObject params) {
        return Ref.modeling(SUser.class)
            .on(Ux.Jooq.on(SUserDao.class))
            .updateJ(key, params);
    }

    @Override
    public Future<JsonArray> fetch(final Set<String> keys) {
        return Ref.modeling(SUser.class)
            .on(Ux.Jooq.on(SUserDao.class))
            .fetchA(keys);
    }

    @Override
    public Future<ConcurrentMap<String, String>> auditor(final Set<String> keys) {
        return this.fetchList(keys).compose(results -> {
            final ConcurrentMap<String, String> map = Ut.elementMap(results, SUser::getKey, SUser::getRealname);
            return Ux.future(map);
        });
    }

    @Override
    public Future<ConcurrentMap<String, JsonObject>> user(final Set<String> keys, final boolean employee) {
        final Refer userRef = new Refer();
        return this.fetchList(keys)
            .compose(userRef::future)
            .compose(queried -> {
                if (employee) {
                    return this.userEmp(queried);
                } else {
                    return Ux.futureA();
                }
            })
            .compose(employeeA -> {
                /*
                 * Grouped List User by modelKey
                 * Employee + SUser returned
                 */
                final List<SUser> users = userRef.get();
                final JsonArray userA = Ux.toJson(users);
                final ConcurrentMap<String, JsonObject> mapUser = Ut.elementMap(userA, KName.KEY);
                return Ux.future(this.userMap(mapUser, employeeA));
            });
    }

    @Override
    public Future<JsonArray> search(final String keyword) {
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

    private Future<JsonArray> userEmp(final List<SUser> users) {
        final Set<String> modelKeys = users.stream()
            .filter(user -> "employee".equals(user.getModelId()))
            .map(SUser::getModelKey)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        return Ux.channel(ExEmployee.class, JsonArray::new, stub -> stub.fetchAsync(modelKeys));
    }

    private Future<List<SUser>> fetchList(final Set<String> keys) {
        final JsonObject condition = new JsonObject();
        condition.put(KName.KEY + ",i", Ut.toJArray(keys));
        return Ux.Jooq.on(SUserDao.class).fetchAsync(condition);
    }

}
