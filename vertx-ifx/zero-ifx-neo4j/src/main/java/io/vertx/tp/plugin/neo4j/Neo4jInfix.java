package io.vertx.tp.plugin.neo4j;

import io.vertx.core.Vertx;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.plugin.Infix;
import io.vertx.up.uca.cache.Cc;

@Plugin
@SuppressWarnings("all")
public class Neo4jInfix implements Infix {

    private static final String NAME = "ZERO_NEO4J_POOL";
    private static final Cc<String, Neo4jClient> CC_CLIENT = Cc.open();

    private static void initInternal(final Vertx vertx, final String name) {
        CC_CLIENT.pick(() -> Infix.init("neo4j",
            config -> Neo4jClient.createShared(vertx, config),
            Neo4jInfix.class), name);
    }

    public static void init(final Vertx vertx) {
        initInternal(vertx, NAME);
    }

    public static Neo4jClient getClient() {
        return CC_CLIENT.store(NAME);
    }

    @Override
    public Neo4jClient get() {
        return getClient();
    }

}
