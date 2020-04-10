package io.vertx.tp.plugin.redis;

import io.vertx.core.Vertx;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.eon.Plugins;
import io.vertx.up.fn.Fn;
import io.vertx.up.plugin.Infix;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Plugin
@SuppressWarnings("unchecked")
public class RedisInfix implements Infix {

    private static final String NAME = "ZERO_REDIS_POOL";

    private static final ConcurrentMap<String, RedisClient> CLIENTS
            = new ConcurrentHashMap<>();

    private static void initInternal(final Vertx vertx,
                                     final String name) {
        Fn.pool(CLIENTS, name,
                () -> Infix.init(Plugins.Infix.REDIS,
                        (config) -> RedisClient.create(vertx, new RedisOptions(config)),
                        RedisInfix.class));
    }

    public static void init(final Vertx vertx) {
        initInternal(vertx, NAME);
    }

    public static RedisClient getClient() {
        return CLIENTS.get(NAME);
    }

    @Override
    public RedisClient get() {
        return getClient();
    }
}
