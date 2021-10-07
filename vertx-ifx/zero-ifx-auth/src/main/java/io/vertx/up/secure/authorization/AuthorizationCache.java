package io.vertx.up.secure.authorization;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;
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

    private static final String KEY = "AUTHORIZED-403";

    private static final Annal LOGGER = Annal.get(AuthorizationCache.class);

    private static String requestKey(final RoutingContext context) {
        final HttpServerRequest request = context.request();
        final String uri = ZeroAnno.recoveryUri(request.path(), request.method());
        return request.method() + " " + uri;
    }

    static void userAuthorized(final RoutingContext context, final Actuator actuator) {
        final User user = context.user();
        final String habitus = user.principal().getString(KName.HABITUS);
        final UxPool pool = Ux.Pool.on(habitus);
        pool.<String, JsonObject>get(KEY).onComplete(res -> {
            if (res.succeeded()) {
                JsonObject authorized = res.result();
                if (Objects.isNull(authorized)) {
                    authorized = new JsonObject();
                }
                final String requestKey = requestKey(context);
                if (authorized.getBoolean(requestKey, Boolean.FALSE)) {
                    LOGGER.info("[ Auth ]\u001b[0;32m 403 Authorized Cached successfully \u001b[m for ( {1}, {0} )", habitus, requestKey);
                    context.next();
                } else {
                    actuator.execute();
                }
            }
        });
    }

    static void userAuthorize(final RoutingContext context, final Actuator actuator) {
        final User user = context.user();
        final String habitus = user.principal().getString(KName.HABITUS);
        final UxPool pool = Ux.Pool.on(habitus);
        pool.<String, JsonObject>get(KEY).onComplete(res -> {
            if (res.succeeded()) {
                final String requestKey = requestKey(context);
                JsonObject authorized = res.result();
                if (Objects.isNull(authorized)) {
                    authorized = new JsonObject();
                }
                authorized.put(requestKey, Boolean.TRUE);
                pool.put(KEY, authorized).onComplete(next -> actuator.execute());
            }
        });
    }
}
