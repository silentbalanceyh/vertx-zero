package io.vertx.tp.plugin.mongo;

import io.vertx.core.Vertx;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.eon.Plugins;
import io.vertx.up.fn.Fn;
import io.vertx.up.plugin.Infix;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 */
@Plugin
@SuppressWarnings("unchecked")
public class MongoInfix implements Infix {

    private static final String NAME = "ZERO_MONGO_POOL";
    /**
     * All Configs
     **/
    private static final ConcurrentMap<String, MongoClient> CLIENTS
        = new ConcurrentHashMap<>();

    private static void initInternal(final Vertx vertx,
                                     final String name) {
        Fn.pool(CLIENTS, name,
            () -> Infix.init(Plugins.Infix.MONGO,
                (config) -> MongoClient.createShared(vertx, config, name),
                MongoInfix.class));
    }

    public static void init(final Vertx vertx) {
        initInternal(vertx, NAME);
    }

    public static MongoClient getClient() {
        return CLIENTS.get(NAME);
    }

    @Override
    public MongoClient get() {
        return getClient();
    }
}
