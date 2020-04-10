package io.vertx.up.uca.web.anima;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.up.eon.em.ServerType;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.rs.Extractor;
import io.vertx.up.uca.rs.config.AgentExtractor;
import io.vertx.up.uca.web.limit.ApiFactor;
import io.vertx.up.uca.web.limit.Factor;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentMap;

public class PointScatter implements Scatter<Vertx> {

    private static final Annal LOGGER = Annal.get(PointScatter.class);

    private transient final Factor factor = Ut.singleton(ApiFactor.class);

    @Override
    public void connect(final Vertx vertx) {
        /** 1.Find Agent for deploy **/
        final ConcurrentMap<ServerType, Class<?>> agents = this.factor.agents();
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
