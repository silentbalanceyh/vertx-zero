package io.vertx.up.uca.marshal;

import io.vertx.core.RpcOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.options.JTransformer;

public class RpcServerSetUp implements JTransformer<RpcOptions> {

    private static final Annal LOGGER = Annal.get(RpcServerSetUp.class);

    @Override
    public RpcOptions transform(final JsonObject input) {
        return Fn.orSemi(null == input, LOGGER,
            RpcOptions::new,
            () -> new RpcOptions(input));
    }
}
