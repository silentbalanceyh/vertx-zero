package io.vertx.up.boot.options;

import io.horizon.uca.log.Annal;
import io.vertx.core.ClusterOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.options.JTransformer;

public class ClusterSetUp implements JTransformer<ClusterOptions> {

    private static final Annal LOGGER = Annal.get(ClusterSetUp.class);

    @Override
    public ClusterOptions transform(final JsonObject config) {
        return Fn.runOr(null == config, LOGGER,
            ClusterOptions::new,
            () -> new ClusterOptions(config));
    }
}
