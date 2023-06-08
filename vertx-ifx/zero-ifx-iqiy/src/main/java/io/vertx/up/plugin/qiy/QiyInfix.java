package io.vertx.up.plugin.qiy;

import io.horizon.uca.cache.Cc;
import io.vertx.core.Vertx;
import io.vertx.up.annotations.Infusion;

@Infusion
@SuppressWarnings("all")
public class QiyInfix implements io.vertx.up.plugin.Infix {
    private static final String NAME = "ZERO_QIY_POOL";

    private static final Cc<String, QiyClient> CC_CLIENT = Cc.open();

    private static void initInternal(final Vertx vertx,
                                     final String name) {
        CC_CLIENT.pick(() -> io.vertx.up.plugin.Infix.init("qiy",
            (config) -> QiyClient.createShared(vertx, config),
            QiyInfix.class), name);
    }

    public static void init(final Vertx vertx) {
        initInternal(vertx, NAME);
    }

    public static QiyClient getClient() {
        return CC_CLIENT.store(NAME);
    }

    @Override
    public QiyClient get() {
        return getClient();
    }
}
