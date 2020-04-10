package io.vertx.tp.optic.business;

import cn.vertxup.erp.domain.tables.daos.EEmployeeDao;
import cn.vertxup.erp.domain.tables.pojos.EEmployee;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

/*
 * Get user information from database
 * Account + Employee
 */
public class ExEmployeeEpic implements ExEmployee {
    @Override
    public Future<JsonObject> fetchAsync(final String id) {
        return Ux.Jooq.on(EEmployeeDao.class)
                .findByIdAsync(id)
                .compose(Ux::fnJObject);
    }

    @Override
    public Future<JsonObject> updateAsync(final String id, final JsonObject params) {
        final EEmployee employee = Ux.fromJson(params, EEmployee.class);
        return Ux.Jooq.on(EEmployeeDao.class)
                .saveAsync(id, employee)
                .compose(Ux::fnJObject);
    }
}
