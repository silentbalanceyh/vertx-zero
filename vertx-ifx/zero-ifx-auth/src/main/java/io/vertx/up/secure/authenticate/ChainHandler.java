package io.vertx.up.secure.authenticate;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.ext.web.handler.AuthenticationHandler;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface ChainHandler extends AuthenticationHandler {

    static ChainHandler all() {
        return new ChainHandlerImpl(true);
    }

    static ChainHandler any() {
        return new ChainHandlerImpl(false);
    }

    @Fluent
    ChainHandler add(AuthenticationHandler other);
}
