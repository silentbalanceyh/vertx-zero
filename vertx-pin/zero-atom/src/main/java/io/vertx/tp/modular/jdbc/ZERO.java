package io.vertx.tp.modular.jdbc;

import io.vertx.tp.plugin.database.DataPool;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    /* OxPool资源池 */
    ConcurrentMap<String, DataPool> POOL
        = new ConcurrentHashMap<>();
}
