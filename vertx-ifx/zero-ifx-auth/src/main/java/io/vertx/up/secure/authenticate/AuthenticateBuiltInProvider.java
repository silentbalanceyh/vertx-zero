package io.vertx.up.secure.authenticate;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.tp.error._401UnauthorizedException;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.atom.secure.Against;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AuthenticateBuiltInProvider implements AuthenticationProvider {

    private final static Annal LOGGER = Annal.get(AuthenticateBuiltInProvider.class);
    private final transient Aegis aegis;
    private transient Function<JsonObject, Future<User>> userFn;

    @SuppressWarnings("all")
    private AuthenticateBuiltInProvider(final Aegis aegis) {
        this.aegis = aegis;
        // @AuthorizedUser method process user data
        final Method method = aegis.getAuthorizer().getUser();
        if (Objects.nonNull(method)) {
            this.userFn = (json) -> (Future<User>) Fn.getJvm(() -> method.invoke(aegis.getProxy(), json));
        }
    }

    public static AuthenticateBuiltInProvider provider(final Aegis aegis) {
        return new AuthenticateBuiltInProvider(aegis);
    }

    @Override
    public void authenticate(final JsonObject credentials, final Handler<AsyncResult<User>> handler) {
        final Against against = this.aegis.getAuthorizer();
        final Method method = against.getAuthenticate();
        if (Objects.isNull(method)) {
            // Exception for method is null ( This situation should not happen )
            handler.handle(Future.failedFuture(new _401UnauthorizedException(this.getClass())));
        } else {
            // Verify the data by @Wall's method that has been annotated by @Authenticate
            final Object proxy = this.aegis.getProxy();
            final Future<Boolean> checkedFuture = Ut.invokeAsync(proxy, method, credentials);
            checkedFuture.onComplete(res -> {
                if (res.succeeded()) {
                    final Boolean checked = res.result();
                    if (Objects.isNull(checked) || !checked) {
                        // 401 Workflow
                        handler.handle(Future.failedFuture(new _401UnauthorizedException(this.getClass())));
                    } else {
                        // Success to passed validation
                        LOGGER.info("[ Auth ]\u001b[0;32m 401 Authenticated successfully!\u001b[m");
                        if (Objects.isNull(this.userFn)) {
                            // Attribute should be empty here
                            final User user = User.create(credentials, new JsonObject());
                            handler.handle(Future.succeededFuture(user));
                        } else {
                            handler.handle(this.userFn.apply(credentials));
                        }
                    }
                } else {
                    // Exception Throw
                    final Throwable ex = res.cause();
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
    }
}
