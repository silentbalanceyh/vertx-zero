package io.vertx.up.boot.options;

import io.horizon.uca.log.Annal;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.options.JTransformer;
import io.vertx.up.uca.options.NodeVisitor;

import java.util.Objects;

public class VertxSetUp implements JTransformer<VertxOptions> {

    private static final Annal LOGGER = Annal.get(VertxSetUp.class);

    public static VertxOptions nativeOption() {
        final VertxOptions options = new VertxOptions();
        options.setMaxEventLoopExecuteTime(3000_000_000_000L);
        options.setMaxWorkerExecuteTime(3000_000_000_000L);
        options.setBlockedThreadCheckInterval(10000);
        options.setPreferNativeTransport(true);
        return options;
    }

    @Override
    public VertxOptions transform(final JsonObject input) {
        final JsonObject config = input.getJsonObject(NodeVisitor.YKEY_OPTIONS, null);
        final VertxOptions options = Fn.runOr(null == config, LOGGER,
            VertxOptions::new,
            () -> {
                assert Objects.nonNull(config) : "`config` should not be null";
                return new VertxOptions(config);
            });
        assert options != null;
        options.setPreferNativeTransport(true);
        return options;
    }
}
