package io.vertx.mod.workflow.uca.deployment;

import cn.vertxup.workflow.cv.WfPool;
import io.vertx.core.Future;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface DeployOn {

    static DeployOn get(final String folder) {
        return WfPool.CC_DEPLOY.pick(() -> new DeployBpmnService(folder), folder);
    }

    // Deployment with service
    Future<Boolean> initialize();

    // Bind tentId
    default DeployOn tenant(final String tenantId) {
        return this;
    }
}
