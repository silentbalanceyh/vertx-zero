package io.vertx.tp.plugin.history;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

public class TrashPlatformService implements TrashPlatform {

    private final transient Vertx vertxRef;
    private final transient JsonObject options = new JsonObject();

    public TrashPlatformService(final Vertx vertxRef, final JsonObject options) {
        this.vertxRef = vertxRef;
        if (Ut.notNil(options)) {
            this.options.mergeIn(options);
        }
    }

    @Override
    public TrashClient getClient(final String identifier) {
        return Fn.pool(Pool.CLIENT_POOL, identifier, () -> new TrashClientImpl(this.vertxRef, identifier));
    }
}
