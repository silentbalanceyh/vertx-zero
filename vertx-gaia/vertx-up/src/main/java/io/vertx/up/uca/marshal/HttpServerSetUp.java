package io.vertx.up.uca.marshal;

import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

import java.util.Objects;

public class HttpServerSetUp implements JTransformer<HttpServerOptions> {

    private static final Annal LOGGER = Annal.get(HttpServerSetUp.class);

    @Override
    public HttpServerOptions transform(final JsonObject input) {
        final JsonObject config = input.getJsonObject(KName.CONFIG, null);
        return Fn.orSemi(null == config, LOGGER,
            HttpServerOptions::new,
            () -> {
                assert Objects.nonNull(config) : "`config` is not null";
                return new HttpServerOptions(config);
            });
    }
}
