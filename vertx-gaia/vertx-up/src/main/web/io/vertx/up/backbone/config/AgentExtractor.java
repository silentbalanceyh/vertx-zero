package io.vertx.up.backbone.config;

import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.vertx.core.DeploymentOptions;
import io.vertx.up.backbone.Extractor;
import io.vertx.up.boot.deployment.DeployRotate;
import io.vertx.up.boot.deployment.Rotate;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

/**
 * Standard bottle deployment.
 */
public class AgentExtractor implements Extractor<DeploymentOptions> {

    private static final Annal LOGGER = Annal.get(AgentExtractor.class);
    private static final Cc<Class<?>, DeploymentOptions> CC_OPTIONS = Cc.open();

    @Override
    public DeploymentOptions extract(final Class<?> clazz) {
        Fn.runAt(() -> LOGGER.info(INFO.AGENT_HIT, clazz.getName()), clazz);
        final Rotate rotate = Ut.singleton(DeployRotate.class);

        return CC_OPTIONS.pick(() -> rotate.spinAgent(clazz), clazz);
        // Fn.po?l(OPTIONS, clazz, () -> rotate.spinAgent(clazz));
    }
}
