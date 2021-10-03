package io.vertx.up.secure.authorization;

import io.vertx.ext.auth.authorization.Authorization;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface Pool {
    ConcurrentMap<String, Authorization> NO_ACCESS_POOL = new ConcurrentHashMap<>();
    ConcurrentMap<String, PermissionHandler> HANDLER_POOL = new ConcurrentHashMap<>();
}
