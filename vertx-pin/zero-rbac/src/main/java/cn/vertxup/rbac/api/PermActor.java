package cn.vertxup.rbac.api;

import cn.vertxup.rbac.domain.tables.daos.SPermissionDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@Queue
public class PermActor {
    /*
     * Calculate Permission Groups
     */
    @Address(Addr.Authority.PERMISSION_GROUP)
    public Future<JsonArray> calculate(final String sigma) {
        /*
         * Build condition of `sigma`
         */
        final JsonObject condition = new JsonObject();
        condition.put(KeField.SIGMA, sigma);
        /*
         * Permission Groups processing
         */
        return Ux.Jooq.on(SPermissionDao.class).groupAsync(condition, "group", "identifier");
    }
}
