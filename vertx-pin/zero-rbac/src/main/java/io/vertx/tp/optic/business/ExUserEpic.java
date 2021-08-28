package io.vertx.tp.optic.business;

import cn.vertxup.rbac.domain.tables.daos.SUserDao;
import cn.vertxup.rbac.domain.tables.pojos.SUser;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.tunnel.Nexus;
import io.vertx.up.unity.Ux;

import java.util.Set;

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
}
