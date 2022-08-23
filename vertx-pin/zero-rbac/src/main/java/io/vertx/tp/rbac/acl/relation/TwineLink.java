package io.vertx.tp.rbac.acl.relation;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.secure.Twine;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

public class TwineLink implements Twine<String> {
    /*
     * userJ -> User + Extension JsonObject
     * This method will extract `roles` & `groups` from system
     */
    @Override
    public Future<JsonObject> identAsync(final JsonObject userJ) {
        final String key = Ut.valueString(userJ, KName.KEY);
        return Junc.role().identAsync(key).compose(roles -> {
            userJ.put(KName.ROLE, Ut.encryptBase64(roles.encodePrettily()));
            return Ux.future();
        }).compose(nil -> Junc.group().identAsync(key)).compose(groups -> {
            userJ.put(KName.GROUP, Ut.encryptBase64(groups.encodePrettily()));
            return Ux.future(userJ);
        });
    }

    @Override
    public Future<JsonObject> identAsync(final String key) {
        return null;
    }

    @Override
    public Future<JsonObject> identAsync(final String key, final JsonObject updatedData) {
        return null;
    }
}
