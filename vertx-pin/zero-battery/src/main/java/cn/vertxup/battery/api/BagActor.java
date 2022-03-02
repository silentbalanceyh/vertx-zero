package cn.vertxup.battery.api;

import cn.vertxup.battery.domain.tables.daos.BBagDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.battery.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class BagActor {

    @Address(Addr.Module.FETCH)
    public Future<JsonArray> fetchModule(final String appId) {
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.APP_ID, appId);
        return Ux.Jooq.on(BBagDao.class).fetchJAsync(condition);
    }

    @Address(Addr.Module.UP_PROCESS)
    public Future<Boolean> process(final JsonObject params) {
        return Ux.futureT();
    }

    @Address(Addr.Module.UP_AUTHORIZE)
    public Future<Boolean> authorize(final JsonObject params) {
        return Ux.futureT();
    }
}
