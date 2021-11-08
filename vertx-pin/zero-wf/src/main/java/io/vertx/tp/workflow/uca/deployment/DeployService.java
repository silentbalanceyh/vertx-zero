package io.vertx.tp.workflow.uca.deployment;

import io.vertx.core.Future;
import io.vertx.tp.workflow.init.WfPin;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class DeployService extends AbstractCamunda {
    DeployService(final String workflow) {
        super(workflow, WfPin.engineCamunda());
    }

    @Override
    public Future<Boolean> initialize() {
        return null;
    }
}
