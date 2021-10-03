package io.vertx.up.secure.cv;

import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.auth.authorization.Authorization;
import io.vertx.ext.web.handler.AuthorizationHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface LeePool {
    // Provider
    ConcurrentMap<String, AuthenticationProvider> POOL_PROVIDER = new ConcurrentHashMap<>();
    // Authorization
    ConcurrentMap<String, Authorization> POOL_NO_ACCESS = new ConcurrentHashMap<>();
    // Handler
    ConcurrentMap<String, AuthorizationHandler> POOL_HANDLER = new ConcurrentHashMap<>();
}
