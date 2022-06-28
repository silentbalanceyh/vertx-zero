package io.vertx.up.secure.bridge;

import io.vertx.core.Vertx;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.up.atom.secure.Aegis;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Bolt {
    static Bolt get() {
        return BoltWhich.CC_BOLT.pick(BoltWhich::new);
        // return Fn.po?lThread(BoltWhich.POOL_BOLT, BoltWhich::new);
    }

    /*
     * 1. Authenticate Handler
     */
    AuthenticationHandler authenticate(Vertx vertx, Aegis config);

    /*
     * 2. Authorization Handler
     */
    AuthorizationHandler authorization(Vertx vertx, Aegis config);

    AuthenticationProvider authenticateProvider(Vertx vertx, Aegis config);
}
