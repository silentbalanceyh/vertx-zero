package io.vertx.up.uca.web.anima;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.up.annotations.Worker;
import io.horizon.eon.em.container.MessageModel;
import io.vertx.up.log.Annal;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.runtime.ZeroHeart;
import io.vertx.up.uca.rs.Extractor;
import io.vertx.up.uca.rs.config.WorkerExtractor;
import io.vertx.up.util.Ut;
import io.vertx.up.verticle.ZeroHttpWorker;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Worker scatter to deploy workers
 */
public class WorkerScatter implements Scatter<Vertx> {

    @Override
    public void connect(final Vertx vertx) {
        /* 1.Find Workers for deploy **/
        final Set<Class<?>> sources = ZeroAnno.getWorkers();
        /* 2.Default Workers **/
        if (sources.isEmpty()) {
            sources.add(ZeroHttpWorker.class);
        }
        // Filter and extract by message model, this scatter only support
        // MessageModel equal REQUEST_RESPONSE
        final Set<Class<?>> workers = this.getTargets(sources);
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

    private Set<Class<?>> getTargets(final Set<Class<?>> sources) {
        final Set<Class<?>> workers = new HashSet<>();
        for (final Class<?> source : sources) {
            final MessageModel model = Ut.invoke(source.getAnnotation(Worker.class), "value");
            if (this.getModel().contains(model)) {
                workers.add(source);
            }
        }
        return workers;
    }

    protected Set<MessageModel> getModel() {
        // Enabled Micro model
        final Set<MessageModel> models = new HashSet<MessageModel>() {
            {
                this.add(MessageModel.REQUEST_RESPONSE);
            }
        };
        if (ZeroHeart.isEtcd()) {
            models.add(MessageModel.REQUEST_MICRO_WORKER);
        }
        return models;
    }

}
