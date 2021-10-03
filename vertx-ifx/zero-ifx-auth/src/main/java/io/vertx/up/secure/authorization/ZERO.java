package io.vertx.up.secure.authorization;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface Pool {
    ConcurrentMap<String, WallAuthorizationHandler> POOL = new ConcurrentHashMap<>();
}
