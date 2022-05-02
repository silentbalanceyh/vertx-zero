package io.vertx.tp.optic.business;

import cn.vertxup.rbac.domain.tables.daos.SUserDao;
import cn.vertxup.rbac.domain.tables.pojos.SUser;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.experiment.reference.Ref;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    public Future<ConcurrentMap<String, JsonObject>> auditor(final ConcurrentMap<String, String> keyMap) {
        final Set<String> keys = new HashSet<>(keyMap.values());
        return this.fetchList(keys).compose(Ux::futureA).compose(resultA -> {
            final ConcurrentMap<String, JsonObject> mapped = Ut.elementMap(resultA, KName.KEY);
            final ConcurrentMap<String, JsonObject> result = Ut.elementZip(keyMap, mapped);
            return Ux.future(result);
        });
    }

    private Future<List<SUser>> fetchList(final Set<String> keys) {
        final JsonObject condition = new JsonObject();
        condition.put(KName.KEY + ",i", Ut.toJArray(keys));
        return Ux.Jooq.on(SUserDao.class).fetchAsync(condition);
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
}
