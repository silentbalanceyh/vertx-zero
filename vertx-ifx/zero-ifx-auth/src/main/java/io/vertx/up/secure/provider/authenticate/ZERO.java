package io.vertx.up.secure.provider.authenticate;

import io.vertx.ext.auth.authentication.AuthenticationProvider;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface Pool {
    ConcurrentMap<String, AuthenticationProvider> POOL_401 = new ConcurrentHashMap<>();
}
