package cn.zeroup.macrocosm.cv;

import io.vertx.tp.workflow.atom.WEngine;
import io.vertx.tp.workflow.uca.deployment.DeployOn;
import io.vertx.tp.workflow.uca.runner.ProcOn;
import io.vertx.tp.workflow.uca.runner.StoreOn;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface WfPool {
    ConcurrentMap<String, StoreOn> POOL_STORE = new ConcurrentHashMap<>();
    ConcurrentMap<String, ProcOn> POOL_PROC = new ConcurrentHashMap<>();

    ConcurrentMap<String, DeployOn> POOL_DEPLOY = new ConcurrentHashMap<>();
    ConcurrentMap<String, WEngine> POOL_ENGINE = new ConcurrentHashMap<>();
}
