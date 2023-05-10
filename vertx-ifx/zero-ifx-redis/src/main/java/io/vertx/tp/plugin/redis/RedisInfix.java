package io.vertx.tp.plugin.redis;

import io.horizon.uca.log.Annal;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisOptions;
import io.vertx.tp.plugin.cache.Harp;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.plugin.Infix;
import io.vertx.up.runtime.ZeroYml;
import io.vertx.up.util.Ut;
import redis.clients.jedis.Jedis;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Plugin
@SuppressWarnings("unchecked")
public class RedisInfix implements Infix {

    private static final String NAME = "ZERO_REDIS_POOL";
    private static final Annal LOGGER = Annal.get(RedisInfix.class);

    private static final ConcurrentMap<String, Redis> CLIENTS
        = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, Jedis> CLIENTS_SYNC
        = new ConcurrentHashMap<>();

    private static void initInternal(final Vertx vertx,
                                     final String name) {
        final RedisOptions options = Infix.init(ZeroYml.inject.redis,
            /*
             * Two parts for
             * - Redis reference
             * - RedisOptions reference ( For Sync usage )
             */
            (config) -> new RedisOptions(initSync(name, config)),
            RedisInfix.class);
        /*
         * Redis client processing, ping to check whether it's Ok
         */
        final Redis redis = Redis.createClient(vertx, options);
        CLIENTS.put(name, redis);
        redis.connect(handler -> {
            /*
             * If connected, keep
             * If not connected, remove
             * This kind of operation could let your system synced the Redis
             * instead of Null Pointer in Unit Testing
             */
            if (handler.succeeded()) {
                LOGGER.info("[ Redis ] Connected successfully! {0}", options.toJson().encode());
                /*
                 * Harp Component for cache system initialized
                 * The cache system support L1, L2, L3 level for database accessing
                 * You can select different cache implementation component such as Memory, Redis etc.
                 * Zero system support redis in standard mode
                 */
                Harp.init(vertx);
            } else {
                final Throwable ex = handler.cause();
                if (Objects.nonNull(ex)) {
                    LOGGER.fatal(ex);
                }
                CLIENTS.remove(name);
            }
        });
    }

    private static JsonObject initSync(final String name, final JsonObject options) {
        if (!CLIENTS_SYNC.containsKey(name)) {
            final String host = options.getString("host");
            final Integer port = options.getInteger("port");
            // Fix New Version Issue
            if (!options.containsKey("endpoint")) {
                options.put("endpoint", "redis://" + host + ":" + port);
            }
            final Jedis client = new Jedis(host, port);
            // Auth
            final String password = options.getString("password");
            if (Ut.isNotNil(password)) {
                final String username = options.getString("username");
                if (Ut.isNil(username)) {
                    client.auth(password);
                } else {
                    client.auth(username, password);
                }
            }
            try {
                client.ping();
                CLIENTS_SYNC.put(name, client);
            } catch (final Throwable ex) {
                LOGGER.fatal(ex);
            }
        }
        return options;
    }

    public static void init(final Vertx vertx) {
        initInternal(vertx, NAME);
    }

    public static Redis getClient() {
        return CLIENTS.get(NAME);
    }

    public static Jedis getJClient() {
        return CLIENTS_SYNC.get(NAME);
    }

    public static void disabled() {
        CLIENTS.clear();
        CLIENTS_SYNC.clear();
    }

    @Override
    public Redis get() {
        return getClient();
    }
}
