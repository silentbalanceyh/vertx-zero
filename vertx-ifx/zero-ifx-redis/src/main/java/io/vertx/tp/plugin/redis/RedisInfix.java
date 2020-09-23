package io.vertx.tp.plugin.redis;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisOptions;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.eon.Plugins;
import io.vertx.up.fn.Fn;
import io.vertx.up.plugin.Infix;
import io.vertx.up.util.Ut;
import redis.clients.jedis.Jedis;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Plugin
@SuppressWarnings("unchecked")
public class RedisInfix implements Infix {

    private static final String NAME = "ZERO_REDIS_POOL";

    private static final ConcurrentMap<String, Redis> CLIENTS
            = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, Jedis> CLIENTS_SYNC
            = new ConcurrentHashMap<>();

    private static void initInternal(final Vertx vertx,
                                     final String name) {
        Fn.pool(CLIENTS, name, () -> Infix.init(Plugins.Infix.REDIS,
                /*
                 * Two parts for
                 * - Redis reference
                 * - RedisOptions reference ( For Sync usage )
                 */
                (config) -> Redis.createClient(vertx, new RedisOptions(initSync(name, config))),
                RedisInfix.class));
    }

    private static JsonObject initSync(final String name, final JsonObject options) {
        if (!CLIENTS_SYNC.containsKey(name)) {
            final String host = options.getString("host");
            final Integer port = options.getInteger("port");
            final Jedis client = new Jedis(host, port);
            // Auth
            final String password = options.getString("password");
            if (Ut.notNil(password)) {
                final String username = options.getString("username");
                if (Ut.isNil(username)) {
                    client.auth(password);
                } else {
                    client.auth(username, password);
                }
            }
            CLIENTS_SYNC.put(name, client);
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

    @Override
    public Redis get() {
        return getClient();
    }
}
