package io.vertx.rx.web.anima;

import io.horizon.uca.log.Annal;
import io.macrocosm.specification.config.HConfig;
import io.vertx.core.DeploymentOptions;
import io.vertx.rx.web.limit.RxFactor;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.up.backbone.Extractor;
import io.vertx.up.backbone.config.AgentExtractor;
import io.vertx.up.boot.anima.Factor;
import io.vertx.up.boot.anima.Scatter;
import io.vertx.up.eon.em.container.ServerType;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentMap;

public class AgentScatter implements Scatter<Vertx> {

    private static final Annal LOGGER = Annal.get(AgentScatter.class);

    private transient final Factor factor = Ut.singleton(RxFactor.class);

    @Override
    public void connect(final Vertx vertx, final HConfig config) {
        /* 1.Find Agent for deploy **/
        final ConcurrentMap<ServerType, Class<?>> agents = this.factor.endpoint(config);

        final Extractor<DeploymentOptions> extractor =
            Ut.instance(AgentExtractor.class);

        Ut.itMap(agents, (type, clazz) -> {
            // 2.1. Agent deployment options
            final DeploymentOptions option = extractor.extract(clazz);
            // 2.2. Agent deployment
            Verticles.deploy(vertx, clazz, option, LOGGER);
        });
    }
}
