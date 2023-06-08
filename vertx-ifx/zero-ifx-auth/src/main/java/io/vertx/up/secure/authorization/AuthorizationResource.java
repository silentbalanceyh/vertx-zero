package io.vertx.up.secure.authorization;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authorization.Authorization;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.commune.secure.Aegis;
import io.vertx.up.commune.secure.Vis;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KWeb;
import io.vertx.up.runtime.ZeroAnno;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface AuthorizationResource {

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
    static JsonObject parameters(final RoutingContext context) {
        final User user = context.user();
        final JsonObject normalized;
        if (Objects.isNull(user)) {
            normalized = new JsonObject();
        } else {
            // Keep the original workflow
            normalized = user.principal().copy();
        }
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
            if (KWeb.HEADER.PARAM_MAP.containsKey(entry.getKey())) {
                headers.put(entry.getKey(), entry.getValue());
            }
        });
        normalized.put("headers", headers);
        /*
         * Build data part ( collect all data )
         */
        return normalized;
    }

    static AuthorizationResource buildIn(final Aegis aegis) {
        return new AuthorizationResourceImpl(aegis);
    }

    void requestResource(RoutingContext context, Handler<AsyncResult<Authorization>> handler);
}
