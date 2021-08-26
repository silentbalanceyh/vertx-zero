package io.vertx.tp.plugin.session;

import io.vertx.core.Vertx;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.eon.Plugins;
import io.vertx.up.fn.Fn;
import io.vertx.up.plugin.Infix;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Plugin
@SuppressWarnings("unchecked")
public class SessionInfix implements Infix {

    private static final String NAME = "ZERO_SESSION_POOL";
    private static final ConcurrentMap<String, SessionClient> CLIENTS
        = new ConcurrentHashMap<>();

    private static void initInternal(final Vertx vertx,
                                     final String name) {
        getOrCreate(vertx, name);
    }

    public static void init(final Vertx vertx) {
        initInternal(vertx, NAME);
    }

    public static SessionClient getClient() {
        return CLIENTS.get(NAME);
    }

    public static SessionClient getOrCreate(final Vertx vertx) {
        return getOrCreate(vertx, NAME);
    }

    private static SessionClient getOrCreate(final Vertx vertx, final String name) {
        final SessionClient client = CLIENTS.get(name);
        if (Objects.isNull(client)) {
            /*
             * Null will create new
             */
            return Fn.pool(CLIENTS, name,
                () -> Infix.init(Plugins.Infix.SESSION,
                    (config) -> SessionClient.createShared(vertx, config),
                    SessionInfix.class));
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
