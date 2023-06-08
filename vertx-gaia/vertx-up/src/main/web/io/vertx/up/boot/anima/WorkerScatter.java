package io.vertx.up.boot.anima;

import io.horizon.uca.log.Annal;
import io.macrocosm.specification.config.HConfig;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.up.annotations.Worker;
import io.vertx.up.backbone.Extractor;
import io.vertx.up.backbone.config.WorkerExtractor;
import io.vertx.up.bottle.ZeroHttpWorker;
import io.vertx.up.configuration.BootStore;
import io.vertx.up.eon.em.EmTraffic;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Worker scatter to deploy workers
 */
public class WorkerScatter implements Scatter<Vertx> {
    private static final BootStore STORE = BootStore.singleton();

    @Override
    public void connect(final Vertx vertx, final HConfig config) {
        /* 1.Find Workers for deploy **/
        final Set<Class<?>> sources = ZeroAnno.getWorkers();
        /* 2.Default Workers **/
        if (sources.isEmpty()) {
            sources.add(ZeroHttpWorker.class);
        }
        // Filter and extract by message model, this scatter only support
        // Exchange equal REQUEST_RESPONSE
        final Set<Class<?>> workers = this.getTargets(sources, config);
        final Extractor<DeploymentOptions> extractor =
            Ut.instance(WorkerExtractor.class);
        final ConcurrentMap<Class<?>, DeploymentOptions> options =
            new ConcurrentHashMap<>();
        for (final Class<?> worker : workers) {
            // 2.1 Worker deployment options
            final DeploymentOptions option = extractor.extract(worker);
            options.put(worker, option);
            // 2.2 Worker deployment
            Verticles.deploy(vertx, worker, option, this.getLogger());
        }
        // Runtime hooker
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
            Ut.itSet(workers, (clazz, index) -> {
                // 4. Undeploy Agent.
                final DeploymentOptions opt = options.get(clazz);
                Verticles.undeploy(vertx, clazz, opt, this.getLogger());
            })));
    }

    private Annal getLogger() {
        return Annal.get(this.getClass());
    }

    private Set<Class<?>> getTargets(final Set<Class<?>> sources, final HConfig config) {
        final Set<Class<?>> workers = new HashSet<>();
        for (final Class<?> source : sources) {
            final EmTraffic.Exchange model = Ut.invoke(source.getAnnotation(Worker.class), "value");
            if (this.getModel(config).contains(model)) {
                workers.add(source);
            }
        }
        return workers;
    }

    protected Set<EmTraffic.Exchange> getModel(final HConfig hConfig) {
        // Enabled Micro model
        final Set<EmTraffic.Exchange> models = new HashSet<EmTraffic.Exchange>() {
            {
                this.add(EmTraffic.Exchange.REQUEST_RESPONSE);
            }
        };
        if (STORE.isEtcd()) {
            models.add(EmTraffic.Exchange.REQUEST_MICRO_WORKER);
        }
        return models;
    }

}
