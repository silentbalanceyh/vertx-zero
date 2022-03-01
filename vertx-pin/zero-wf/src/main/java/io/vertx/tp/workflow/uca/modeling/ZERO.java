package io.vertx.tp.workflow.uca.modeling;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface Pool {
    ConcurrentMap<String, Register> THREAD_POOL = new ConcurrentHashMap<>();
}