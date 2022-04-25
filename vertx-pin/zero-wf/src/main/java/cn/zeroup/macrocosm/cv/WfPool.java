package cn.zeroup.macrocosm.cv;

import io.vertx.tp.workflow.atom.EngineOn;
import io.vertx.tp.workflow.uca.component.Behaviour;
import io.vertx.tp.workflow.uca.component.Divert;
import io.vertx.tp.workflow.uca.deployment.DeployOn;
import io.vertx.tp.workflow.uca.modeling.ActionOn;
import io.vertx.tp.workflow.uca.runner.AidOn;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.tp.workflow.uca.runner.RunOn;
import io.vertx.tp.workflow.uca.runner.StoreOn;
import io.vertx.up.uca.cache.Cc;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface WfPool {
    // uca.runner POOL
    Cc<String, StoreOn> CC_STORE = Cc.openThread();
    Cc<String, RunOn> CC_RUN = Cc.openThread();
    Cc<String, EventOn> CC_EVENT = Cc.openThread();

    Cc<String, AidOn> CC_AID = Cc.openThread();

    // uca.modeling POOL
    Cc<String, ActionOn> CC_ACTION = Cc.openThread();

    // uca.deployment POOL
    Cc<String, DeployOn> CC_DEPLOY = Cc.open();
    Cc<String, EngineOn> CC_ENGINE = Cc.openThread();

    // uca.component POOL -> Transfer / Movement

    Cc<String, Behaviour> CC_COMPONENT = Cc.openThread();

    // uca.component POOL -> Divert
    Cc<String, Divert> CC_DIVERT = Cc.openThread();
}
