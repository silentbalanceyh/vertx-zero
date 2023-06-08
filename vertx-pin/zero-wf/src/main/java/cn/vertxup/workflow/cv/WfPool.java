package cn.vertxup.workflow.cv;

import io.horizon.uca.cache.Cc;
import io.vertx.mod.workflow.atom.EngineOn;
import io.vertx.mod.workflow.uca.camunda.Io;
import io.vertx.mod.workflow.uca.camunda.RunOn;
import io.vertx.mod.workflow.uca.central.Behaviour;
import io.vertx.mod.workflow.uca.component.MoveOn;
import io.vertx.mod.workflow.uca.deployment.DeployOn;
import io.vertx.mod.workflow.uca.modeling.ActionOn;
import org.camunda.bpm.engine.repository.ProcessDefinition;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface WfPool {

    @SuppressWarnings("all")
    Cc<String, Io> CC_IO = Cc.openThread();
    Cc<String, ProcessDefinition> CC_DEFINITION = Cc.open();
    Cc<String, RunOn> CC_RUN = Cc.openThread();

    // uca.modeling POOL
    Cc<String, ActionOn> CC_ACTION = Cc.openThread();

    // uca.deployment POOL
    Cc<String, DeployOn> CC_DEPLOY = Cc.open();
    Cc<String, EngineOn> CC_ENGINE = Cc.openThread();

    // uca.component POOL -> Transfer / Movement

    Cc<String, Behaviour> CC_COMPONENT = Cc.openThread();

    // uca.component POOL -> MoveOn
    Cc<String, MoveOn> CC_MOVE_ON = Cc.openThread();
}
