package io.vertx.up.secure.authorization;

import io.vertx.core.Future;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.eon.Constants;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Actuator;
import io.vertx.up.log.Annal;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.unity.Ux;
import io.vertx.up.unity.UxPool;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AuthorizationCache {

    private static final Annal LOGGER = Annal.get(AuthorizationCache.class);

    private static String requestKey(final RoutingContext context) {
        final HttpServerRequest request = context.request();
        final String uri = ZeroAnno.recoveryUri(request.path(), request.method());
        return request.method() + " " + uri;
    }

    static void userAuthorized(final RoutingContext context, final Actuator actuator) {
        final User user = context.user();
        final String session = user.principal().getString(KName.SESSION);
        final UxPool pool = Ux.Pool.on(Constants.Pool.SESSION_AUTHORIZED);
        final Future<JsonObject> future = pool.get(session);
        future.onComplete(res -> {
            if (res.succeeded()) {
                JsonObject authorized = res.result();
                if (Objects.isNull(authorized)) {
                    authorized = new JsonObject();
                }
                final String requestKey = requestKey(context);
                if (authorized.getBoolean(requestKey, Boolean.FALSE)) {
                    LOGGER.info("[ Auth ]\u001b[0;32m 403 Authorized Cached successfully \u001b[m for ( {0}, {1} )", session, requestKey);
                    context.next();
                } else {
                    actuator.execute();
                }
            }
        });
    }

    static void userAuthorize(final RoutingContext context, final Actuator actuator) {
        final User user = context.user();
        final String session = user.principal().getString(KName.SESSION);
        final UxPool pool = Ux.Pool.on(Constants.Pool.SESSION_AUTHORIZED);
        final Future<JsonObject> future = pool.get(session);
        future.onComplete(res -> {
            if (res.succeeded()) {
                final String requestKey = requestKey(context);
                JsonObject authorized = res.result();
                if (Objects.isNull(authorized)) {
                    authorized = new JsonObject();
                }
                authorized.put(requestKey, Boolean.TRUE);
                /*
                 * Here are the auto pool for each session has 2 hours
                 * authorized keep sessions to avoid different user used the same
                 * browser to keep session. In future version it will be replaced by token
                 * expired
                 */
                pool.put(session, authorized, 2 * 60 * 60)
                    .onComplete(next -> actuator.execute());
            }
        });
    }
}
