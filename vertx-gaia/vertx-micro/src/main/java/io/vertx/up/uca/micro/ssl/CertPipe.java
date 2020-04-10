package io.vertx.up.uca.micro.ssl;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.TCPSSLOptions;
import io.vertx.up.eon.em.CertType;
import io.vertx.up.uca.micro.ssl.server.JksCert;
import io.vertx.up.uca.micro.ssl.server.PemCert;
import io.vertx.up.uca.micro.ssl.server.PfxCert;
import io.vertx.up.util.Ut;

/**
 * Different ssl pipe
 *
 * @param <I>
 */
public interface CertPipe<I> {
    static CertPipe<JsonObject> get(final CertType type) {
        // 1. OpenSSL
        CertPipe<JsonObject> pipe = null;
        switch (type) {
            case PKCS12:
                pipe = Ut.singleton(PfxCert.class);
                break;
            case JKS:
                pipe = Ut.singleton(JksCert.class);
                break;
            case PEM:
                pipe = Ut.singleton(PemCert.class);
                break;
        }
        return pipe;
    }

    /**
     * Different pipe to parse JsonObject to generate Options
     *
     * @param options
     * @return
     */
    Handler<TCPSSLOptions> parse(I options);
}
