package io.vertx.tp.optic.business;

import cn.vertxup.erp.domain.tables.daos.EEmployeeDao;
import cn.vertxup.erp.domain.tables.pojos.EEmployee;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Set;

/*
 * Get user information from database
 * Account + Employee
 */
public class ExEmployeeEpic implements ExEmployee {
    @Override
    public Future<JsonObject> fetchAsync(final String id) {
        return Ux.Jooq.on(EEmployeeDao.class)
            .fetchByIdAsync(id)
            .compose(Ux::futureJ);
    }

    @Override
    public Future<JsonArray> fetchAsync(final Set<String> ids) {
        if (ids.isEmpty()) {
            return Ux.futureA();
        } else {
            final JsonObject condition = new JsonObject();
            condition.put(KName.KEY + ",i", Ut.toJArray(ids));
            return Ux.Jooq.on(EEmployeeDao.class).fetchJAsync(condition);
        }
    }

    @Override
    public Future<JsonObject> updateAsync(final String id, final JsonObject params) {
        final EEmployee employee = Ux.fromJson(params, EEmployee.class);
        return Ux.Jooq.on(EEmployeeDao.class)
            .updateAsync(id, employee)
            .compose(Ux::futureJ);
    }
}
