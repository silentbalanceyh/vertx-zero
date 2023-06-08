package io.vertx.up.plugin.elasticsearch;

import io.horizon.uca.cache.Cc;
import io.vertx.core.Vertx;
import io.vertx.up.annotations.Infusion;
import io.vertx.up.eon.configure.YmlCore;

/**
 * @author Hongwei
 * @since 2019/12/28, 16:09
 */

@Infusion
public class ElasticSearchInfix implements io.vertx.up.plugin.Infix {
    private static final String NAME = "ZERO_ELASTIC_SEARCH_POOL";

    private static final Cc<String, ElasticSearchClient> CC_CLIENT = Cc.open();

    private static void initInternal(final Vertx vertx) {
        CC_CLIENT.pick(() -> io.vertx.up.plugin.Infix.init(YmlCore.inject.ES,
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
