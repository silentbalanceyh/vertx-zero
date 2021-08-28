package io.vertx.tp.plugin.neo4j;

import io.vertx.core.Vertx;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.fn.Fn;
import io.vertx.up.plugin.Infix;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Plugin
@SuppressWarnings("all")
public class Neo4jInfix implements Infix {

    private static final String NAME = "ZERO_NEO4J_POOL";

    private static final ConcurrentMap<String, Neo4jClient> CLIENTS
        = new ConcurrentHashMap<>();

    private static void initInternal(final Vertx vertx, final String name) {
        Fn.pool(CLIENTS, name, () -> Infix.init("neo4j",
            config -> Neo4jClient.createShared(vertx, config),
            Neo4jInfix.class));
    }

    public static void init(final Vertx vertx) {
        initInternal(vertx, NAME);
    }

    public static Neo4jClient getClient() {
        return CLIENTS.get(NAME);
    }

    @Override
    public Neo4jClient get() {
        return getClient();
    }

}
