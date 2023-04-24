package io.vertx.up.secure.authorization;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.authorization.AndAuthorization;
import io.vertx.ext.auth.authorization.Authorization;
import io.vertx.ext.auth.authorization.OrAuthorization;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.annotations.AuthorizedResource;
import io.vertx.up.atom.secure.Aegis;
import io.horizon.eon.em.secure.AuthWord;
import io.vertx.up.fn.Fn;
import io.vertx.up.secure.profile.PermissionAuthorization;
import io.vertx.up.util.Ut;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * There are some difference between vertx and zero, in vertx, the resource and permissions are defined in
 * Static Mode, it means that you won't fetch resource in your @AuthorizedResource method, in this kind of
 * situation, you can pass `Authorization` object and keep matching unique one. But in zero framework, the
 * `Authorization` object is calculated by resource in each request.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AuthorizationResourceImpl implements AuthorizationResource {
    private final transient Aegis aegis;

    AuthorizationResourceImpl(final Aegis aegis) {
        this.aegis = aegis;
    }

    @SuppressWarnings("all")
    public void requestResource(final RoutingContext context, final Handler<AsyncResult<Authorization>> handler) {
        final JsonObject params = AuthorizationResource.parameters(context);
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
}
