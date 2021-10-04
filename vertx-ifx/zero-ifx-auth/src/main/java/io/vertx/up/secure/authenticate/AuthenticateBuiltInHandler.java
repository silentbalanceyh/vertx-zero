package io.vertx.up.secure.authenticate;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.impl.AuthenticationHandlerImpl;
import io.vertx.up.atom.secure.Aegis;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AuthenticateBuiltInHandler extends AuthenticationHandlerImpl<AuthenticationProvider> {

    private AuthenticateBuiltInHandler(final Aegis aegis) {
        super(AuthenticateBuiltInProvider.provider(aegis));
    }

    public static AuthenticationHandler create(final Aegis aegis) {
        return new AuthenticateBuiltInHandler(aegis);
    }

    @Override
    public void authenticate(final RoutingContext context, final Handler<AsyncResult<User>> handler) {
        final User user = context.user();
        Objects.requireNonNull(user);
        this.authProvider.authenticate(user.principal(), handler);
    }
}
