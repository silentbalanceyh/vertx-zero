package io.vertx.up.secure.authorization;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.authorization.AndAuthorization;
import io.vertx.ext.auth.authorization.Authorization;
import io.vertx.ext.auth.authorization.OrAuthorization;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.annotations.AuthorizedResource;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.atom.secure.Vis;
import io.vertx.up.eon.ID;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.AuthWord;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroAnno;
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
class AuthorizationResource {
    private final transient Aegis aegis;

    private AuthorizationResource(final Aegis aegis) {
        this.aegis = aegis;
    }

    public static AuthorizationResource create(final Aegis aegis) {
        return new AuthorizationResource(aegis);
    }

    @SuppressWarnings("all")
    void requestResource(final RoutingContext context, final Handler<AsyncResult<Authorization>> handler) {
        final JsonObject params = getResource(context);
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

    /*
     * This method is for dynamic using of 403 authorization.
     * Sometimes when the URI has been stored as resource in zero system,
     * You can extract the metadata in @Wall classes by
     *      data.getJsonObject("metadata");
     * It means that you can find unique resource identifier by
     * 1) Http Method: GET, DELETE, POST, PUT
     * 2) Uri Original
     * Here are some calculation results that has been provided by zero container such as following situation:
     * When the registry uri is as : /api/test/:name
     * In this situation the real path should be : /api/test/lang
     * In this method the metadata -> uri will be provided by : /api/test/:name
     *                    metadata -> requestUri will be provided by : /api/test/lang
     * It's specific situation when you used path variable.
     *
     * 「Objective」
     * The metadata stored for real project when you want to do some limitation in RBAC mode.
     * Because the application system will scan our storage to do resource authorization, the application
     * often need the metadata information to do locating and checking here.
     */
    private JsonObject getResource(final RoutingContext context) {
        final JsonObject normalized = new JsonObject();
        final HttpServerRequest request = context.request();
        /*
         * Build metadata
         */
        final JsonObject metadata = new JsonObject();
        /*
         * Old: request.uri()
         * New: request.path()
         * path() will remove all query string part
         */
        metadata.put(KName.URI, ZeroAnno.recoveryUri(request.path(), request.method()));
        metadata.put(KName.URI_REQUEST, request.path());
        metadata.put(KName.METHOD, request.method().name());
        /*
         * view parameters for ScRequest to build cache key
         * It's important
         */
        final String literal = request.getParam(KName.VIEW);
        final Vis view = Vis.create(literal);
        metadata.put(KName.VIEW, view);
        normalized.put(KName.METADATA, metadata);
        /*
         * Build Custom Headers
         */
        final MultiMap inputHeaders = request.headers();
        final JsonObject headers = new JsonObject();
        inputHeaders.forEach(entry -> {
            if (ID.Header.PARAM_MAP.containsKey(entry.getKey())) {
                headers.put(entry.getKey(), entry.getValue());
            }
        });
        normalized.put("headers", headers);
        /*
         * Build data part ( collect all data )
         */
        return normalized;
    }
}
