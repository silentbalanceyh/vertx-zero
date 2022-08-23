package io.vertx.tp.rbac.acl.relation;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.optic.environment.Connex;

public class ConnexNexus implements Connex<String> {
    /*
     * userJ -> User + Extension JsonObject
     * This method will extract `roles` & `groups` from system
     */
    @Override
    public Future<JsonObject> identAsync(final JsonObject userJ) {
        return null;
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
