package io.vertx.up.uca.marshal;

import io.vertx.core.ClusterOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

public class ClusterStrada implements Transformer<ClusterOptions> {

    private static final Annal LOGGER = Annal.get(ClusterStrada.class);

    @Override
    public ClusterOptions transform(final JsonObject config) {
        return Fn.getSemi(null == config, LOGGER,
            ClusterOptions::new,
            () -> new ClusterOptions(config));
    }
}
