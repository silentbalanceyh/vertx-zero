package io.vertx.tp.plugin.session;

import io.horizon.eon.em.StoreType;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.ClusteredSessionStore;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import io.vertx.up.exception.web._500SessionClientInitException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.concurrent.atomic.AtomicBoolean;

public class SessionClientImpl implements SessionClient {

    private static final Annal LOGGER = Annal.get(SessionClientImpl.class);
    private static final AtomicBoolean LOG_MSG = new AtomicBoolean(true);
    private static SessionStore STORE;
    private final transient Vertx vertx;

    private SessionClientImpl(final Vertx vertx, final JsonObject config, final StoreType type) {
        this.vertx = vertx;
        if (null == STORE) {
            if (LOG_MSG.getAndSet(Boolean.FALSE)) {
                LOGGER.info(Info.SESSION_MODE, type);
            }
            /* Whether existing store */
            if (StoreType.LOCAL == type) {
                STORE = LocalSessionStore.create(vertx);
            } else if (StoreType.CLUSTER == type) {
                STORE = ClusteredSessionStore.create(this.vertx);
            } else {
                final String store = config.getString("store");
                Fn.outWeb(Ut.isNil(store), _500SessionClientInitException.class, this.getClass());
                LOGGER.info(Info.SESSION_STORE, store);
                /*
                 * SessionStore -> Defined here
                 * The session store could not be singleton because each session store must not
                 * be shared and located by each thread here.
                 */
                final SessionStore defined = Ut.singleton(store);
                JsonObject opts = config.getJsonObject("options");
                if (Ut.isNil(opts)) {
                    opts = new JsonObject();
                }
                STORE = defined.init(vertx, opts);
            }
        }
    }

    static SessionClientImpl create(final Vertx vertx, final JsonObject config) {
        final String type = config.getString("category");
        if ("CLUSTER".equals(type)) {
            /* CLUSTER */
            return new SessionClientImpl(vertx, config, StoreType.CLUSTER);
        } else if ("DEFINED".equals(type)) {
            /* DEFINED */
            return new SessionClientImpl(vertx, config, StoreType.DEFINED);
        } else {
            /* LOCAL ( Default ) */
            return new SessionClientImpl(vertx, config, StoreType.LOCAL);
        }
    }

    @Override
    public SessionHandler getHandler() {
        return SessionHandler.create(STORE)
            /*
             * Refer: https://vertx.io/blog/writing-secure-vert-x-web-apps/
             * */
            // .setCookieSecureFlag(true)
            .setCookieHttpOnlyFlag(true);
    }

    @Override
    public Future<Session> get(final String id) {
        final Promise<Session> promise = Promise.promise();
        STORE.get(id, result -> {
            if (result.succeeded()) {
                promise.complete(result.result());
            } else {
                promise.complete(null);
            }
        });
        return promise.future();
    }

    @Override
    public Future<Session> open(final String sessionId) {
        return Future.succeededFuture(STORE.createSession(2000));
    }
}
