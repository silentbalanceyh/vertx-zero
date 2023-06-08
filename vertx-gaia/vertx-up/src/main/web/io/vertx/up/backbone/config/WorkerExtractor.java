package io.vertx.up.backbone.config;

import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.vertx.core.DeploymentOptions;
import io.vertx.up.backbone.Extractor;
import io.vertx.up.boot.deployment.DeployRotate;
import io.vertx.up.boot.deployment.Rotate;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Worker bottle deployment
 */
public class WorkerExtractor implements Extractor<DeploymentOptions> {

    private static final Annal LOGGER = Annal.get(WorkerExtractor.class);

    private static final ConcurrentMap<Class<?>, DeploymentOptions>
        OPTIONS = new ConcurrentHashMap<>();
    private static final Cc<Class<?>, DeploymentOptions> CC_OPTIONS = Cc.open();

    @Override
    public DeploymentOptions extract(final Class<?> clazz) {
        Fn.runAt(() -> LOGGER.info(INFO.WORKER_HIT, clazz.getName()), clazz);
        final Rotate rotate = Ut.singleton(DeployRotate.class);

        return CC_OPTIONS.pick(() -> rotate.spinWorker(clazz), clazz);
        // Fn.po?l(OPTIONS, clazz, () -> rotate.spinWorker(clazz));
    }
}
