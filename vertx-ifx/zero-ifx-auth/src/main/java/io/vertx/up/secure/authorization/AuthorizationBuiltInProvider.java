package io.vertx.up.secure.authorization;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authorization.Authorization;
import io.vertx.ext.auth.authorization.AuthorizationProvider;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.fn.Fn;
import io.vertx.up.secure.profile.PermissionAuthorization;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AuthorizationBuiltInProvider implements AuthorizationProvider {

    private transient final Aegis aegis;

    private AuthorizationBuiltInProvider(final Aegis aegis) {
        this.aegis = aegis;
    }

    public static AuthorizationProvider provider(final Aegis aegis) {
        return new AuthorizationBuiltInProvider(aegis);
    }

    @Override
    public String getId() {
        return this.aegis.getType().key();
    }

    @Override
    @SuppressWarnings("all")
    public void getAuthorizations(final User user, final Handler<AsyncResult<Void>> handler) {
        final Method method = this.aegis.getAuthorizer().getAuthorization();
        Fn.safeJvm(() -> {
            /*
             * Future<Set<String>> fetching
             */
            final Future<Set<String>> future = (Future<Set<String>>) method.invoke(this.aegis.getProxy(), user);
            future.onComplete(res -> {
                if (res.succeeded()) {
                    final Set<String> permissionSet = res.result();
                    final Authorization authorization = PermissionAuthorization.create(permissionSet);
                    user.authorizations().add(this.getId(), authorization);
                    handler.handle(Future.succeededFuture());
                } else {
                    final Throwable ex = res.cause();
                    handler.handle(Future.failedFuture(ex));
                }
            });
        });
    }
}
