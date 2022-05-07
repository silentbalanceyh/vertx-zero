package io.vertx.up.secure.authorization;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Actuator;
import io.vertx.up.log.Annal;
import io.vertx.up.log.Debugger;
import io.vertx.up.uca.cache.Rapid;
import io.vertx.up.uca.cache.RapidKey;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AuthorizationCache {

    private static final Annal LOGGER = Annal.get(AuthorizationCache.class);

    private static String requestKey(final RoutingContext context) {
        final HttpServerRequest request = context.request();
        // Recovery will let your api/:actor/search abstract uri security issue
        // final String uri = ZeroAnno.recoveryUri(request.path(), request.method());
        return request.method() + " " + request.path();
    }

    static void userAuthorized(final RoutingContext context, final Actuator actuator) {
        final User user = context.user();
        final Rapid<String, JsonObject> rapid = Rapid.user(user);
        rapid.read(RapidKey.User.AUTHORIZATION).onComplete(res -> {
            if (res.succeeded()) {
                JsonObject authorized = res.result();
                if (Objects.isNull(authorized)) {
                    authorized = new JsonObject();
                }
                final String requestKey = requestKey(context);
                if (authorized.getBoolean(requestKey, Boolean.FALSE)) {
                    final String habitus = user.principal().getString(KName.HABITUS);
                    if (Debugger.onAuthorizedCache()) {
                        LOGGER.info("[ Auth ]\u001b[0;32m 403 Authorized Cached successfully \u001b[m for ( {1}, {0} )", habitus, requestKey);
                    }
                    context.next();
                } else {
                    LOGGER.info("[ Auth ] 403 Authorized First Time ! request = {0}", requestKey);
                    actuator.execute();
                }
            }
        });
    }

    static void userAuthorize(final RoutingContext context, final Actuator actuator) {
        final User user = context.user();
        final Rapid<String, JsonObject> rapid = Rapid.user(user);
        rapid.read(RapidKey.User.AUTHORIZATION).onComplete(res -> {
            if (res.succeeded()) {
                final String requestKey = requestKey(context);
                JsonObject authorized = res.result();
                if (Objects.isNull(authorized)) {
                    authorized = new JsonObject();
                }
                authorized.put(requestKey, Boolean.TRUE);
                rapid.write(RapidKey.User.AUTHORIZATION, authorized).onComplete(next -> actuator.execute());
            }
        });
    }
}
