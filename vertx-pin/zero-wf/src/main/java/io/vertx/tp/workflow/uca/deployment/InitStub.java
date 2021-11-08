package io.vertx.tp.workflow.uca.deployment;

import io.vertx.core.Future;
import io.vertx.up.fn.Fn;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface InitStub {

    static InitStub create(final String folder) {
        return Fn.pool(Pool.POOL_DEPLOYE, folder, () -> new DeployService(folder));
    }

    // Deployment with service
    Future<Boolean> initialize();
}
