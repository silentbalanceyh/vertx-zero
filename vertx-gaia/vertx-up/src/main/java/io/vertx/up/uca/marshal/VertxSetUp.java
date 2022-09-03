package io.vertx.up.uca.marshal;

import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.options.NodeVisitor;

import java.util.Objects;

public class VertxSetUp implements JTransformer<VertxOptions> {

    private static final Annal LOGGER = Annal.get(VertxSetUp.class);

    @Override
    public VertxOptions transform(final JsonObject input) {
        final JsonObject config = input.getJsonObject(NodeVisitor.YKEY_OPTIONS, null);
        return Fn.orSemi(null == config, LOGGER,
            VertxOptions::new,
            () -> {
                assert Objects.nonNull(config) : "`config` should not be null";
                return new VertxOptions(config);
            });
    }
}
