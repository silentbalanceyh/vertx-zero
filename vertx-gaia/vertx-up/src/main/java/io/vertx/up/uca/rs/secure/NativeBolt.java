package io.vertx.up.uca.rs.secure;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.up.atom.secure.Cliff;
import io.vertx.up.fn.Fn;

/**
 * Native AuthHandler extract from bolt,
 * All native authHandler could be bind to vert.x security module
 */
class NativeBolt implements Bolt {

    private static Bolt INSTANCE;

    static Bolt create() {
        if (null == INSTANCE) {
            INSTANCE = new NativeBolt();
        }
        return INSTANCE;
    }

    @Override
    public AuthHandler mount(final Vertx vertx,
                             final Cliff cliff) {
        return Fn.getJvm(() -> {
                    final JsonObject config = Fn.getNull(new JsonObject(), cliff::getConfig);
                    final Object reference = cliff.getAuthorizer().getAuthenticate()
                            .invoke(cliff.getProxy(), vertx, config);
                    return null == reference ? null : (AuthHandler) reference;
                }, cliff, cliff.getProxy(), cliff.getAuthorizer(),
                cliff.getAuthorizer().getAuthenticate());
    }
}
