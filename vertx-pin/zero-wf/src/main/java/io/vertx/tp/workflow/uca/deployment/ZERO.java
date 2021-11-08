package io.vertx.tp.workflow.uca.deployment;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface Pool {
    ConcurrentMap<String, InitStub> POOL_DEPLOYE = new ConcurrentHashMap<>();
}
