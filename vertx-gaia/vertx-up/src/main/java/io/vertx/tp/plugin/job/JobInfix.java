package io.vertx.tp.plugin.job;

import io.vertx.core.Vertx;
import io.vertx.tp.plugin.session.SessionInfix;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.plugin.Infix;
import io.vertx.up.uca.cache.Cc;

@Plugin
@SuppressWarnings("unchecked")
public class JobInfix implements Infix {

    private static final String NAME = "ZERO_JOB_POOL";
    private static final Cc<String, JobClient> CC_CLIENTS = Cc.open();

    private static void initInternal(final Vertx vertx,
                                     final String name) {
        CC_CLIENTS.pick(() -> Infix.init("job",
            (config) -> JobClient.createShared(vertx, config.getJsonObject("client")),
            SessionInfix.class
        ), name);
    }

    public static void init(final Vertx vertx) {
        initInternal(vertx, NAME);
    }

    public static JobClient getClient() {
        return CC_CLIENTS.store(NAME);
    }

    @Override
    public JobClient get() {
        return getClient();
    }
}
