package io.vertx.tp.workflow.uca.deployment;

import org.camunda.bpm.engine.ProcessEngine;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractCamunda implements DeployStub {
    protected final transient ProcessEngine engine;

    protected AbstractCamunda(final ProcessEngine engine) {
        this.engine = engine;
    }
}
