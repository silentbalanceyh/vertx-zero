package io.vertx.up.uca.micro.ssl.server;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.TCPSSLOptions;
import io.vertx.up.uca.micro.ssl.CertPipe;

/**
 * Pem key cert options
 */
@SuppressWarnings("unchecked")
public class JksCert implements CertPipe<JsonObject> {

    @Override
    public Handler<TCPSSLOptions> parse(final JsonObject config) {
        // TODO: JKS
        return null;
    }
}
