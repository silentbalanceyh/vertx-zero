package io.vertx.tp.plugin.history;

import io.vertx.core.Vertx;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.plugin.Infix;
import io.horizon.uca.cache.Cc;

@Plugin
@SuppressWarnings("all")
public class TrashInfix implements Infix {

    private static final String NAME = "ZERO_TRASH_POOL";
    private static final Cc<String, TrashPlatform> CC_CLIENT = Cc.open();

    private static void initInternal(final Vertx vertx,
                                     final String name) {
        CC_CLIENT.pick(() -> Infix.init("trash",
            (config) -> TrashPlatform.createShared(vertx, config),
            TrashPlatform.class), name);
    }

    public static void init(final Vertx vertx) {
        initInternal(vertx, NAME);
    }

    public static TrashPlatform getClient() {
        return CC_CLIENT.store(NAME);
    }

    @Override
    public TrashPlatform get() {
        return getClient();
    }
}
