package io.vertx.up.secure.authenticate;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface Pool {
    ConcurrentMap<String, AdapterProvider> POOL_ADAPTER = new ConcurrentHashMap<>();
}
