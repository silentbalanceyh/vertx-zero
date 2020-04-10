package io.vertx.tp.jet.monitor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    ConcurrentMap<Class<?>, JtMonitor> MONITORS = new ConcurrentHashMap<>();
}
