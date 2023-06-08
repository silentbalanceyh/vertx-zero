package io.vertx.up.uca.cosmic;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.Integration;
import io.vertx.up.util.Ut;

import java.util.Objects;

/*
 * Communicate with Integration/IntegrationRequest structure for
 * Http / Https client build
 */
public interface Emitter {
    /*
     * Get Emitter reference
     */
    static Emitter create(final Integration integration) {
        if (Objects.isNull(integration)) {
            /*
             * null reference
             */
            return null;
        } else {

            final String publicFile = integration.getPublicKeyFile();
            if (Ut.isNil(publicFile)) {
                /*
                 * If not for `publicKeyFile`, the client will be in `standard` mode
                 * Authorization, token mode, the token could be
                 *
                 * 1) WebToken interface and default implementation is `username/password` of `Basic`
                 * 2) You can provide your own token implementation such as other authorization
                 */
                return CACHE.CC_EMITTER.pick(() -> new StandardEmitter(integration), integration.hashCode());
                // return Fn.po?l(Pool.POOL_EMITTER, integration.hashCode(), () -> new StandardEmitter(integration));
            } else {
                /*
                 * If the `integration` contains publicKeyFile configured
                 * The `Emitter` will be switched to `LegacyEmitter` ( Java Net )
                 *
                 * The implementation is HttpURLConnection ( for old mode )
                 * It could set `TLS / SSL` of https
                 */
                return CACHE.CC_EMITTER.pick(() -> new LegacyEmitter(integration), integration.hashCode());
                // return Fn.po?l(Pool.POOL_EMITTER, integration.hashCode(), () -> new LegacyEmitter(integration));
            }
        }
    }

    /*
     * String Request
     */
    String request(String apiKey, JsonObject params, MultiMap headers);

    default String request(final String apiKey, final JsonObject params) {
        return this.request(apiKey, params, MultiMap.caseInsensitiveMultiMap());
    }

    default Future<String> requestAsync(final String apiKey, final JsonObject params, final MultiMap headers) {
        return Future.succeededFuture(this.request(apiKey, params, headers));
    }

    default Future<String> requestAsync(final String apiKey, final JsonObject params) {
        return Future.succeededFuture(this.request(apiKey, params));
    }

    /*
     * JsonArray request
     */
    JsonArray requestA(String apiKey, JsonObject params, MultiMap headers);

    default Future<JsonArray> requestAsyncA(final String apiKey, final JsonObject params, final MultiMap headers) {
        return Future.succeededFuture(this.requestA(apiKey, params, headers));
    }

    default JsonArray requestA(final String apiKey, final JsonObject params) {
        return this.requestA(apiKey, params, MultiMap.caseInsensitiveMultiMap());
    }

    default Future<JsonArray> requestAsyncA(final String apiKey, final JsonObject params) {
        return Future.succeededFuture(this.requestA(apiKey, params));
    }

    /*
     * JsonObject Request
     */
    JsonObject requestJ(String apiKey, JsonObject params, MultiMap headers);

    default Future<JsonObject> requestAsyncJ(final String apiKey, final JsonObject params, final MultiMap headers) {
        return Future.succeededFuture(this.requestJ(apiKey, params, headers));
    }

    default JsonObject requestJ(final String apiKey, final JsonObject params) {
        return this.requestJ(apiKey, params, MultiMap.caseInsensitiveMultiMap());
    }

    default Future<JsonObject> requestAsyncJ(final String apiKey, final JsonObject params) {
        return Future.succeededFuture(this.requestJ(apiKey, params));
    }
}
