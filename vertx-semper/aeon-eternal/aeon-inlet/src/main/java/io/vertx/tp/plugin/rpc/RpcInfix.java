package io.vertx.tp.plugin.rpc;

import io.vertx.core.Vertx;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.eon.KPlugin;
import io.vertx.up.plugin.Infix;
import io.vertx.up.uca.cache.Cc;

/**
 * Rpc Client for specific
 */
@Plugin
@SuppressWarnings("unchecked")
public class RpcInfix implements Infix {

    private static final String NAME = "ZERO_RPC_POOL";

    private static final Cc<String, RpcClient> CC_CLIENT = Cc.open();


    private static void initInternal(final Vertx vertx,
                                     final String name) {
        CC_CLIENT.pick(() -> Infix.init(KPlugin.Infix.RPC,
            (config) -> RpcClient.createShared(vertx, config, name),
            RpcInfix.class), name);
    }

    public static void init(final Vertx vertx) {
        initInternal(vertx, NAME);
    }

    public static RpcClient getClient() {
        return CC_CLIENT.store(NAME);
    }

    @Override
    public RpcClient get() {
        return getClient();
    }
}
