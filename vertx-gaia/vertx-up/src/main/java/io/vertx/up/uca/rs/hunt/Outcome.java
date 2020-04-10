package io.vertx.up.uca.rs.hunt;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.Strings;

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
         * @Produces means that server generate response to client
         */
        if (produces.isEmpty()) {
            /*
             * Default situation
             */
            response.putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        } else {
            if (produces.contains(MediaType.WILDCARD_TYPE)) {
                /*
                 * Default situation
                 */
                response.putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
            } else {
                /*
                 * MediaType extract from first set
                 */
                final MediaType type = produces.iterator().next();
                if (Objects.isNull(type)) {
                    /*
                     * Default situation
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

    static void security(final HttpServerResponse response) {
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

    static void out(final HttpServerResponse response, final Envelop envelop, final Set<MediaType> produces) {

        /*
         * Set response data for current request / event
         * The mime type has been set before this step ( mime ( HttpServerResponse, Set<MediaType> )
         */
        if (!response.ended()) {
            if (HttpMethod.HEAD == envelop.getMethod()) {

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
                if (Objects.isNull(headerStr)) {

                    /*
                     * Default String mode
                     * 1. No `Content-Type` provided
                     * 2. Default mime is `application/json` and replied in string
                     */
                    response.end(envelop.toString());
                } else {

                    /*
                     * Extract the data `MediaType` from response header
                     */
                    final MediaType type = MediaType.valueOf(headerStr);
                    if (MediaType.WILDCARD_TYPE.equals(type)) {
                        /*
                         * Default String mode
                         * 1. Content-Type is `* / *` format
                         * 2. Replied body directly
                         */
                        response.end(envelop.outString());
                    } else {
                        if (MediaType.APPLICATION_OCTET_STREAM_TYPE.equals(type)) {
                            /*
                             * Buffer only ( Buffer output )
                             * 1. Content-Type is `application/octet-stream`
                             * 2. Replied in Buffer mode
                             * byte[] data body instead of String.
                             *
                             * Situation 1:
                             * Download stream here for file download here
                             */
                            response.end(envelop.outBuffer());
                        } else {

                            /*
                             * Other situation for uniform response
                             * In Stream mode
                             */
                            response.end(envelop.outString());
                        }
                    }
                }
            }
        }
        response.closed();
    }
}
