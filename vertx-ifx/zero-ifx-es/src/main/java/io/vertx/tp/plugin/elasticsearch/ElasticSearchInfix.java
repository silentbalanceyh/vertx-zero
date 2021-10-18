package io.vertx.tp.plugin.elasticsearch;

import io.vertx.core.Vertx;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.fn.Fn;
import io.vertx.up.plugin.Infix;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Hongwei
 * @since 2019/12/28, 16:09
 */

@Plugin
public class ElasticSearchInfix implements Infix {
    private static final String NAME = "ZERO_ELASTIC_SEARCH_POOL";

    private static final ConcurrentMap<String, ElasticSearchClient> CLIENTS = new ConcurrentHashMap<>();

    private static void initInternal(final Vertx vertx) {
        Fn.pool(CLIENTS, NAME,
            () -> Infix.init("elasticsearch",
                (config) -> ElasticSearchClient.createShared(vertx, config),
                ElasticSearchInfix.class));
    }

    public static void init(final Vertx vertx) {
        initInternal(vertx);
    }

    public static ElasticSearchClient getClient() {
        return CLIENTS.get(NAME);
    }

    @Override
    @SuppressWarnings("all")
    public ElasticSearchClient get() {
        return getClient();
    }
}
