package io.vertx.tp.plugin.history;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.horizon.uca.cache.Cc;
import io.vertx.up.util.Ut;

public class TrashPlatformService implements TrashPlatform {

    private static final Cc<String, TrashClient> CC_CLIENT = Cc.open();

    private final transient Vertx vertxRef;
    private final transient JsonObject options = new JsonObject();

    public TrashPlatformService(final Vertx vertxRef, final JsonObject options) {
        this.vertxRef = vertxRef;
        if (Ut.isNotNil(options)) {
            this.options.mergeIn(options);
        }
    }

    @Override
    public TrashClient getClient(final String identifier) {
        return CC_CLIENT.pick(() -> new TrashClientImpl(this.vertxRef, identifier), identifier);
        // Fn.po?l(Pool.CLIENT_POOL, identifier, () -> new TrashClientImpl(this.vertxRef, identifier));
    }
}
