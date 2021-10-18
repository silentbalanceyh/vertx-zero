package io.vertx.tp.ambient.atom;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    /*
     * DSLContext = AppDao in
     * For multi application usage, each application should has
     * only one AppDao that in to DSLContext.
     */
    ConcurrentMap<String, AtApp> APP_POOL
        = new ConcurrentHashMap<>();
}
