package io.vertx.up.plugin.session;

import io.horizon.uca.cache.Cc;
import io.vertx.core.Vertx;
import io.vertx.up.annotations.Infusion;
import io.vertx.up.eon.configure.YmlCore;

import java.util.Objects;

@Infusion
@SuppressWarnings("unchecked")
public class SessionInfix implements io.vertx.up.plugin.Infix {

    private static final String NAME = "ZERO_SESSION_POOL";
    private static final Cc<String, SessionClient> CC_CLIENTS = Cc.open();

    private static void initInternal(final Vertx vertx,
                                     final String name) {
        getOrCreate(vertx, name);
    }

    public static void init(final Vertx vertx) {
        initInternal(vertx, NAME);
    }

    public static SessionClient getClient() {
        return CC_CLIENTS.store(NAME);
    }

    public static SessionClient getOrCreate(final Vertx vertx) {
        return getOrCreate(vertx, NAME);
    }

    private static SessionClient getOrCreate(final Vertx vertx, final String name) {
        final SessionClient client = CC_CLIENTS.store(name);
        if (Objects.isNull(client)) {
            /* Null will create new */
            return CC_CLIENTS.pick(() -> io.vertx.up.plugin.Infix.init(YmlCore.inject.SESSION,
                (config) -> SessionClient.createShared(vertx, config),
                SessionInfix.class
            ), name);
        } else {
            /*
             * Not null, it will get previous reference
             */
            return client;
        }
    }

    @Override
    public SessionClient get() {
        return getClient();
    }
}
