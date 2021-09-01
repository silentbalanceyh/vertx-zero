package io.vertx.tp.ke.refine;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.Session;
import io.vertx.tp.error._409SessionConflictException;
import io.vertx.tp.plugin.session.SessionClient;
import io.vertx.tp.plugin.session.SessionInfix;
import io.vertx.up.atom.secure.Vis;
import io.vertx.up.commune.Envelop;
import io.vertx.up.exception.WebException;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/*
 * Key generated for uniform platform
 */
class KeCache {

    private static final SessionClient CLIENT = SessionInfix.getClient();

    static String keySession(final String method, final String uri, final Vis view) {
        /*
         * session-POST:uri:position/name
         */
        return "session-" + method + ":" + uri + ":" + view.position() + "/" + view.view();
    }

    static String keyAuthorized(final String method, final String uri) {
        return "authorized-" + method + ":" + uri;
    }

    static String keyHabitus(final Envelop envelop) {
        final String token = envelop.jwt();
        final JsonObject tokenJson = Ux.Jwt.extract(token);
        return tokenJson.getString("habitus");
    }

    static String keyUser(final Envelop envelop) {
        return keyUser(envelop.user());
    }

    static String keyUser(final User user) {
        /* Principle */
        final JsonObject principle = user.principal();
        /* User extract */
        final String token = principle.getString("jwt");
        final JsonObject credential = Ux.Jwt.extract(token);
        return credential.getString("user");
    }

    /*
     * Session Data Get
     */
    static Future<Session> session(final String id) {
        return CLIENT.get(id).compose(session -> {
            if (Objects.nonNull(session) && !session.isDestroyed()) {
                return Future.succeededFuture(session);
            } else {
                final WebException error = new _409SessionConflictException(KeCache.class, id);
                return Future.failedFuture(error);
            }
        });
    }

    static <T> Future<T> session(final Session session, final String sessionKey, final String dataKey, final T value) {
        /* Data Get */
        final Buffer storedBuffer = session.get(sessionKey);
        /* Updated Projection */
        if (Objects.nonNull(storedBuffer)) {
            final JsonObject storedData = storedBuffer.toJsonObject();
            storedData.put(dataKey, value);
            session.put(sessionKey, storedData.toBuffer());
        }
        return Ux.future(value);
    }
}
