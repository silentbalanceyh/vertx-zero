package io.vertx.up.uca.job.plugin;

import io.horizon.uca.cache.Cc;
import io.vertx.core.Vertx;
import io.vertx.up.annotations.Infusion;
import io.vertx.up.plugin.session.SessionInfix;

@Infusion
@SuppressWarnings("unchecked")
public class JobInfix implements io.vertx.up.plugin.Infix {

    private static final String NAME = "ZERO_JOB_POOL";
    private static final Cc<String, JobClient> CC_CLIENTS = Cc.open();

    private static void initInternal(final Vertx vertx,
                                     final String name) {
        CC_CLIENTS.pick(() -> io.vertx.up.plugin.Infix.init("job",
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
