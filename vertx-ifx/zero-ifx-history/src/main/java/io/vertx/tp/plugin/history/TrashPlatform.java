package io.vertx.tp.plugin.history;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/*
 * Trash Service for
 * 1) Removing
 * 2) Deleting
 */
public interface TrashPlatform {

    static TrashPlatform createShared(final Vertx vertx, final JsonObject options) {
        return new TrashPlatformService(vertx, options);
    }

    TrashClient getClient(String identifier);
}
