package io.vertx.tp.plugin.elasticsearch;

import io.horizon.uca.cache.Cc;
import io.vertx.core.Vertx;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.plugin.Infix;

/**
 * @author Hongwei
 * @since 2019/12/28, 16:09
 */

@Plugin
public class ElasticSearchInfix implements Infix {
    private static final String NAME = "ZERO_ELASTIC_SEARCH_POOL";

    private static final Cc<String, ElasticSearchClient> CC_CLIENT = Cc.open();

    private static void initInternal(final Vertx vertx) {
        CC_CLIENT.pick(() -> Infix.init("elasticsearch",
            (config) -> ElasticSearchClient.createShared(vertx, config),
            ElasticSearchInfix.class), NAME);
    }

    public static void init(final Vertx vertx) {
        initInternal(vertx);
    }

    public static ElasticSearchClient getClient() {
        return CC_CLIENT.store(NAME);
    }

    @Override
    @SuppressWarnings("all")
    public ElasticSearchClient get() {
        return getClient();
    }
}
