package io.vertx.up.secure.authorization;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authorization.Authorization;
import io.vertx.ext.auth.authorization.AuthorizationProvider;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.fn.Fn;

import java.lang.reflect.Method;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WallAuthorizationProvider implements AuthorizationProvider {

    private transient final Aegis aegis;

    private WallAuthorizationProvider(final Aegis aegis) {
        this.aegis = aegis;
    }

    public static AuthorizationProvider provider(final Aegis aegis) {
        return new WallAuthorizationProvider(aegis);
    }

    @Override
    public String getId() {
        return this.aegis.getType().key();
    }

    @Override
    public void getAuthorizations(final User user, final Handler<AsyncResult<Void>> handler) {
        this.runUser(user, result -> {
            System.out.println(result);
        });
    }

    @SuppressWarnings("all")
    private void runUser(final User user, final Handler<AsyncResult<User>> handler) {
        final Method method = this.aegis.getAuthorizer().getAuthorization();
        Fn.safeJvm(() -> {
            /*
             * Future<Authorization> fetching
             */
            final Future<Authorization> future = (Future<Authorization>) method.invoke(this.aegis.getProxy(), user);
            future.onComplete(res -> {
                if (res.succeeded()) {
                    final Authorization authorization = res.result();
                    user.authorizations().add(this.getId(), authorization);
                    handler.handle(Future.succeededFuture(user));
                } else {
                    final Throwable ex = res.cause();
                    handler.handle(Future.failedFuture(ex));
                }
            });
        });
    }
}
