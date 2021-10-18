package io.vertx.tp.modular.acc;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface Pool {
    ConcurrentMap<String, Rise> POOL_RAPID = new ConcurrentHashMap<>();
}
