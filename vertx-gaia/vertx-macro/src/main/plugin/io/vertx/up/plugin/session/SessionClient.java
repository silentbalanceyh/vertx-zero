package io.vertx.up.plugin.session;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.up.util.Ut;

/*
 * Session Client in zero system, it could be enabled by zero
 * and keep session when authorization.
 *
 * Keep only one session store.
 */
public interface SessionClient {
    /*
     * Create local session store bind data
     */
    static SessionClient createShared(final Vertx vertx, final JsonObject config) {
        return SessionClientImpl.create(vertx, Ut.isNil(config) ? new JsonObject() : config);
    }

    static SessionClient createShared(final Vertx vertx) {
        return SessionClientImpl.create(vertx, new JsonObject());
    }

    SessionHandler getHandler();

    /*
     * Get Session by id
     */
    Future<Session> get(String id);

    /*
     * Open new Session
     */
    Future<Session> open(String id);
}
