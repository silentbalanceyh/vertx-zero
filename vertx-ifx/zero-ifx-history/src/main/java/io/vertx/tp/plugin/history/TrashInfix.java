package io.vertx.tp.plugin.history;

import io.vertx.core.Vertx;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.fn.Fn;
import io.vertx.up.plugin.Infix;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Plugin
@SuppressWarnings("all")
public class TrashInfix implements Infix {

    private static final String NAME = "ZERO_TRASH_POOL";

    private static final ConcurrentMap<String, TrashPlatform> CLIENTS
        = new ConcurrentHashMap<>();

    private static void initInternal(final Vertx vertx,
                                     final String name) {
        Fn.pool(CLIENTS, name,
            () -> Infix.init("trash",
                (config) -> TrashPlatform.createShared(vertx, config),
                TrashPlatform.class));
    }

    public static void init(final Vertx vertx) {
        initInternal(vertx, NAME);
    }

    public static TrashPlatform getClient() {
        return CLIENTS.get(NAME);
    }

    @Override
    public TrashPlatform get() {
        return getClient();
    }
}
