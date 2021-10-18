package io.vertx.up.uca.micro.ssl.client;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.ClientOptionsBase;
import io.vertx.up.uca.micro.ssl.TrustPipe;

public class JksTrust implements TrustPipe<JsonObject> {

    @Override
    public Handler<ClientOptionsBase> parse(final JsonObject options) {
        return null;
    }
}
