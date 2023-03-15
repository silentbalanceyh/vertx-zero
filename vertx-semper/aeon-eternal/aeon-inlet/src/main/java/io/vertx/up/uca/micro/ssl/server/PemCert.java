package io.vertx.up.uca.micro.ssl.server;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.OpenSSLEngineOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.core.net.TCPSSLOptions;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.micro.ssl.CertPipe;
import io.vertx.up.uca.micro.ssl.tls.Cert;

/**
 * Pem key cert options
 */
@SuppressWarnings("unchecked")
public class PemCert implements CertPipe<JsonObject> {

    private static final Annal LOGGER = Annal.get(PemCert.class);

    private static final String PATH_CERT = "cert";
    private static final String PATH_KEY = "key";

    @Override
    public Handler<TCPSSLOptions> parse(final JsonObject options) {
        return Fn.orNull(() -> {
            final PemKeyCertOptions pem = Fn.orSemi(
                null == options ||
                    !options.containsKey(PATH_KEY) ||
                    !options.containsKey(PATH_CERT), LOGGER,
                Cert.SERVER_PEM,
                () -> new PemKeyCertOptions().setKeyPath(PATH_KEY).setCertPath(PATH_CERT)
            );
            return option -> option
                .setSsl(true)
                .setUseAlpn(true)
                .setPemKeyCertOptions(pem)
                .setOpenSslEngineOptions(new OpenSSLEngineOptions());
        }, options);
    }
}
