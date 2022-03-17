package io.vertx.tp.optic.business;

import cn.vertxup.rbac.domain.tables.daos.SUserDao;
import cn.vertxup.rbac.domain.tables.pojos.SUser;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.tunnel.Nexus;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class ExUserEpic implements ExUser {
    @Override
    public Future<JsonObject> fetch(final JsonObject filters) {
        return Nexus.create(SUser.class)
            .on(Ux.Jooq.on(SUserDao.class))
            .fetchNexus(filters);
    }

    @Override
    public Future<JsonObject> update(final String key, final JsonObject params) {
        return Nexus.create(SUser.class)
            .on(Ux.Jooq.on(SUserDao.class))
            .updateNexus(key, params);
    }

    @Override
    public Future<JsonArray> fetch(final Set<String> keys) {
        return Nexus.create(SUser.class)
            .on(Ux.Jooq.on(SUserDao.class))
            .fetchNexus(keys);
    }

    @Override
    public Future<ConcurrentMap<String, String>> auditor(final Set<String> keys) {
        final JsonObject condition = new JsonObject();
        condition.put(KName.KEY + ",i", Ut.toJArray(keys));
        return Ux.Jooq.on(SUserDao.class).<SUser>fetchAsync(condition).compose(results -> {
            final ConcurrentMap<String, String> map = Ut.elementMap(results, SUser::getKey, SUser::getRealname);
            return Ux.future(map);
        });
    }

    @Override
    public Future<JsonArray> auditor(final String keyword) {
        final JsonObject condition = new JsonObject();
        condition.put(KName.REAL_NAME + ",c", keyword);
        return Ux.Jooq.on(SUserDao.class).<SUser>fetchAsync(condition).compose(users -> {
            final List<String> keys = users.stream().map(SUser::getKey).collect(Collectors.toList());
            return Ux.futureA(keys);
        });
    }
}
