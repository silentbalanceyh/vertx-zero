package io.vertx.up.secure.authenticate;

import io.vertx.core.json.JsonObject;
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
class AuthenticateCache {

    private static final Annal LOGGER = Annal.get(AuthenticateCache.class);

    static void userAuthorized(final JsonObject credentials, final Actuator actuator,
                               final Actuator fnCache) {
        final String habitus = credentials.getString(KName.HABITUS);
        final Rapid<String, JsonObject> rapid = Rapid.t(habitus);
        rapid.read(RapidKey.User.AUTHENTICATE).onComplete(res -> {
            if (res.succeeded()) {
                final JsonObject cached = res.result();
                if (Objects.isNull(cached)) {
                    actuator.execute();
                } else {
                    if (Debugger.onAuthorizedCache()) {
                        LOGGER.info("[ Auth ]\u001b[0;32m 401 Authenticated Cached successfully!\u001b[m");
                    }
                    fnCache.execute();
                }
            }
        });
    }

    static void userAuthorize(final JsonObject credentials, final Actuator actuator) {
        final String habitus = credentials.getString(KName.HABITUS);
        final Rapid<String, JsonObject> rapid = Rapid.t(habitus);
        rapid.write(RapidKey.User.AUTHENTICATE, credentials).onComplete(next -> actuator.execute());
    }
}
