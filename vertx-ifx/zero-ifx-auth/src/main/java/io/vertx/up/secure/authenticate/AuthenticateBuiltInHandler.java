package io.vertx.up.secure.authenticate;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.impl.AuthenticationHandlerInternal;
import io.vertx.tp.error._401UnauthorizedException;
import io.vertx.up.atom.secure.Aegis;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AuthenticateBuiltInHandler implements AuthenticationHandlerInternal {
    private transient final Aegis aegis;
    private transient final AuthenticationHandler internal;

    private AuthenticateBuiltInHandler(final AuthenticationHandler internal,
                                       final Aegis aegis) {
        this.internal = internal;
        this.aegis = aegis;
    }

    public static AuthenticateBuiltInHandler wrap(final AuthenticationHandler internal,
                                                  final Aegis aegis) {
        return new AuthenticateBuiltInHandler(internal, aegis);
    }

    @Override
    public void authenticate(final RoutingContext context, final Handler<AsyncResult<User>> handler) {
        // Should cost successfully
        ((AuthenticationHandlerInternal) this.internal).authenticate(context, processed -> {
            if (processed.succeeded()) {
                final User user = processed.result();
                final AuthenticationProvider provider = AuthenticateBuiltInProvider.provider(this.aegis);
                provider.authenticate(user.principal(), handler);
            } else {
                // Exception Throw
                final Throwable ex = processed.cause();
                if (Objects.isNull(ex)) {
                    // 401 Without Exception
                    handler.handle(Future.failedFuture(new _401UnauthorizedException(this.getClass())));
                } else {
                    // 401 With Throwable
                    handler.handle(Future.failedFuture(ex));
                }
            }
        });
    }

    @Override
    public void handle(final RoutingContext event) {
        this.internal.handle(event);
    }
}
