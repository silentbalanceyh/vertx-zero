package cn.zeroup.macrocosm.cv;

import io.vertx.tp.workflow.atom.EngineOn;
import io.vertx.tp.workflow.uca.component.Behaviour;
import io.vertx.tp.workflow.uca.deployment.DeployOn;
import io.vertx.tp.workflow.uca.modeling.ActionOn;
import io.vertx.tp.workflow.uca.modeling.TodoOn;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.tp.workflow.uca.runner.RunOn;
import io.vertx.tp.workflow.uca.runner.StoreOn;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface WfPool {
    ConcurrentMap<String, StoreOn> POOL_STORE = new ConcurrentHashMap<>();
    ConcurrentMap<String, RunOn> POOL_PROC = new ConcurrentHashMap<>();
    ConcurrentMap<String, EventOn> POOL_EVENT = new ConcurrentHashMap<>();

    ConcurrentMap<String, ActionOn> POOL_ACTION = new ConcurrentHashMap<>();
    ConcurrentMap<String, TodoOn> POOL_TODO = new ConcurrentHashMap<>();

    ConcurrentMap<String, DeployOn> POOL_DEPLOY = new ConcurrentHashMap<>();
    ConcurrentMap<String, EngineOn> POOL_ENGINE = new ConcurrentHashMap<>();

    ConcurrentMap<String, Behaviour> POOL_COMPONENT = new ConcurrentHashMap<>();
}
