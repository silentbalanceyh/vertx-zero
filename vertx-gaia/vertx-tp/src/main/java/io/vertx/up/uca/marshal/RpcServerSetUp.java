package io.vertx.up.uca.marshal;

import io.horizon.uca.log.Annal;
import io.vertx.core.RpcOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.options.JTransformer;

public class RpcServerSetUp implements JTransformer<RpcOptions> {

    private static final Annal LOGGER = Annal.get(RpcServerSetUp.class);

    @Override
    public RpcOptions transform(final JsonObject input) {
        return Fn.runOr(null == input, LOGGER,
            RpcOptions::new,
            () -> new RpcOptions(input));
    }
}
