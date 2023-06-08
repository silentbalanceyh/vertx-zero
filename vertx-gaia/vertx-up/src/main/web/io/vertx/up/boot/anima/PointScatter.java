package io.vertx.up.boot.anima;

import io.horizon.uca.log.Annal;
import io.macrocosm.specification.config.HConfig;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.up.backbone.Extractor;
import io.vertx.up.backbone.config.AgentExtractor;
import io.vertx.up.eon.em.container.ServerType;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentMap;

public class PointScatter implements Scatter<Vertx> {

    private static final Annal LOGGER = Annal.get(PointScatter.class);

    private transient final Factor factor = Ut.singleton(FactorRpc.class);

    @Override
    public void connect(final Vertx vertx, final HConfig config) {
        /* 1.Find Agent for deploy **/
        final ConcurrentMap<ServerType, Class<?>> agents = this.factor.endpoint(config);
        final Extractor<DeploymentOptions> extractor =
            Ut.instance(AgentExtractor.class);
        Ut.itMap(agents, (type, clazz) -> {
            // 3.1 Agent deployment options
            final DeploymentOptions option = extractor.extract(clazz);
            // 3.2 Agent deployment
            Verticles.deploy(vertx, clazz, option, LOGGER);
        });
    }
}
