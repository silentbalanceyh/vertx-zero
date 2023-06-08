package io.vertx.up.boot.anima;

import io.horizon.uca.log.Annal;
import io.macrocosm.specification.config.HConfig;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.up.backbone.Extractor;
import io.vertx.up.backbone.config.AgentExtractor;
import io.vertx.up.eon.em.container.ServerType;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Agent scatter to deploy agents
 */

public class AgentScatter implements Scatter<Vertx> {

    private static final Annal LOGGER = Annal.get(AgentScatter.class);

    private transient final Factor factor = Ut.singleton(FactorHttp.class);

    @Override
    public void connect(final Vertx vertx, final HConfig config) {
        /* 1.Find Agent for deploy **/
        final ConcurrentMap<ServerType, Class<?>> agents = this.factor.endpoint(config);
        final Extractor<DeploymentOptions> extractor =
            Ut.instance(AgentExtractor.class);
        /* 2.Record options**/
        final ConcurrentMap<Class<?>, DeploymentOptions> options =
            new ConcurrentHashMap<>();
        Ut.itMap(agents, (type, clazz) -> {
            // 3.1 Agent deployment options
            final DeploymentOptions option = extractor.extract(clazz);
            options.put(clazz, option);
            // 3.2 Agent deployment
            Verticles.deploy(vertx, clazz, option, LOGGER);
        });
        // Runtime hooker
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
            Ut.itMap(agents, (type, clazz) -> {
                // 4. Undeploy Agent.
                final DeploymentOptions opt = options.get(clazz);
                Verticles.undeploy(vertx, clazz, opt, LOGGER);
            })));
    }
}
