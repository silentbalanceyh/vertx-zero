package io.vertx.tp.workflow.uca.deployment;

import io.vertx.core.Future;
import io.vertx.up.fn.Fn;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface DeployStub {

    static DeployStub create(final String folder) {
        return Fn.pool(Pool.POOL_DEPLOYE, folder, () -> new DeployBpmnService(folder));
    }

    // Deployment with service
    Future<Boolean> initialize();

    // Bind tentId
    default DeployStub tenant(final String tenantId) {
        return this;
    }
}
