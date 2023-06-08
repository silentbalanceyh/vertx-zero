package io.vertx.up.uca.micro.ssl.client;

import io.horizon.uca.log.Annal;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.ClientOptionsBase;
import io.vertx.core.net.OpenSSLEngineOptions;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.micro.ssl.TrustPipe;
import io.vertx.up.uca.micro.ssl.tls.Trust;

public class PemTrust implements TrustPipe<JsonObject> {

    private static final Annal LOGGER = Annal.get(PemTrust.class);

    private static final String PATH_CERT = "cert";

    @Override
    public Handler<ClientOptionsBase> parse(
        final JsonObject options) {
        return Fn.runOr(() -> {
            final PemTrustOptions pem = Fn.runOr(
                !options.containsKey(PATH_CERT), LOGGER,
                Trust.CLIENT_PEM,
                () -> new PemTrustOptions().addCertPath(PATH_CERT)
            );
            return option -> option
                .setSsl(true)
                .setUseAlpn(true)
                .setPemTrustOptions(pem)
                .setOpenSslEngineOptions(new OpenSSLEngineOptions());
        }, options);
    }
}
