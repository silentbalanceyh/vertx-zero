package io.vertx.up.uca.rs.config;

import io.horizon.uca.cache.Cc;
import io.vertx.core.DeploymentOptions;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.runtime.deployment.DeployRotate;
import io.vertx.up.runtime.deployment.Rotate;
import io.vertx.up.uca.rs.Extractor;
import io.vertx.up.util.Ut;

/**
 * Standard verticle deployment.
 */
public class AgentExtractor implements Extractor<DeploymentOptions> {

    private static final Annal LOGGER = Annal.get(AgentExtractor.class);
    private static final Cc<Class<?>, DeploymentOptions> CC_OPTIONS = Cc.open();

    @Override
    public DeploymentOptions extract(final Class<?> clazz) {
        Fn.runAt(() -> LOGGER.info(Info.AGENT_HIT, clazz.getName()), clazz);
        final Rotate rotate = Ut.singleton(DeployRotate.class);

        return CC_OPTIONS.pick(() -> rotate.spinAgent(clazz), clazz);
        // Fn.po?l(OPTIONS, clazz, () -> rotate.spinAgent(clazz));
    }
}
