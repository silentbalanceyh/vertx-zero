package cn.vertxup.lbs.service;

import cn.vertxup.lbs.domain.tables.daos.LLocationDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

public class LocationService implements LocationStub {

    @Override
    public Future<JsonObject> fetchAsync(final String locationId) {
        return Ux.Jooq.on(LLocationDao.class)
            .fetchByIdAsync(locationId)
            .compose(Ux::futureJ);
    }
}
