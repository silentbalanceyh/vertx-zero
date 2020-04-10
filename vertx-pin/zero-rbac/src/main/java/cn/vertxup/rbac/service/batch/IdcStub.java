package cn.vertxup.rbac.service.batch;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._501NotSupportException;
import io.vertx.up.eon.Values;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;

/*
 * Uniform `SUser` created
 * 1. SUser
 * 2. OUser
 * 3. SRole
 */
public interface IdcStub {

    static IdcStub create(final String sigma) {
        /*
         * Each sigma has one reference of `IdcStub`
         */
        return Fn.pool(Pool.STUBS, sigma, () -> new IdcService(sigma));
    }

    /*
     * Save user information for
     * 1) Add
     * 2) Update
     */
    default Future<JsonArray> saveAsync(final JsonArray user, final String by) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    default Future<JsonObject> saveAsync(final JsonObject user, final String by) {
        final JsonArray users = new JsonArray();
        users.add(user);
        return this.saveAsync(users, by).compose(array -> Ux.future(array.getJsonObject(Values.IDX)));
    }
}
