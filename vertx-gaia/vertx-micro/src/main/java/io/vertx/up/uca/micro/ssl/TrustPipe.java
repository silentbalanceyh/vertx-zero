package io.vertx.up.uca.micro.ssl;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.ClientOptionsBase;
import io.vertx.up.eon.em.CertType;
import io.vertx.up.uca.micro.ssl.client.JksTrust;
import io.vertx.up.uca.micro.ssl.client.PemTrust;
import io.vertx.up.uca.micro.ssl.client.PfxTrust;
import io.vertx.up.util.Ut;

public interface TrustPipe<I> {

    static TrustPipe<JsonObject> get(final CertType type) {
        // 1. OpenSSL
        TrustPipe<JsonObject> pipe = null;
        switch (type) {
            case PKCS12:
                pipe = Ut.singleton(PfxTrust.class);
                break;
            case JKS:
                pipe = Ut.singleton(JksTrust.class);
                break;
            case PEM:
                pipe = Ut.singleton(PemTrust.class);
                break;
        }
        return pipe;
    }

    Handler<ClientOptionsBase> parse(I options);
}
