package io.vertx.up.secure.authorization;

import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.up.atom.secure.Aegis;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface AuthorizationZeroHandler extends AuthorizationHandler {

    default AuthorizationZeroHandler configure(final Aegis aegis) {
        return this;
    }
}
