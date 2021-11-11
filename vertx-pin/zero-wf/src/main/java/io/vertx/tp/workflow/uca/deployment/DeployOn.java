package io.vertx.tp.workflow.uca.deployment;

import cn.zeroup.macrocosm.cv.WfPool;
import io.vertx.core.Future;
import io.vertx.up.fn.Fn;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface DeployOn {

    static DeployOn get(final String folder) {
        return Fn.pool(WfPool.POOL_DEPLOY, folder, () -> new DeployBpmnService(folder));
    }

    // Deployment with service
    Future<Boolean> initialize();

    // Bind tentId
    default DeployOn tenant(final String tenantId) {
        return this;
    }
}
