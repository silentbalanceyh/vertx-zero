package io.vertx.up.uca.micro.discovery;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.servicediscovery.Record;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.exception.WebException;
import io.vertx.up.exception.web._404ServiceNotFoundException;
import io.vertx.up.exception.web._405MethodForbiddenException;
import io.vertx.up.exception.web._500InternalServerException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.rs.hunt.Answer;
import io.vertx.up.util.Ut;
import org.apache.http.Header;
import org.apache.http.StatusLine;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * Reply response to client for special situation
 * Include some important http status code
 */
public final class InOut {

    private static final Annal LOGGER = Annal.get(InOut.class);

    /**
     * Service Not Found ( 404 )
     */
    static void sync404Error(final Class<?> clazz,
                             final RoutingContext context) {
        final HttpServerRequest request = context.request();
        final WebException exception = new _404ServiceNotFoundException(clazz, request.uri(),
                request.method());
        Answer.reply(context, Envelop.failure(exception));
    }

    /**
     * Method not allowed ( 405 )
     */
    public static void sync405Error(final Class<?> clazz,
                                    final RoutingContext context) {
        final HttpServerRequest request = context.request();
        final WebException exception = new _405MethodForbiddenException(clazz, request.method(),
                request.uri());
        Answer.reply(context, Envelop.failure(exception));
    }

    public static void sync500Error(final Class<?> clazz,
                                    final RoutingContext context,
                                    final Throwable ex) {
        final WebException exception = new _500InternalServerException(clazz,
                null == ex ? "Server Error" : ex.getMessage());
        Answer.reply(context, Envelop.failure(exception));
    }

    /**
     * Success ( 200 )
     */
    private static void syncSuccess(
            final HttpServerResponse response,
            final HttpResponse<Buffer> clientResponse) {
        final Buffer data = clientResponse.bodyAsBuffer();
        // Copy header
        syncSuccess(response,
                clientResponse.headers(),
                clientResponse.statusCode(),
                clientResponse.statusMessage(),
                data);
    }

    static Handler<AsyncResult<HttpResponse<Buffer>>> replyWeb(
            final Class<?> clazz,
            final RoutingContext context,
            final Consumer<Void> consumer
    ) {
        return response -> {
            if (response.succeeded()) {
                final HttpResponse<Buffer> remoteResp = response.result();
                if (404 == remoteResp.statusCode()) {
                    /*
                     * 404 -> 405 Error
                     */
                    InOut.sync405Error(clazz, context);
                } else {
                    /*
                     * 200 -> Success
                     */
                    InOut.syncSuccess(context.response(), remoteResp);
                }
            } else {
                /*
                 * 500
                 */
                InOut.sync500Error(clazz, context, response.cause());
            }
            consumer.accept(null);
        };
    }

    static Handler<org.apache.http.HttpResponse> replyHttp(
            final Class<?> clazz,
            final RoutingContext context,
            final Consumer<Void> consumer
    ) {
        return response -> {
            /*
             * Get status
             */
            final StatusLine line = response.getStatusLine();
            if (null == line) {
                /*
                 * 500 Error
                 */
                InOut.sync500Error(clazz, context, new RuntimeException("Remote 500 Error."));
            } else {
                if (404 == line.getStatusCode()) {
                    /*
                     * 404 -> 405 Error
                     */
                    InOut.sync405Error(clazz, context);
                } else {
                    /*
                     * 200 -> Success
                     */
                    InOut.syncSuccess(context, response);
                }
            }
            consumer.accept(null);
        };
    }

    private static void syncSuccess(
            final RoutingContext context,
            final org.apache.http.HttpResponse clientResponse
    ) {
        final StatusLine line = clientResponse.getStatusLine();
        final HttpServerResponse response = context.response();
        /*
         * Body processing
         */
        Fn.safeJvm(() -> {
            final InputStream in = clientResponse.getEntity().getContent();
            // No performance considering
            final byte[] bytes = new byte[in.available()];
            final int result = in.read(bytes);
            if (Values.RANGE != result) {

                // Body data
                final Buffer buffer = Buffer.buffer(bytes);

                // Headers
                final MultiMap headers = MultiMap.caseInsensitiveMultiMap();
                for (final Header header : clientResponse.getAllHeaders()) {
                    LOGGER.info("[ Zero ] Response header: {0} = {1}",
                            header.getName(), header.getValue());
                    headers.set(header.getName(), header.getValue());
                }
                // Cors copy switch here
                {
                    final HttpServerRequest request = context.request();
                    final String origin = request.getHeader(HttpHeaders.ORIGIN);
                    if (Ut.notNil(origin)) {
                        // No 'Access-Control-Allow-Origin' header is present on the requested resource.
                        headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
                    }
                }
                // Bridge to Vert.x response
                syncSuccess(response, headers,
                        line.getStatusCode(),
                        line.getReasonPhrase(),
                        buffer);
            }
        });
    }

    private static void syncSuccess(
            final HttpServerResponse response,
            final MultiMap headers,
            final int statusCode,
            final String statusMessage,
            final Buffer data
    ) {
        response.headers().setAll(headers);
        response.setStatusCode(statusCode);
        response.setStatusMessage(statusMessage);
        response.write(data);
        response.end();
    }

    // ----------------------- In Methods for request
    public static String normalizeUri(final RoutingContext event) {
        final StringBuilder uri = new StringBuilder();
        uri.append(event.request().path());
        if (null != event.request().query()) {
            uri.append(Strings.QUESTION).append(event.request().query());
        }
        return uri.toString();
    }

    static RequestOptions getOptions(final Record record, final String normalizedUri) {
        final RequestOptions options = new RequestOptions();
        options.setURI(normalizedUri);
        // Extract host / port
        final JsonObject location = record.getLocation();
        options.setHost(location.getString("host"));
        options.setPort(location.getInteger("port"));
        LOGGER.info("Found remote host: {0}, port: {1}, uri: {2}",
                options.getHost(), String.valueOf(options.getPort()), options.getURI());
        return options;
    }
}
