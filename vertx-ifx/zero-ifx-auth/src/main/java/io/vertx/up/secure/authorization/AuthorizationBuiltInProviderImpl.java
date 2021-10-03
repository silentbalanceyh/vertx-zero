package io.vertx.up.secure.authorization;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authorization.AndAuthorization;
import io.vertx.ext.auth.authorization.Authorization;
import io.vertx.ext.auth.authorization.AuthorizationProvider;
import io.vertx.ext.auth.authorization.OrAuthorization;
import io.vertx.up.annotations.AuthorizedResource;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.eon.em.AuthWord;
import io.vertx.up.exception.web._403ForbiddenException;
import io.vertx.up.fn.Fn;
import io.vertx.up.secure.profile.PermissionAuthorization;
import io.vertx.up.util.Ut;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AuthorizationBuiltInProviderImpl implements AuthorizationBuiltInProvider {

    private transient final Aegis aegis;

    private AuthorizationBuiltInProviderImpl(final Aegis aegis) {
        this.aegis = aegis;
    }

    public static AuthorizationProvider provider(final Aegis aegis) {
        return new AuthorizationBuiltInProviderImpl(aegis);
    }

    @Override
    public String getId() {
        return this.aegis.getType().key();
    }

    @Override
    public void getAuthorizations(final User user, final JsonObject request, final Handler<AsyncResult<Void>> handler) {
        this.getAuthorized(user, actual -> this.getResource(request, required -> {
            final Authorization requiredAuthorization = required.result();
            if (requiredAuthorization.match(user)) {
                handler.handle(Future.succeededFuture());
            } else {
                handler.handle(Future.failedFuture(new _403ForbiddenException(this.getClass())));
            }
        }));
    }

    @SuppressWarnings("all")
    private void getResource(final JsonObject params, final Handler<AsyncResult<Authorization>> handler) {
        final Method method = this.aegis.getAuthorizer().getResource();
        Fn.safeJvm(() -> {
            final Future<Object> future = (Future<Object>) method.invoke(this.aegis.getProxy(), params);
            future.onComplete(res -> {
                if (res.succeeded()) {
                    final Authorization required = this.getResource(res.result(), method);
                    handler.handle(Future.succeededFuture(required));
                } else {
                    final Throwable ex = res.cause();
                    handler.handle(Future.failedFuture(ex));
                }
            });
        });
    }

    @SuppressWarnings("all")
    private Authorization getResource(final Object item, final Method method) {
        final Annotation annotation = method.getAnnotation(AuthorizedResource.class);
        final AuthWord word = Ut.invoke(annotation, "value");
        final Authorization required;
        if (item instanceof Set) {
            final Set<String> set = (Set<String>) item;
            if (AuthWord.AND == word) {
                final AndAuthorization and = AndAuthorization.create();
                set.forEach(each -> and.addAuthorization(PermissionAuthorization.create(each)));
                required = and;
            } else {
                final OrAuthorization and = OrAuthorization.create();
                set.forEach(each -> and.addAuthorization(PermissionAuthorization.create(each)));
                required = and;
            }
        } else {
            // Only one
            required = PermissionAuthorization.create((String) item);
        }
        return required;
    }

    @SuppressWarnings("all")
    private void getAuthorized(final User user, final Handler<AsyncResult<User>> handler) {
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
                    handler.handle(Future.succeededFuture(user));
                } else {
                    final Throwable ex = res.cause();
                    handler.handle(Future.failedFuture(ex));
                }
            });
        });
    }
}
