package io.vertx.up.plugin.mongo;

import io.horizon.uca.cache.Cc;
import io.vertx.core.Vertx;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.up.annotations.Infusion;
import io.vertx.up.eon.configure.YmlCore;

/**
 *
 */
@Infusion
@SuppressWarnings("unchecked")
public class MongoInfix implements io.vertx.up.plugin.Infix {

    private static final String NAME = "ZERO_MONGO_POOL";
    /**
     * All Configs
     **/
    private static final Cc<String, MongoClient> CC_CLIENT = Cc.open();

    private static void initInternal(final Vertx vertx,
                                     final String name) {
        CC_CLIENT.pick(() -> io.vertx.up.plugin.Infix.init(YmlCore.inject.MONGO,
            (config) -> MongoClient.createShared(vertx, config, name),
            MongoInfix.class), name);
    }

    public static void init(final Vertx vertx) {
        initInternal(vertx, NAME);
    }

    public static MongoClient getClient() {
        return CC_CLIENT.store(NAME);
    }

    @Override
    public MongoClient get() {
        return getClient();
    }
}
