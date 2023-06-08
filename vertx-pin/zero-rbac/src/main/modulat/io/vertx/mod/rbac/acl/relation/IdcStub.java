package io.vertx.mod.rbac.acl.relation;

import io.horizon.eon.VValue;
import io.horizon.exception.web._501NotSupportException;
import io.horizon.uca.cache.Cc;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

/*
 * Uniform `SUser` created
 * 1. SUser
 * 2. OUser
 * 3. SRole
 */
public interface IdcStub {

    Cc<String, IdcStub> CC_STUB = Cc.open();

    static IdcStub create(final String sigma) {
        /*
         * Each sigma has one reference of `IdcStub`
         */
        return CC_STUB.pick(() -> new IdcService(sigma), sigma);
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
        return this.saveAsync(users, by).compose(array -> Ux.future(array.getJsonObject(VValue.IDX)));
    }
}
