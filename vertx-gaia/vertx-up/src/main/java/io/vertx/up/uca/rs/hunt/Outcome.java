package io.vertx.up.uca.rs.hunt;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.Strings;
import io.vertx.up.uca.rs.hunt.adaptor.WingSelector;
import io.vertx.up.uca.rs.hunt.adaptor.Wings;

import javax.ws.rs.core.MediaType;
import java.util.Objects;
import java.util.Set;

/**
 * Content-Type
 * Accept
 */
final class Outcome {

    static void media(final HttpServerResponse response, final Set<MediaType> produces) {
        /*
         * Response head already sent
         */
        if (!response.headWritten()) {
            /*
             * @Produces means that server generate response to client
             */
            if (produces.isEmpty()) {
                /*
                 * No `produces` set for current api, select default `application/json`.
                 */
                response.putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
            } else {
                if (produces.contains(MediaType.WILDCARD_TYPE)) {
                    /*
                     * Here are `.* / .*` wild card type set, select default `application/json`.
                     */
                    response.putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
                } else {
                    /*
                     * MediaType extract from first set
                     */
                    final MediaType type = produces.iterator().next();
                    if (Objects.isNull(type)) {
                        /*
                         * No content type set default situation, select default `application/json`
                         */
                        response.putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
                    } else {
                        /*
                         * Type + SLASH + SubType
                         */
                        final String content = type.getType() + Strings.SLASH + type.getSubtype();
                        response.putHeader(HttpHeaders.CONTENT_TYPE, content);
                    }
                }
            }
        }
    }

    static void security(final HttpServerResponse response) {
        /*
         * Response head already sent
         */
        if (!response.headWritten()) {
            /*
             * Refer: https://vertx.io/blog/writing-secure-vert-x-web-apps/
             * */
            response
                /*
                 * do not allow proxies to cache the data
                 */
                .putHeader("Cache-Control", "no-store, no-cache")
                /*
                 * prevents Internet Explorer from MIME - sniffing a
                 * response away from the declared content-type
                 */
                .putHeader("X-Content-Type-Options", "nosniff")
                /*
                 * Strict HTTPS (for about ~6Months)
                 */
                .putHeader("Strict-Transport-Security", "max-age=" + 15768000)
                /*
                 * IE8+ do not allow opening of attachments in the context of this resource
                 */
                .putHeader("X-Download-Options", "noopen")
                /*
                 * enable XSS for IE
                 */
                .putHeader("X-XSS-Protection", "1; mode=block")
                /*
                 * deny frames
                 */
                .putHeader("X-FRAME-OPTIONS", "DENY");
            /*
             * Set-Cookie
             */
        }
    }

    static void out(final HttpServerResponse response, final Envelop envelop, final Set<MediaType> produces) {
        /*
         * Response processing
         */
        if (!response.headWritten()) {
            /*
             * Set response data for current request / event
             * The mime type has been set before this step ( mime ( HttpServerResponse, Set<MediaType> )
             */
            if (!response.ended()) {
                if (HttpMethod.HEAD == envelop.method()) {
                    /*
                     * Whether the method is header
                     * When it's header, here process header request in special situation
                     * Only header
                     * 1. @HEAD annotation
                     * 2. No Data response ( No Content )
                     */
                    response.setStatusCode(HttpStatusCode.NO_CONTENT.code());
                    response.setStatusMessage(HttpStatusCode.NO_CONTENT.message());
                    response.end(Strings.EMPTY);
                } else {
                    final String headerStr = response.headers().get(HttpHeaders.CONTENT_TYPE);
                    /*
                     * Wings selected
                     */
                    final Wings wings = WingSelector.end(headerStr, produces);
                    /*
                     * Response head already sent
                     */
                    wings.output(response, envelop);
                }
            }
        }
        response.closed();
    }
}
