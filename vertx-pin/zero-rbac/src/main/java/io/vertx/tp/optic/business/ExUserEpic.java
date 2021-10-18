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

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class ExUserEpic implements ExUser {
    @Override
    public Future<JsonObject> fetchRef(final JsonObject filters) {
        return Nexus.create(SUser.class)
            .on(Ux.Jooq.on(SUserDao.class))
            .fetchNexus(filters);
    }

    @Override
    public Future<JsonObject> updateRef(final String key, final JsonObject params) {
        return Nexus.create(SUser.class)
            .on(Ux.Jooq.on(SUserDao.class))
            .updateNexus(key, params);
    }

    @Override
    public Future<JsonArray> fetchRef(final Set<String> keys) {
        return Nexus.create(SUser.class)
            .on(Ux.Jooq.on(SUserDao.class))
            .fetchNexus(keys);
    }

    @Override
    public Future<ConcurrentMap<String, String>> transAuditor(final Set<String> keys) {
        final JsonObject condition = new JsonObject();
        condition.put(KName.KEY + ",i", Ut.toJArray(keys));
        return Ux.Jooq.on(SUserDao.class).<SUser>fetchAsync(condition).compose(results -> {
            final ConcurrentMap<String, String> map = Ut.elementMap(results, SUser::getKey, SUser::getRealname);
            return Ux.future(map);
        });
    }
}
