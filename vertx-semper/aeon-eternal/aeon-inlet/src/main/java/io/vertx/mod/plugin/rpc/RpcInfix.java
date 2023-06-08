package io.vertx.mod.plugin.rpc;

import io.horizon.uca.cache.Cc;
import io.vertx.core.Vertx;
import io.vertx.up.annotations.Infusion;
import io.vertx.up.eon.configure.YmlCore;

/**
 * Rpc Client for specific
 */
@Infusion
@SuppressWarnings("unchecked")
public class RpcInfix implements io.vertx.up.plugin.Infix {

    private static final String NAME = "ZERO_RPC_POOL";

    private static final Cc<String, RpcClient> CC_CLIENT = Cc.open();


    private static void initInternal(final Vertx vertx,
                                     final String name) {
        CC_CLIENT.pick(() -> io.vertx.up.plugin.Infix.init(YmlCore.inject.RPC,
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
