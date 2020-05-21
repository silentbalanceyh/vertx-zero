package io.vertx.up.secure.handler;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.eon.ID;
import io.vertx.up.runtime.ZeroAnno;

public class AuthReady {
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
     * Because the application system will scanned our storage to do resource authorization, the application
     * often need the metadata information to do locating and checking here.
     */
    public static JsonObject prepare(final JsonObject data, final RoutingContext context) {
        final JsonObject normalized = data.copy();
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
        metadata.put("uri", ZeroAnno.recoveryUri(request.path(), request.method()));
        metadata.put("requestUri", request.path());
        metadata.put("method", request.method().name());
        normalized.put("metadata", metadata);
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
        return normalized;
    }
}
