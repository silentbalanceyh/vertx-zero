package io.vertx.up.secure.authenticate;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Actuator;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.unity.UxPool;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AuthenticateCache {

    private static final String KEY = "AUTHORIZED-401";
    private static final Annal LOGGER = Annal.get(AuthenticateCache.class);

    static void userAuthorized(final JsonObject credentials, final Actuator actuator,
                               final Actuator fnCache) {
        final String habitus = credentials.getString(KName.HABITUS);
        final UxPool pool = Ux.Pool.on(habitus);
        pool.<String, JsonObject>get(KEY).onComplete(res -> {
            if (res.succeeded()) {
                final JsonObject cached = res.result();
                if (Objects.isNull(cached)) {
                    actuator.execute();
                } else {
                    LOGGER.info("[ Auth ]\u001b[0;32m 401 Authenticated Cached successfully!\u001b[m");
                    fnCache.execute();
                }
            }
        });
    }

    static void userAuthorize(final JsonObject credentials, final Actuator actuator) {
        final String habitus = credentials.getString(KName.HABITUS);
        final UxPool pool = Ux.Pool.on(habitus);
        pool.<String, JsonObject>get(KEY).onComplete(res -> {
            if (res.succeeded()) {
                pool.put(KEY, credentials).onComplete(next -> actuator.execute());
            }
        });
    }
}
