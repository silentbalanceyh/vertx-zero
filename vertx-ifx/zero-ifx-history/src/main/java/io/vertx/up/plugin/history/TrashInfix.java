package io.vertx.up.plugin.history;

import io.horizon.uca.cache.Cc;
import io.vertx.core.Vertx;
import io.vertx.up.annotations.Infusion;
import io.vertx.up.eon.configure.YmlCore;

@Infusion
@SuppressWarnings("all")
public class TrashInfix implements io.vertx.up.plugin.Infix {

    private static final String NAME = "ZERO_TRASH_POOL";
    private static final Cc<String, TrashPlatform> CC_CLIENT = Cc.open();

    private static void initInternal(final Vertx vertx,
                                     final String name) {
        CC_CLIENT.pick(() -> io.vertx.up.plugin.Infix.init(YmlCore.inject.TRASH,
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
